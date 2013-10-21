package com.quidsi.log.analyzing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UnFileFactory {

    private final static Logger logger = LoggerFactory.getLogger(UnFileFactory.class);

    public static void unGz(File file) {
        StringBuilder dstDirectoryPath = new StringBuilder();
        String srcGzPath = file.getName();
        dstDirectoryPath.append(file.getParent());
        try (GZIPInputStream in = new GZIPInputStream(new FileInputStream(file))) {

            byte[] buf = new byte[1024];
            int len;

            srcGzPath = srcGzPath.replace(dstDirectoryPath.toString(), "");
            FileUtils.folderIsExists(dstDirectoryPath.append("//decompression").toString());
            dstDirectoryPath.append("//");
            File outFile = new File(dstDirectoryPath.append(srcGzPath.replace(".gz", "")).toString());
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
}
