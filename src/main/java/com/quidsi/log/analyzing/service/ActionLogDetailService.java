package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.dao.ActionLogDetailDao;
import com.quidsi.log.analyzing.domain.ActionLogDetail;

@Service
public class ActionLogDetailService {

    private ActionLogDetailDao actionLogDetailDao;

    @Transactional
    public void saveList(List<ActionLogDetail> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (ActionLogDetail record : records) {
                actionLogDetailDao.save(record);
            }
        }
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        return actionLogDetailDao.getRecordsByLogId(logId);
    }

    @Inject
    public void setActionLogDetailDao(ActionLogDetailDao actionLogDetailDao) {
        this.actionLogDetailDao = actionLogDetailDao;
    }

}
