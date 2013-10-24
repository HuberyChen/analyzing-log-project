package com.quidsi.log.analyzing.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

public class ScanUtils {

    public static List<String> scanDirectoryFileName(String path) {
        List<String> filesName = new ArrayList<>();
        File root = new File(path);
        if (root.exists()) {
            for (File f : root.listFiles()) {
                filesName.add(f.getName());
            }
        }
        return filesName;
    }

    public static List<String> scanDirectoryFilePath(String path) {
        List<String> pathList = new ArrayList<>();
        File root = new File(path);
        if (root.exists()) {
            for (File f : root.listFiles()) {
                pathList.add(f.getAbsolutePath());
            }
        }
        return pathList;
    }

    public static String scanPathFileName(String path) {
        File file = new File(path);
        return file.getName();
    }

    public static List<String> scan(String path, List<String> pathFilters, List<String> nameFilters) {
        List<String> logs = new ArrayList<>();

        File root = new File(path);
        if (root.exists()) {
            Stack<File> fileStack = new Stack<File>();
            fileStack.add(root);

            final List<Pattern> fileNamePatterns = new ArrayList<Pattern>();
            final List<Pattern> pathPatterns = new ArrayList<Pattern>();

            if (nameFilters != null && nameFilters.size() > 0) {
                for (String f : nameFilters) {
                    fileNamePatterns.add(Pattern.compile(f));
                }
            }

            if (pathFilters != null && pathFilters.size() > 0) {
                for (String f : pathFilters) {
                    pathPatterns.add(Pattern.compile(f));
                }
            }

            while (!fileStack.isEmpty()) {
                final File file = fileStack.pop();
                if (file.isDirectory()) {
                    for (File f : file.listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            if (file.isDirectory())
                                return true;

                            if (CollectionUtils.isEmpty(fileNamePatterns)) {
                                return true;
                            }

                            if (!CollectionUtils.isEmpty(fileNamePatterns)) {
                                for (Pattern p : fileNamePatterns) {
                                    if (!p.matcher(file.getName()).matches()) {
                                        return false;
                                    }
                                }
                                return true;
                            }

                            return false;
                        }
                    })) {
                        fileStack.add(f);
                    }
                } else {
                    String filePath = file.getParent();
                    boolean pathIsMatch = true;
                    if (!CollectionUtils.isEmpty(pathPatterns)) {
                        for (Pattern p : pathPatterns) {
                            if (!p.matcher(filePath).matches()) {
                                pathIsMatch = false;
                                break;
                            }
                        }
                    }
                    if (pathIsMatch) {
                        logs.add(file.getAbsolutePath());
                    }
                }

            }
        }
        return logs;
    }
}
