package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.ActionLogDetailDao;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
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
        return actionLogDetailDao.findConditionLimit(searchDetailCondition);
    }

    public int getTotalCountByCondition(SearchDetailCondition searchDetailCondition) {
        return actionLogDetailDao.getTotalCountByCondition(searchDetailCondition);
    }

    public List<ActionLogDetail> findDetail(List<Integer> logIdList, int offset) {
        return actionLogDetailDao.findList(logIdList, offset);
    }

    public int getTotalCount(List<Integer> logIdList) {
        return actionLogDetailDao.getTotalCount(logIdList);
    }

    public List<ActionLogDetail> getRecordsByLogId(int logId) {
        return actionLogDetailDao.getRecordsByLogId(logId);
    }

    @Inject
    public void setActionLogDetailDao(ActionLogDetailDao actionLogDetailDao) {
        this.actionLogDetailDao = actionLogDetailDao;
    }

}
