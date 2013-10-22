package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.dao.ActionLogRecordDao;
import com.quidsi.log.analyzing.domain.ActionLogRecord;

@Service
public class APILogRecordService {

    private ActionLogRecordDao actionLogRecordDao;

    @Transactional
    public void saveList(List<ActionLogRecord> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (ActionLogRecord record : records) {
                actionLogRecordDao.save(record);
            }
        }
    }

    @Inject
    public void setActionLogRecordDao(ActionLogRecordDao actionLogRecordDao) {
        this.actionLogRecordDao = actionLogRecordDao;
    }
}
