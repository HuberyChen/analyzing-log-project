package com.quidsi.log.analyzing.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

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

        List<String> logs = ScanUtils.scan("D:\\test", filterMap.get("pathFilters"), filterMap.get("nameFilters"));
        if (CollectionUtils.isEmpty(logs)) {
            return;
        }
        for (String logMessage : logs) {
            System.out.println(logMessage);
        }
    }

}
