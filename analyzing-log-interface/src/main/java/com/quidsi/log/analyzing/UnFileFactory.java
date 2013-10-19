package com.quidsi.log.analyzing;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

public class UnFileFactory implements IFileOperation {

    public void fileOpeartion(String filePath) {
        deCompression(filePath);
    }

    public static final String GZ_POSTFIX = ".gz";

    private void deCompression(String filePath) {
        if (filePath.endsWith(GZ_POSTFIX)) {
            unGz(filePath);
        }
    }

    @SuppressWarnings("resource")
    private void unGz(String srcGzPath) {
        File file = new File(srcGzPath);
        StringBuilder dstDirectoryPath = new StringBuilder();
        dstDirectoryPath.append(file.getParent());
        GZIPInputStream in;
        try {
            in = new GZIPInputStream(new FileInputStream(srcGzPath));

            // Transfer bytes from the compressed file to the output file
            byte[] buf = new byte[1024];
            int len;

            // Open the output file
            srcGzPath = srcGzPath.replace(dstDirectoryPath.toString(), "");
            FileUtils.folderIsExists(dstDirectoryPath.append("//decompression").toString());
            File outFile = new File(dstDirectoryPath.append(srcGzPath.replace(".gz", "")).toString());
            if (!outFile.exists()) {
                OutputStream out = new FileOutputStream(outFile);
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
