package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.ActionLogDetailDao;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.domain.TempLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActionLogDetailService {

    private ActionLogDetailDao actionLogDetailDao;

    @Transactional
    public void save(ActionLogDetail detail) {
        actionLogDetailDao.save(detail);
    }

    @Transactional
    public void saveList(List<ActionLogDetail> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (ActionLogDetail record : records) {
                actionLogDetailDao.save(record);
            }
        }
    }

    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
        List<TempLog> tempLogs = actionLogDetailDao.findConditionLimitId(searchDetailCondition);
        if (CollectionUtils.isEmpty(tempLogs)) {
            return null;
        }
        List<Integer> ids = new ArrayList<>();
        for (TempLog tempLog : tempLogs) {
            ids.add(tempLog.getId());
        }
        return actionLogDetailDao.findConditionLimit(ids);
    }

    public int getTotalCountByCondition(SearchDetailCondition searchDetailCondition) {
        return actionLogDetailDao.getTotalCountByCondition(searchDetailCondition);
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        return actionLogDetailDao.getRecordsByLogId(logId);
    }

    @Inject
    public void setActionLogDetailDao(ActionLogDetailDao actionLogDetailDao) {
        this.actionLogDetailDao = actionLogDetailDao;
    }
}