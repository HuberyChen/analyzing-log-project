package com.quidsi.log.analyzing.dao;

import org.junit.Test;

import java.io.File;

public class PathTest {

    @Test
    public void pathTest() {
        // String path = "D:\\test";
        String path = "\\\\mnt\\prodlog";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
        }
    }

}
