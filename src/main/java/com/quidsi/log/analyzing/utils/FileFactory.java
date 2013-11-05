package com.quidsi.log.analyzing.utils;

import com.quidsi.log.analyzing.service.ServiceConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class FileFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileFactory.class);

    public static Map<Integer, String[]> logRead(File file) {

        Map<Integer, String[]> messageMap = new HashMap<>();

        int line = 0;

        try (BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))) {
            String str = bufferReader.readLine();
            while (null != str) {
                String[] messages = str.split("\\|");
                messageMap.put(line, messages);
                line++;
                str = bufferReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messageMap;
    }

    public static String unGz(File file) {
        String separator = File.separator;
        StringBuilder dstDirectoryPath = new StringBuilder();
        String srcGzPath = file.getName();
        dstDirectoryPath.append(file.getParent());
        srcGzPath = srcGzPath.replace(dstDirectoryPath.toString(), "");
        FileUtils.folderIsExists(dstDirectoryPath.append(separator + ServiceConstant.DECOMPRESSION).toString());
        dstDirectoryPath.append(separator);
        File outFile = new File(dstDirectoryPath.append(srcGzPath.replace(ServiceConstant.GZ_SUFFIX, "")).toString());
        LOGGER.info("log path={}", dstDirectoryPath.toString());
        if (outFile.exists()) {
            return null;
        }
        LOGGER.info("out file is not exists");
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
             OutputStream out = new FileOutputStream(outFile)) {
            byte[] buf = new byte[1024];
            int len = in.read(buf);
            while (len > 0) {
                out.write(buf, 0, len);
                len = in.read(buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("out file path={}", outFile.getAbsolutePath());
        return outFile.getAbsolutePath();
    }
}
