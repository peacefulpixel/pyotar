package org.example.corp.engine.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.*;
import java.util.stream.Collectors;

import static java.util.logging.Level.WARNING;

public class LoggerUtils {

    private static final Logger nonFileLogger = Logger.getLogger(LoggerUtils.class.getName());

    public static boolean isFileLoggingEnabled = true;
    public static String logDir = "log/";
    public static String logName = "game_log";

    private static FileHandler handler;

    private static void createFileHandler(String path) throws IOException {
        handler = new FileHandler(path);
        Formatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
    }

    @SuppressWarnings("rawtypes")
    public synchronized static Logger getLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        if (isFileLoggingEnabled) {
            if (handler == null) {
                File dir = new File(logDir);
                if (!dir.exists()) {
                    if (!dir.mkdir()) {
                        nonFileLogger.warning("Unable to create dir " + logDir);
                        return logger;
                    }
                    try {
                        createFileHandler(logDir + logName + ".0");
                    } catch (IOException e) {
                        nonFileLogger.log(WARNING, "Unable to create log file", e);
                        return logger;
                    }
                }
                try {
                    List<Integer> logNumbers = Files.list(dir.toPath()).filter(p -> !Files.isDirectory(p))
                            .map(p -> Integer.parseUnsignedInt(new LinkedList<>(
                                    Arrays.asList(p.toString().split("\\."))
                            ).getLast())).sorted().collect(Collectors.toList()); // Is it even worth xD?
                    if (logNumbers.isEmpty()) {
                        createFileHandler(logDir + logName + ".0");
                    } else {
                        int logNumber = logNumbers.get(logNumbers.size() - 1) + 1;
                        createFileHandler(logDir + logName + "." + logNumber);
                    }
                } catch (IOException e) {
                    nonFileLogger.log(WARNING, "Unable to create log file", e);
                    return logger;
                }
            }
            logger.addHandler(handler);
        }
        return Logger.getLogger(clazz.getName());
    }
}
