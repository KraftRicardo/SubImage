package util;

import org.junit.jupiter.api.*;
import util.Logger;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {
    private Path tempLog;

    @BeforeEach
    void setup() throws Exception {
        // Create a "logs" folder inside the project
        Path logDir = Path.of("src/test/resources/test-logs");
        if (!Files.exists(logDir)) {
            Files.createDirectories(logDir);
        }

        // Create a temporary file for testing
        tempLog = logDir.resolve("test-log-" + System.currentTimeMillis() + ".log");
        System.out.println("Test log file: " + tempLog.toAbsolutePath());

        // Reset the logger before each test
        Logger.reset();
        Logger.setLoggingEnabled(true);
        Logger.setLogLevel(Logger.LogLevel.DEBUG);
        Logger.setWriter(new PrintWriter(tempLog.toFile(), "UTF-8"));
    }

    @AfterEach
    void cleanup() {
        Logger.close();

        // Note: Comment out the line below to keep it so you can inspect
        // tempLog.toFile().delete();
    }

    @Test
    void testDebugManyMessages() throws Exception {
        for(int i = 0; i < 10; i++){
            Logger.debug("Debug message %d", i);
            Logger.info("Info message %d", i);
            Logger.warning("Warning message %d", i);
            Logger.error("Error message %d",i);
        }

        // Force flush because the logger also flushing only every so often
        Logger.flush();

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);

        for(int i = 0; i < 10; i++){
            assertTrue(content.contains(String.format("Debug message %d", i)), String.format("Missing: Debug message %d", i));
            assertTrue(content.contains(String.format("Info message %d", i)), String.format("Missing: Info message %d", i));
            assertTrue(content.contains(String.format("Warning message %d", i)), String.format("Missing: Warning message %d", i));
            assertTrue(content.contains(String.format("Error message %d", i)), String.format("Missing: Error message %d", i));
        }
    }

    @Test
    void testDebugWritesToFile() throws Exception {
        Logger.debug("Hello %s", "World");

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);
        assertTrue(content.contains("Hello World"), "Debug message should be in the log file");
    }

    @Test
    void testInfoWritesToFile() throws Exception {
        Logger.info("Info message %d", 42);

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);
        assertTrue(content.contains("Info message 42"), "Info message should be in the log file");
    }

    @Test
    void testWarningRespectsLogLevel() throws Exception {
        Logger.setLogLevel(Logger.LogLevel.ERROR); // WARNING is below ERROR

        Logger.debug("This debug message should NOT appear");
        Logger.info("This info message should NOT appear");
        Logger.warning("This warning should NOT appear");

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);
        assertFalse(content.contains("NOT appear"), "This message should not be logged when logLevel=ERROR");
    }

    @Test
    void testErrorLogsException() throws Exception {
        Exception e = new RuntimeException("Boom!");
        Logger.error(e);

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);
        assertTrue(content.contains("Boom!"), "Exception message should be logged");
    }

    @Test
    void testResetAllowsReinit() throws Exception {
        Logger.debug("First message");
        Logger.reset();
        Logger.setWriter(new PrintWriter(tempLog.toFile(), "UTF-8"));
        Logger.debug("Second message");

        String content = Files.readString(tempLog);
        System.out.println("Content of log file:\n" + content);
        assertTrue(content.contains("Second message"), "After reset, logger should log new messages");
    }
}
