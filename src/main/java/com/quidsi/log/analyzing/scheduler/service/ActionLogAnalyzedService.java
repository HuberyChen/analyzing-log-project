package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.ScheduleService;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

@Service
public class ActionLogAnalyzedService {

    private LogFileOperation logFileOperation;
    private ScheduleService scheduleService;

    public void actionLogAnalyzed(ActionLogSchedule actionLogSchedule, List<LogFileWrapper> logFileWrappers) {
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            actionLogSchedule.setNote(actionLogSchedule.getNote() + "analyzing action log in project = " + logFileWrapper.getProject().getName() + " and instance = "
                    + logFileWrapper.getServer().getServerName() + ". ");
            scheduleService.update(actionLogSchedule);
            logFileWrapper.setActionLogSchedule(actionLogSchedule);

            logFileOperation.saveLogFilesNotExisted(logFileWrapper);

            logFileOperation.decompression(logFileWrapper);

            logFileOperation.saveActionLogDetail(logFileWrapper);
        }

        actionLogSchedule.setStatus(ActionLogSchedule.ScheduleStatus.SUCCESS);
        actionLogSchedule.setEffectiveEndTime(new Date());
        actionLogSchedule.setNote(actionLogSchedule.getNote() + "end schedule.");
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
