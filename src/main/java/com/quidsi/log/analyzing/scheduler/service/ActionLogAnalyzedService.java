package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.ScheduleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

@Service
public class ActionLogAnalyzedService {

    private LogFileOperation logFileOperation;
    private ScheduleService scheduleService;

    @Transactional
    public void actionLogAnalyzed(ActionLogSchedule actionLogSchedule, List<LogFileWrapper> logFileWrappers) {
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            actionLogSchedule.setNote(actionLogSchedule.getNote() + "analyzing action log in project = " + logFileWrapper.getProject().getName() + " and instance = "
                    + logFileWrapper.getServer().getServerName() + ". ");
            scheduleService.update(actionLogSchedule);
            logFileOperation.saveLogFilesNotExisted(logFileWrapper);
            logFileOperation.decompression(logFileWrapper);
            logFileOperation.saveActionLogDetail(logFileWrapper);
            if (!StringUtils.hasText(logFileWrapper.getNote())) {
                logFileWrapper.setNote("have not operation. ");
            }
            actionLogSchedule.setNote(actionLogSchedule.getNote() + logFileWrapper.getNote());
            scheduleService.update(actionLogSchedule);
        }

        actionLogSchedule.setStatus(ActionLogSchedule.ScheduleStatus.SUCCESS);
        actionLogSchedule.setEffectiveEndTime(new Date());
        scheduleService.update(actionLogSchedule);
    }

    @Inject
    public void setLogFileOperation(LogFileOperation logFileOperation) {
        this.logFileOperation = logFileOperation;
    }

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
