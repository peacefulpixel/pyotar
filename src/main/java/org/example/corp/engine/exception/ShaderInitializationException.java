package org.example.corp.engine.exception;

public class ShaderInitializationException extends EngineException {

    public ShaderInitializationException(String message) {
        super(message);
    }

    public ShaderInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
