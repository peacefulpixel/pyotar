package org.example.corp.engine.util;

public class ExceptionUtils {

    public static <T extends Exception> T setCause(T exception, Throwable cause) {
        exception.initCause(cause);
        return exception;
    }
}
