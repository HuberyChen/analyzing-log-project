package com.quidsi.log.analyzing;


import java.io.File;
import java.util.Stack;

public class FileScan implements IFileScanOperation {

    private String path;
    private IFileOperation operation;

    public void getTime() {
        scan();
    }

    public FileScan(String path, IFileOperation operation) {
        this.path = path;
        this.operation = operation;
    }

    private void scan() {
        File root = new File(path);
        if (root.exists()) {
            Stack<File> fileStack = new Stack<File>();
            fileStack.add(root);

            while (!fileStack.isEmpty()) {
                final File file = fileStack.pop();
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        fileStack.add(f);
                    }
                } else {
                    operation.fileOpeartion(file.getAbsolutePath());
                }
            }
        }
    }

}
