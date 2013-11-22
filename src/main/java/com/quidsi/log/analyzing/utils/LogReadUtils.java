package com.quidsi.log.analyzing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author hubery.chen
 */
public class LogReadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogReadUtils.class);

    public static String logRead(String path) {
        String log = "";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            log = sb.toString();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return log;
    }
}
