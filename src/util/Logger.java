package util;

import lombok.Getter;
import lombok.Setter;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe Logger with colored console output, optional file logging, and multiple log levels (DEBUG, INFO, WARNING,
 * ERROR). Supports injection of a PrintWriter for testing.
 */
public class Logger {

	/**
	 * Log levels in increasing verbosity
	 */
	public enum LogLevel {ERROR, WARNING, INFO, DEBUG}

	// ANSI escape codes for colors
	private static final String RESET = "\u001B[0m";
	private static final String RED = "\u001B[31m";
	private static final String YELLOW = "\u001B[33m";
	private static final String BLUE = "\u001B[34m";
	private static final String GREEN = "\u001B[32m";

	@Setter @Getter private static boolean loggingEnabled = false;
	@Setter @Getter private static LogLevel logLevel = LogLevel.DEBUG;

	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final DateTimeFormatter FILE_NAME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

	private static PrintWriter writer;
	private static long lastFlush = 0;

	/**
	 * Initializes the logger. If file logging is enabled, creates a timestamped log file.
	 * Safe to call multiple times; does nothing if already initialized.
	 */
	public static synchronized void init(String dirPath) {
		if (writer != null) {
			return;
		}

		LocalDateTime now = LocalDateTime.now();
		System.out.println("Logging started: " + now);

		if (!loggingEnabled) {
			return;
		}

		String fileName = dirPath + "/" + FILE_NAME_FORMAT.format(now) + ".log";
		try {
			writer = new PrintWriter(new FileWriter(fileName, true));
			writer.println("Logging started: " + now);
			writer.flush();
		} catch (IOException e) {
			System.err.println("Could not initialize log file: " + e.getMessage());
		}
	}

	/**
	 * Closes the logger and flushes any remaining data.
	 */
	public static synchronized void close() {
		if (writer != null) {
			writer.println("Logging stopped");
			writer.flush();
			writer.close();
			writer = null;
		}
	}

	/**
	 * Resets the logger for testing or re-initialization.
	 * Safely closes any existing writer.
	 */
	public static synchronized void reset() {
		close();
		lastFlush = 0;
	}

	public static synchronized void flush() {
		if (writer != null) {
			writer.flush();
		}
	}

	/**
	 * Injects a PrintWriter for logging (useful for testing).
	 *
	 * @param pw the PrintWriter to use for logging
	 */
	public static synchronized void setWriter(PrintWriter pw) {
		writer = pw;
	}

	/**
	 * Logs a debug message.
	 */
	public static void debug(String msg, Object... args) {
		log(LogLevel.DEBUG, BLUE, msg, args);
	}

	/**
	 * Logs an info message.
	 */
	public static void info(String msg, Object... args) {
		log(LogLevel.INFO, GREEN, msg, args);
	}

	/**
	 * Logs a warning message.
	 */
	public static void warning(String msg, Object... args) {
		log(LogLevel.WARNING, YELLOW, msg, args);
	}

	/**
	 * Logs an error message.
	 */
	public static void error(String msg, Object... args) {
		log(LogLevel.ERROR, RED, msg, args);
	}

	/**
	 * Logs an exception with stacktrace.
	 */
	public static void error(Exception e) {
		error(e.getMessage());
		for (StackTraceElement element : e.getStackTrace()) {
			writeColored(RED, "[STACKTRACE] " + element);
		}
	}

	/**
	 * Logs a formatted message at the given level with color.
	 */
	private static synchronized void log(LogLevel level, String color, String msg, Object... args) {
		if (logLevel.ordinal() < level.ordinal()) {
			return;
		}

		String text = (args.length > 0) ? String.format(msg, args) : msg;
		String timestamp = TIME_FORMAT.format(LocalDateTime.now());
		String line = String.format("%s[%s %s] %s%s", color, level, timestamp, RESET, text);

		writeColored(color, line);
	}

	/**
	 * Writes a colored message to the console and optionally to the log file.
	 */
	private static synchronized void writeColored(String color, String line) {
		if (color.equals(RED)) System.err.println(line);
		else System.out.println(line);

		writeToFile(line);
	}

	/**
	 * Writes a message to the log file if enabled.
	 * Will flush max every 2000 milliseconds due to keeping the performance loss low.
	 */
	private static synchronized void writeToFile(String line) {
		if (!loggingEnabled || writer == null) {
			return;
		}

		// Remove ANSI escape codes
		String cleanLine = line.replaceAll("\u001B\\[[;\\d]*m", "");

		writer.println(cleanLine);

		long now = System.currentTimeMillis();
		if (now - lastFlush > 2000) {
			writer.flush();
			lastFlush = now;
		}
	}
}
