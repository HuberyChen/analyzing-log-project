package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.SystemAPIDao;
import com.quidsi.log.analyzing.domain.SystemAPI;

@Service
public class SystemAPIService {

    private SystemAPIDao systemAPIDao;

    @Transactional
    public int save(SystemAPI systemAPI) {
        return systemAPIDao.save(systemAPI);
    }

    public SystemAPI getSystemAPIByName(String apiName) {
        return systemAPIDao.getSystemAPIByName(apiName);
    }

    public List<SystemAPI> getSystemAPIs() {
        return systemAPIDao.getSystemAPIs();
    }

    @Inject
    public void setSystemAPIDao(SystemAPIDao systemAPIDao) {
        this.systemAPIDao = systemAPIDao;
    }

}
