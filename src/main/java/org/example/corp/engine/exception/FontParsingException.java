package org.example.corp.engine.exception;

public class FontParsingException extends EngineException {

    public FontParsingException(String message) {
        super(message);
    }

    public FontParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
