package com.quidsi.log.analyzing.path;

import java.io.File;

import org.junit.Test;

import com.quidsi.log.analyzing.SpringTest;

public class PathTest extends SpringTest {

    @Test
    public void pathTest() {
        // String path = "D:\\test";
        String path = "\\\\sharedoc\\文件交换区\\Java-Team\\prod log";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
        }
    }
}
