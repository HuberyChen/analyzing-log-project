package com.quidsi.log.analyzing.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScanUtilsTest {

    @Test
    public void scanUtilsTest() {

        Map<String, List<String>> filterMap = new HashMap<>();

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add("[\\S]*GIFTCOSERVER[\\S]*");
        pathFilters.add("[\\S]*Prod-gcsvc1[\\S]*");
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S]*.log[\\S]*");

        filterMap.put("nameFilters", nameFilters);

        List<File> logs = ScanUtils.scan("D:\\test", filterMap.get("pathFilters"), filterMap.get("nameFilters"));
        System.out.println("");
    }

}
