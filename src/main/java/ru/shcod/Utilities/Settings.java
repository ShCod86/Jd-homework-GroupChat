package ru.shcod.Utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Settings settings;
    private final static Logger LOGGER = Logger.getInstance();
    private final static String  SETTINGS_PATH = "settings.properties";
    private int port;
    private String host;

    private Settings() {
        try(FileReader fileReader = new FileReader(SETTINGS_PATH)) {
            Properties properties = new Properties();
            properties.load(fileReader);
            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("ip");
        } catch (FileNotFoundException e) {
            LOGGER.log("File settings not found: " + e.getMessage());
            throw new RuntimeException("File settings not found");
        } catch (IOException e) {
            LOGGER.log(e.getMessage());
        }
    }
    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
    public int getPort(){
        return port;
    }
    public String  getHost(){
        return host;
    }
}
