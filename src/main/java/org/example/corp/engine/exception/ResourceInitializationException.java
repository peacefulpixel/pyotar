package org.example.corp.engine.exception;

public class ResourceInitializationException extends EngineException {

    public ResourceInitializationException(String message) {
        super(message);
    }

    public ResourceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
