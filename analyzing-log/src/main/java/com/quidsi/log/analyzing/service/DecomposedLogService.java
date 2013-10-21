package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.dao.DecomposedLogDao;
import com.quidsi.log.analyzing.domain.DecomposedLog;

@Service
public class DecomposedLogService {

    private DecomposedLogDao decomposedLogDao;

    @Transactional
    public int save(DecomposedLog log) {
        return decomposedLogDao.save(log);
    }

    @Transactional
    public void saveList(List<DecomposedLog> logs) {
        if (!CollectionUtils.isEmpty(logs)) {
            for (DecomposedLog log : logs) {
                decomposedLogDao.save(log);
            }
        }
    }

    public DecomposedLog getDecomposedLogByName(String name) {
        return decomposedLogDao.getDecomposedLogByName(name);
    }

    @Inject
    public void setDecomposedLogDao(DecomposedLogDao decomposedLogDao) {
        this.decomposedLogDao = decomposedLogDao;
    }
}
