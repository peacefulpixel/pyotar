package org.example.corp.engine.exception;

public class EngineInitializationException extends EngineException {

    public EngineInitializationException(String message) {
        super(message);
    }

    public EngineInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
