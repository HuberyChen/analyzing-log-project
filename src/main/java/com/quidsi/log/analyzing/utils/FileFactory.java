package com.quidsi.log.analyzing.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quidsi.log.analyzing.service.ServiceConstant;

public final class FileFactory {

    private final static Logger logger = LoggerFactory.getLogger(FileFactory.class);

    public static Map<Integer, String[]> logRead(File file) {

        Map<Integer, String[]> messageMap = new HashMap<>();

        int line = 0;

        String str = "";
        try (InputStream inputStream = new FileInputStream(file)) {
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            try (BufferedReader bufferReader = new BufferedReader(inputReader)) {

                while ((str = bufferReader.readLine()) != null) {
                    String[] messages = str.split("\\|");
                    messageMap.put(line, messages);
                    line++;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return messageMap;
    }

    public static String unGz(File file) {
        StringBuilder dstDirectoryPath = new StringBuilder();
        String srcGzPath = file.getName();
        String absolutePath = "";
        dstDirectoryPath.append(file.getParent());
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(file))) {

            byte[] buf = new byte[1024];
            int len;

            srcGzPath = srcGzPath.replace(dstDirectoryPath.toString(), "");
            FileUtils.folderIsExists(dstDirectoryPath.append("//" + ServiceConstant.DECOMPRESSION).toString());
            dstDirectoryPath.append("//");
            File outFile = new File(dstDirectoryPath.append(srcGzPath.replace(ServiceConstant.GZ_SUFFIX, "")).toString());
            if (!outFile.exists()) {
                try (OutputStream out = new FileOutputStream(outFile)) {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            absolutePath = outFile.getAbsolutePath();

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return absolutePath;
    }
}
