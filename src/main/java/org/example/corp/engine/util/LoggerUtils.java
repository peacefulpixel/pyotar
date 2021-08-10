package org.example.corp.engine.util;

import java.util.logging.Logger;

public class LoggerUtils {

    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
