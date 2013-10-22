package com.quidsi.log.analyzing.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.monitor.Track;
import com.quidsi.log.analyzing.domain.APIHost;
import com.quidsi.log.analyzing.domain.APILog;
import com.quidsi.log.analyzing.service.APIHostService;
import com.quidsi.log.analyzing.service.APILogService;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Controller
public class APILogController {

    private APIHostService apiHostService;
    private APILogService apiLogService;

    @RequestMapping(value = "/api/host/log", method = RequestMethod.GET)
    @ResponseBody
    @Track(warningThresholdInMs = 5000)
    public void scanAPIHostLog(@Valid @RequestParam String root) {
        List<APIHost> hostList = apiHostService.getApiHosts();
        if (!CollectionUtils.isEmpty(hostList)) {
            for (APIHost apiHost : hostList) {
                Map<String, List<String>> filterMap = apiLogService.initializeFilters(apiHost);
                List<File> logs = ScanUtils.scan(root, filterMap.get("pathFilters"), filterMap.get("nameFilters"));
                if (!CollectionUtils.isEmpty(logs)) {
                    for (File log : logs) {
                        APILog apiLog = apiLogService.getApiLogByName(log.getName(), apiHost.getApiName(), apiHost.getHostName());
                        if (null == apiLog) {
                            apiLogService.save(apiLog);
                        }
                    }
                }
            }
        }
    }

    @Inject
    public void setApiHostService(APIHostService apiHostService) {
        this.apiHostService = apiHostService;
    }

    @Inject
    public void setApiLogService(APILogService apiLogService) {
        this.apiLogService = apiLogService;
    }

}
