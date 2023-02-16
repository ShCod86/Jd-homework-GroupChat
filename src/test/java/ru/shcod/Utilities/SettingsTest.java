package ru.shcod.Utilities;

import org.junit.jupiter.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    private static final Settings SETTINGS = Settings.getInstance();
    protected int port;
    protected String host;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Start tests.");
    }
    @AfterAll
    public static void completeSuite() {
        System.out.println("Complete all tests.");
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Starting new nest.");
        final String path = "settings.properties";
        try (FileReader fileReader = new FileReader(path)) {
            Properties props = new Properties();
            props.load(fileReader);

            port = Integer.parseInt(props.getProperty("port"));
            host = props.getProperty("ip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    public void completeTest() {
        System.out.println("Complete test.");
    }

    @Test
    public void getInstance() {
        assertEquals(Settings.getInstance(), SETTINGS);
    }

    @Test
    public void getPort() {
        assertEquals(SETTINGS.getPort(), port);
    }

    @Test
    public void getHost() {
        assertEquals(SETTINGS.getHost(), host);
    }
}