package util;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static final int ERROR = 0;
	public static final int WARNING = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;

	public static boolean loggingEnabled = false;
	public static int logLevel = DEBUG;

	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	static FileWriter fileWriter;

	public synchronized static void init() {
		if (fileWriter != null)
			return;

		try {
			Date date = new Date();
			System.out.println("Logging started:   " + date.toString());
			initFileWriter(date);
		} catch (IOException e) {
			System.err.println("Could not initialize logging!");
		}
	}

	private synchronized static void initFileWriter(Date date) throws IOException {
		if (loggingEnabled) {
			fileWriter = new FileWriter(dateFormat.format(date) + ".log");
			fileWriter.write("Logging started:   " + date.toString() + "\n");
			fileWriter.flush();
		}
	}

	public synchronized static void close() {
		try {
			if (fileWriter != null) {
				fileWriter.write("Logging stopped");
				fileWriter.flush();
				fileWriter.close();
			}
		} catch (IOException e) {
			System.err.println("Could not close log file!");
		}

//		System.out.println("Logging stopped");
	}

	public synchronized static void crash(String message, Object ... args){
		if (args.length > 0) {
			message = String.format(message, args);
		}

		crash(message, new RuntimeException());
	}

	public synchronized static void crash(String message, Exception e) {
		error(message);
		crash(e);
	}

	public synchronized static void crash(Exception e) {
		if (logLevel >= ERROR) {
			Date date = new Date();
			String logString = "[CRASH] " + timeFormat.format(date) + " :   " + e.getCause() + "  " + e.getMessage() + "\n";
			System.err.print(logString);

			writeToLog(logString);
			for (StackTraceElement element : e.getStackTrace()) {
				logString = "[STACKTRACE]:\t\t" + element.toString() + "\n";
				System.err.print(logString);
				writeToLog(logString);
			}
			close();
		}

		System.exit(-1);
	}

	public synchronized static void debug(String string, Object... args) {
		if (args.length > 0) {
			string = String.format(string, args);
		}

		if (logLevel >= DEBUG) {
			Date date = new Date();
			String logString = "[DEBUG] " + timeFormat.format(date) + ":\t\t" + string + "\n";
			System.out.print(logString);
			writeToLog(logString);
		}
	}

	public synchronized static void info(String string, Object... args) {
		if (args.length > 0) {
			string = String.format(string, args);
		}
		if (logLevel >= INFO) {
			Date date = new Date();
			String logString = "[INFO] " + timeFormat.format(date) + ":\t\t" + string + "\n";
			System.out.print(logString);
			writeToLog(logString);
		}
	}

	public synchronized static void warning(String string, Object... args) {
		if (args.length > 0) {
			string = String.format(string, args);
		}

		if (logLevel >= WARNING) {
			Date date = new Date();
			String logString = "[WARNING] " + timeFormat.format(date) + ":\t\t" + string + "\n";
			System.out.print(logString);
			writeToLog(logString);
		}
	}

	public synchronized static void error(String string, Object... args) {
		if (args.length > 0) {
			string = String.format(string, args);
		}

		if (logLevel >= ERROR) {
			Date date = new Date();
			String logString = "[ERROR] " + timeFormat.format(date) + ":\t\t" + string + "\n";
			System.err.print(logString);
			writeToLog(logString);
		}
	}

	public synchronized static void error(Exception e) {
		if (logLevel >= ERROR) {
			Date date = new Date();
			String logString = "[ERROR] " + timeFormat.format(date) + ":\t\t" + e.getMessage() + "\n";
			System.err.print(logString);
			writeToLog(logString);

			for (StackTraceElement element : e.getStackTrace()) {
				logString = "[STACKTRACE]:\t\t" + element.toString() + "\n";
				System.err.print(logString);
				writeToLog(logString);
			}
		}
	}

	public synchronized static void error(String string, Exception e) {
		error(string);
		error(e);
	}


	private static long lastFlush = 0;

	private synchronized static void writeToLog(String logString) {
		if (loggingEnabled) {
			try {
				fileWriter.write(logString);
				if (System.currentTimeMillis() - lastFlush > 2000) {
					fileWriter.flush();
					lastFlush = System.currentTimeMillis();
				}
			} catch (IOException e) {
				System.err.println("Could not write to log file!");
			}
		}
	}

	public synchronized static void enableLogging() {
		loggingEnabled = true;
	}

	public synchronized static void disableLogging() {
		loggingEnabled = false;
	}

	public static void setLogLevel(int level) {
		logLevel = level;
	}

    public static void errorIf(boolean b, String msg, Object... args) {
		if(b){
			error(msg, args);
		}
    }
}