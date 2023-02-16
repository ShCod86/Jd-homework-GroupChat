package ru.shcod.Client;

import ru.shcod.Utilities.Logger;
import ru.shcod.Utilities.Settings;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final Logger LOGGER = Logger.getInstance();
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner cons;
    private Socket socket;
    private Thread thread;
    private Thread inThread;

    public static void main(String[] args) {
        new Client();
    }

    public void sendMsg(String msg) {
        writer.println(msg);
    }

    public void closeConnection() {
        try {
            reader.close();
            writer.close();
            cons.close();
            socket.close();
        } catch (IOException e) {
            LOGGER.log(e.getMessage());
            e.printStackTrace();
        }
    }

    public Client() {
        try {
            Settings settings = Settings.getInstance();
            socket = new Socket(settings.getHost(), settings.getPort());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            cons = new Scanner(System.in);

        } catch (IOException e) {
            LOGGER.log(e.getMessage());
            closeConnection();
        }

        thread = new Thread(() -> {
            String message;
            while ((message = cons.nextLine()) != null) {
                if (message.equals("/exit")) {
                    sendMsg(message);
                    break;
                } else {
                    sendMsg(message);
                }
            }
        });
        inThread = new Thread(() -> {
            String incomingMsg;
            while (true) {
                try {
                    if (((incomingMsg = reader.readLine()) == null))
                        break;
                } catch (IOException e) {
                    LOGGER.log(e.getMessage());
                    throw new RuntimeException(e);
                }
                System.out.println(incomingMsg);
            }
        });
        inThread.start();
        thread.start();
    }

}
