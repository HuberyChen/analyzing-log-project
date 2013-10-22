package com.quidsi.log.analyzing.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.monitor.Track;
import com.quidsi.log.analyzing.service.APIHostService;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.SystemAPIService;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Controller
public class SystemAPIController {

    private SystemAPIService systemAPIService;
    private DataConver dataConver;
    private APIHostService apiHostService;

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    @ResponseBody
    @Track(warningThresholdInMs = 5000)
    public void scanSystemAPI(@Valid @RequestParam String root) {
        List<String> apiNameList = ScanUtils.scanDirectoryFileName(root);
        if (!CollectionUtils.isEmpty(apiNameList)) {
            for (String apiName : apiNameList) {
                if (null == systemAPIService.getSystemAPIByName(apiName)) {
                    systemAPIService.save(dataConver.dataConverToSystemAPI(apiName));
                }
            }
        }
    }

    @RequestMapping(value = "/api/host", method = RequestMethod.GET)
    @ResponseBody
    @Track(warningThresholdInMs = 5000)
    public void scanAPIHost(@Valid @RequestParam String root) {
        List<String> pathList = ScanUtils.scanDirectoryFilePath(root);
        if (!CollectionUtils.isEmpty(pathList)) {
            for (String path : pathList) {
                List<String> hostsNameList = ScanUtils.scanDirectoryFileName(path);
                String apiName = ScanUtils.scanPathFileName(path);
                if (!CollectionUtils.isEmpty(hostsNameList)) {
                    for (String hostName : hostsNameList) {
                        if (null == apiHostService.getHostsByAPIAndHost(apiName, hostName)) {
                            apiHostService.save(dataConver.dataConverToApiHost(apiName, hostName));
                        }
                    }
                }
            }
        }
    }

    @Inject
    public void setSystemAPIService(SystemAPIService systemAPIService) {
        this.systemAPIService = systemAPIService;
    }

    @Inject
    public void setDataConver(DataConver dataConver) {
        this.dataConver = dataConver;
    }

    @Inject
    public void setApiHostService(APIHostService apiHostService) {
        this.apiHostService = apiHostService;
    }
}
