package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.APILogDao;
import com.quidsi.log.analyzing.domain.APIHost;
import com.quidsi.log.analyzing.domain.APILog;

@Service
public class APILogService {

    private APILogDao apiLogDao;

    @Transactional
    public int save(APILog apiLog) {
        return apiLogDao.save(apiLog);
    }

    public APILog getApiLogByName(String logName, String apiName, String hostName) {
        return apiLogDao.getApiLogsByName(logName, apiName, hostName);
    }

    public Map<String, List<String>> initializeFilters(APIHost apiHost) {
        Map<String, List<String>> filterMap = new HashMap<>();

        List<String> pathFilters = new ArrayList<>();
        pathFilters.add("[//S]*" + apiHost.getApiName() + "[//S]*");
        pathFilters.add("[//S]*" + apiHost.getHostName() + "[//S]*");
        filterMap.put("pathFilters", pathFilters);

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S]*.log[\\S]*");

        filterMap.put("nameFilters", nameFilters);
        return filterMap;
    }

    @Inject
    public void setApiLogDao(APILogDao apiLogDao) {
        this.apiLogDao = apiLogDao;
    }
}
