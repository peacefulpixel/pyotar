package org.example.corp.engine.exception;

import org.example.corp.engine.util.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.corp.engine.util.ExceptionUtils.setCause;

public class EngineException extends Exception {

    private final Logger logger = LoggerUtils.getLogger(EngineException.class);

    public EngineException(String message) {
        super(message);
        logger.log(Level.INFO, message, this);
    }

    public EngineException(String message, Throwable cause) {
        super(message);
        setCause(this, cause);
        logger.log(Level.INFO, message, this);
    }
}
