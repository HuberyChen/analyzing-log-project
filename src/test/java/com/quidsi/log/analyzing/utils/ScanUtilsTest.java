package com.quidsi.log.analyzing.utils;

import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.service.ServiceConstant;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.File;
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

    @Test
    public void fileFactoryTest() {
        FileFactory.unGz(new File("D:\\test\\giftco-service\\Prod-gcsvc1\\2013\\10\\19\\action\\giftco-service-action.2013-11-02_05.log.gz"));
    }

    @Test
    public void logFileTest() {
        LogFile logFile = new LogFile();
        logFile.setProjectId(1);
        logFile.setServerId(1);
        logFile.setLogName("1");
        logFile.setAbsolutePath("test");

        Map<Integer, LogFile> map = new HashMap<>();
        map.put(logFile.getId(), logFile);

        LogFile logFile2 = new LogFile();
        logFile2.setProjectId(1);
        logFile2.setServerId(1);
        logFile2.setLogName("1");
        logFile2.setAbsolutePath("1");

        System.out.println(map.get(logFile2.getId()).getAbsolutePath());
        System.out.println(logFile2.getAbsolutePath());
        System.out.println(map.containsValue(logFile2));
    }

}
