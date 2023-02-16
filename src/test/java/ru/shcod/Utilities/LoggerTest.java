package ru.shcod.Utilities;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    private static Logger logger;
    private static String message;
    @BeforeAll
    public static void initSuite() {
        System.out.println("Start tests.");
        logger = Logger.getInstance();
        message = "Test message LoggerTest";
    }
    @AfterAll
    public static void completeSuite() {
        logger = null;
        message = null;
        System.out.println("Complete all tests.");
    }
    @BeforeEach
    public void initTest() {
        System.out.println("Starting new nest.");
    }
    @AfterEach
    public void completeTest() {
        System.out.println("Complete test.");
    }

    @Test
    void getInstance() {
        assertEquals(Logger.getInstance(), logger);
    }

    @Test
    void log() {
        assertTrue(logger.log(message));
    }
}