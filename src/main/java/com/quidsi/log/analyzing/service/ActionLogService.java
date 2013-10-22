package com.quidsi.log.analyzing.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.ActionLogDao;
import com.quidsi.log.analyzing.domain.ActionLog;

@Service
public class ActionLogService {

    private ActionLogDao actionLogDao;

    @Transactional
    public void save(ActionLog actionLog) {
        actionLogDao.save(actionLog);
    }

    @Inject
    public void setActionLogDao(ActionLogDao actionLogDao) {
        this.actionLogDao = actionLogDao;
    }
}
