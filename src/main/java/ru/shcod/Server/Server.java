package ru.shcod.Server;


import ru.shcod.Utilities.Logger;
import ru.shcod.Utilities.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final Logger LOGGER = Logger.getInstance();
    private ArrayList<Connection> connections;
    private ServerSocket server;
    private Thread thread;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        Settings settings = Settings.getInstance();
        connections = new ArrayList<>();
        try {
            LOGGER.log("Start server...");
            System.out.println("Start server...");
            server = new ServerSocket(settings.getPort());
            while (!server.isClosed()) {
                Connection client = new Connection(server.accept());
                thread = new Thread(client);
                connections.add(client);
                thread.start();
            }
        } catch (IOException e) {
            serverClose();
        }
    }

    public void serverClose() {
        if (!server.isClosed()) {
            try {
                server.close();
            } catch (IOException e) {
                LOGGER.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public class Connection implements Runnable {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String userName;

        public Connection(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String msg) {
            writer.println(msg);
        }

        public void sendMessageToAll(String msg) {
            for (Connection connection : connections) {
                if (!connection.userName.equals(this.userName)) {
                    connection.sendMessage(msg);
                }
            }
        }
//        connection != null &&

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("Enter your name: ");
                userName = reader.readLine();
                LOGGER.log(userName + socket.getInetAddress() + ": connected.");
                System.out.println(userName + ": connected.");
                sendMessageToAll(userName + ": connect to chat!");

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.equals("/exit")) {
                        LOGGER.log(userName + ": was left the chat.");
                        sendMessageToAll(userName + ": was left the chat.");
                        connections.remove(this);
                        break;
                    } else {
                        LOGGER.log("%s: %s".formatted(userName, message));
                        sendMessageToAll("%s: %s".formatted(userName, message));
                    }
                }
            } catch (IOException e) {
                LOGGER.log(userName + ": was disconnect.");
                sendMessageToAll(userName + ": was disconnect.");
            } finally {
                LOGGER.log("Connection was close.");
                connectionClose();
            }
        }

        public void connectionClose() {
            try {
                reader.close();
                writer.close();
                socket.close();
            } catch (IOException e) {
                LOGGER.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
