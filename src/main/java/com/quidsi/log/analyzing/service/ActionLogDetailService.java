package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.ActionLogDetailDao;
import com.quidsi.log.analyzing.dao.TempDao;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.domain.TempId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActionLogDetailService {

    private ActionLogDetailDao actionLogDetailDao;
    private TempDao tempDao;

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

    @Transactional
    public List<ActionLogDetail> findConditionLimit(SearchDetailCondition searchDetailCondition) {
        List<TempId> tempIds = initializeTempId(actionLogDetailDao.createConditionLimitIdTemp(searchDetailCondition));
        if (CollectionUtils.isEmpty(tempIds)) {
            throw new IllegalStateException("tempId is null");
        }
        for (TempId tempId : tempIds) {
            tempDao.save(tempId);
        }
        return actionLogDetailDao.findConditionLimit(searchDetailCondition);
    }

    private List<TempId> initializeTempId(List<Integer> detailIds) {
        List<TempId> tempIds = new ArrayList<>();
        if (CollectionUtils.isEmpty(detailIds)) {
            throw new IllegalStateException("detail id is null");
        }
        for (Integer detailId : detailIds) {
            TempId tempId = new TempId();
            tempId.setDetailId(detailId);
            tempIds.add(tempId);
        }
        return tempIds;
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

    @Inject
    public void setTempDao(TempDao tempDao) {
        this.tempDao = tempDao;
    }
}
