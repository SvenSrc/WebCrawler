package logger;

import java.net.URI;
import java.time.LocalTime;

public class Logger {

    public static void logInfo(String info, URI uri) {
        System.out.println("[%s][Thread: %s] %s: %s".formatted(
                LocalTime.now(),
                Thread.currentThread().getName(),
                info,
                uri
        ));
    }

    public static void logError(String error, URI uri, Exception exception) {
        System.err.println("[%s][Thread: %s] %s: %s - %s".formatted(
                LocalTime.now(),
                Thread.currentThread().getName(),
                error,
                uri,
                exception.getMessage()
        ));
    }

    public static void logCounter(String counter, int amount){
        System.out.println("[%s][Thread: %s] %s: %s".formatted(
                LocalTime.now(),
                Thread.currentThread().getName(),
                counter,
                amount
        ));
    };

    public static void logFinish(){
        System.out.println("[%s][Thread: %s] Finished. Shutting down".formatted(
                LocalTime.now(),
                Thread.currentThread().getName()
        ));
    };
}
