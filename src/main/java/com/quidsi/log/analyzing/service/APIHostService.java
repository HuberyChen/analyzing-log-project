package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.APIHostDao;
import com.quidsi.log.analyzing.domain.APIHost;

@Service
public class APIHostService {

    private APIHostDao apiHostDao;

    @Transactional
    public int save(APIHost apiHost) {
        return apiHostDao.save(apiHost);
    }

    public APIHost getHostsByAPIAndHost(String apiName, String hostName) {
        return apiHostDao.getHostsByAPIAndHost(apiName, hostName);
    }

    public List<APIHost> getApiHosts() {
        return apiHostDao.getApiHosts();
    }

    @Inject
    public void setApiHostDao(APIHostDao apiHostDao) {
        this.apiHostDao = apiHostDao;
    }

}
