package org.example.corp.engine.util;

import java.io.*;

public class FileUtils {

    public static String readFileAsString(String path) throws IOException {
        return readFileAsString(new File(path));
    }

    public static String readFileAsString(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String buf;
        while ((buf = reader.readLine()) != null) {
            builder.append(buf).append("\n");
        }

        return builder.toString();
    }
}
