package com.quidsi.log.analyzing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author hubery.chen
 */
public class LogReadUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogReadUtils.class);

    public static String logRead(String path) {
        LOGGER.info("start log file read");
        String log = "";
        File file = new File(path);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))) {
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

        LOGGER.info("end log file read");
        return log;
    }
}
