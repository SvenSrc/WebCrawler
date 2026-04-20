package logger;

import java.net.URI;
import java.time.LocalTime;

/**
 * Utility class providing static logging methods for the application.
 */
public class Logger {

    /**
     * Logs an info message to standard output.
     * Includes the current timestamp and thread name.
     *
     * @param info  A short description of the information to log
     * @param uri   The URI related to the log message
     */
    public static void logInfo(String info, URI uri) {
        System.out.printf(
                "[%s][Thread: %s] %s: %s%n", LocalTime.now(),
                Thread.currentThread().getName(),
                info,
                uri
        );
    }

    /**
     * Logs an error message to standard error output.
     * Includes the current timestamp, thread name, and exception message.
     *
     * @param error     A short description of the error
     * @param uri       The URI related to the error
     * @param exception The exception that caused the error
     */
    public static void logError(String error, URI uri, Exception exception) {
        System.err.printf(
                "[%s][Thread: %s] %s: %s - %s%n", LocalTime.now(),
                Thread.currentThread().getName(),
                error,
                uri,
                exception.getMessage()
        );
    }

    /**
     * Logs a counter value to standard output.
     * Used to track increments and decrements of active scans/crawls and downloads.
     * Includes the current timestamp and thread name.
     *
     * @param counter   A short description of the counter being logged
     * @param amount    The current value of the counter
     */
    public static void logCounter(String counter, int amount) {
        System.out.printf(
                "[%s][Thread: %s] %s: %s%n", LocalTime.now(),
                Thread.currentThread().getName(),
                counter,
                amount
        );
    }

    /**
     * Logs a finish message to standard output when the crawler shuts down.
     * Includes the current timestamp and thread name.
     */
    public static void logFinish() {
        System.out.printf(
                "[%s][Thread: %s] Finished. Shutting down%n", LocalTime.now(),
                Thread.currentThread().getName()
        );
    }
}
