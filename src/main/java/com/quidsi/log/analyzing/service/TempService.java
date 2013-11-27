package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.TempDao;
import com.quidsi.log.analyzing.domain.TempId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hubery.chen
 */
@Service
public class TempService {

    private TempDao tempDao;

    @Transactional
    public void saveList(List<TempId> tempIds) {
        if (CollectionUtils.isEmpty(tempIds)) {
            return;
        }
        for (TempId tempId : tempIds) {
            tempDao.save(tempId);
        }
    }

    @Transactional
    public void deleteTemp() {
        List<TempId> tempIds = tempDao.findAll();
        if (CollectionUtils.isEmpty(tempIds)) {
            return;
        }
        for (TempId tempId : tempIds) {
            tempDao.delete(tempId);
        }
    }

    public void saveTemp(List<Integer> logIdList) {
        if (CollectionUtils.isEmpty(logIdList)) {
            return;
        }
        this.saveList(initializeTempId(logIdList));
    }

    private List<TempId> initializeTempId(List<Integer> logIdList) {
        List<TempId> tempIds = new ArrayList<>();
        for (Integer logId : logIdList) {
            TempId tempId = new TempId();
            tempId.setLogId(logId);
            tempIds.add(tempId);
        }
        return tempIds;
    }

    @Transactional
    public void dataStoreIntoTemp(List<Integer> logIdList) {
        this.deleteTemp();
        this.saveTemp(logIdList);
    }

    @Inject
    public void setTempDao(TempDao tempDao) {
        this.tempDao = tempDao;
    }
}
