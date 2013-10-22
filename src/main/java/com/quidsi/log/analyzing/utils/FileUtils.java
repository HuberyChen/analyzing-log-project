package com.quidsi.log.analyzing.utils;


import java.io.File;

public class FileUtils {

    public static void folderIsExists(String path) {
        File file = new File(path);
        folderIsExists(file);
    }

    public static void folderIsExists(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
