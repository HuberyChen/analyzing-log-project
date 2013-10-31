package com.quidsi.log.analyzing.utils;

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

import com.quidsi.log.analyzing.service.ServiceConstant;

public final class FileFactory {

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
        StringBuilder dstDirectoryPath = new StringBuilder();
        String srcGzPath = file.getName();
        dstDirectoryPath.append(file.getParent());
        byte[] buf = new byte[1024];
        srcGzPath = srcGzPath.replace(dstDirectoryPath.toString(), "");
        FileUtils.folderIsExists(dstDirectoryPath.append("//" + ServiceConstant.DECOMPRESSION).toString());
        dstDirectoryPath.append("//");
        File outFile = new File(dstDirectoryPath.append(srcGzPath.replace(ServiceConstant.GZ_SUFFIX, "")).toString());
        if (!outFile.exists()) {
            return null;
        }
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(file)); 
             OutputStream out = new FileOutputStream(outFile)) {
            while (in.read(buf) > 0) {
                out.write(buf, 0, in.read(buf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outFile.getAbsolutePath();
    }
}
