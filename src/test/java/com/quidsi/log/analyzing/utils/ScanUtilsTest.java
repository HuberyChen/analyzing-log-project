package com.quidsi.log.analyzing.utils;

import com.quidsi.log.analyzing.service.ServiceConstant;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanUtilsTest {

    @Test
    public void scanUtilsTest() {

        Map<String, List<String>> filterMap = new HashMap<>();

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add(ServiceConstant.MATCH_ALL + "giftco-service" + ServiceConstant.MATCH_ALL);
        pathFilters.add(ServiceConstant.MATCH_ALL + "Prod-gcsvc1" + ServiceConstant.MATCH_ALL);
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add(ServiceConstant.MATCH_ALL + ".log" + ServiceConstant.MATCH_ALL);
        nameFilters.add(ServiceConstant.MATCH_ALL + "2013-11-02" + ServiceConstant.MATCH_ALL);
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
