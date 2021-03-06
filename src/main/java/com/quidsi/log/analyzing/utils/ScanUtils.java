package com.quidsi.log.analyzing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class ScanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanUtils.class);

    public static List<String> scanDirectoryFileName(String path) {
        List<String> filesName = new ArrayList<>();
        File root = new File(path);
        if (!root.exists()) {
            return null;
        }
        for (File f : root.listFiles()) {
            filesName.add(f.getName());
        }
        return filesName;
    }

    public static List<String> scanSecondaryDirectoryFileName(String path, String pathFilter) {
        List<String> filesName = new ArrayList<>();
        File root = new File(path);
        if (!root.exists()) {
            return null;
        }
        for (File file : root.listFiles()) {
            if (Pattern.compile(pathFilter).matcher(file.getName()).matches()) {
                filesName.addAll(scanDirectoryFileName(file.getAbsolutePath()));
            }
        }
        return filesName;
    }

    public static String scanPathFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    public static List<String> scan(String path, List<String> pathFilters, List<String> nameFilters) {
        List<String> logs = new ArrayList<>();
        File root = new File(path);
        if (!root.exists()) {
            return null;
        }
        LOGGER.info("root exists");
        Stack<File> fileStack = new Stack<>();
        fileStack.add(root);
        final List<Pattern> namePatterns = new ArrayList<>();
        final List<Pattern> pathPatterns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(nameFilters)) {
            for (String f : nameFilters) {
                namePatterns.add(Pattern.compile(f));
            }
        }
        if (!CollectionUtils.isEmpty(pathFilters)) {
            for (String p : pathFilters) {
                pathPatterns.add(Pattern.compile(p));
            }
        }
        while (!fileStack.isEmpty()) {
            final File file = fileStack.pop();
            if (!file.isDirectory()) {
                logs.add(file.getAbsolutePath());
                continue;
            }
            for (File f : file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return judgeAccept(file, pathPatterns, namePatterns);
                }
            })) {
                fileStack.add(f);
            }
        }
        return logs;
    }

    public static boolean judgeAccept(File file, List<Pattern> pathPatterns, List<Pattern> namePatterns) {
        if (file.isDirectory() && CollectionUtils.isEmpty(pathPatterns)) {
            return true;
        }

        if (file.isDirectory() && !CollectionUtils.isEmpty(pathPatterns)) {
            for (Pattern p : pathPatterns) {
                if (p.matcher(file.getAbsolutePath()).matches()) {
                    return true;
                }
            }
        }

        if (!CollectionUtils.isEmpty(pathPatterns)) {
            for (Pattern p : pathPatterns) {
                if (!p.matcher(file.getAbsolutePath()).matches()) {
                    return false;
                }
            }
        }

        if (!CollectionUtils.isEmpty(namePatterns)) {
            for (Pattern p : namePatterns) {
                if (!p.matcher(file.getName()).matches()) {
                    return false;
                }
            }
        }
        return true;
    }

}
