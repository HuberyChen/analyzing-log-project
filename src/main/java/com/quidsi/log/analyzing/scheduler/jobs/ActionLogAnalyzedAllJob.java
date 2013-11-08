package com.quidsi.log.analyzing.scheduler.jobs;

import com.quidsi.core.platform.scheduler.Job;
import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.scheduler.service.ActionLogAnalyzedService;
import com.quidsi.log.analyzing.service.DataValidate;
import com.quidsi.log.analyzing.service.ErrorHandlingService;
import com.quidsi.log.analyzing.service.ScheduleService;
import com.quidsi.log.analyzing.service.ServiceConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

public class ActionLogAnalyzedAllJob extends Job {
    private final Logger logger = LoggerFactory.getLogger(ActionLogAnalyzedAllJob.class);

    private DataValidate dataValidate;
    private String path;
    private ScheduleService scheduleService;
    private ActionLogAnalyzedService actionLogAnalyzedService;
    private ErrorHandlingService errorHandlingService;

    @Override
    @Transactional
    protected void execute() throws Throwable {

        StopWatch stopWatch = new StopWatch();
        logger.debug("start analyzing action log");

        List<ActionLogSchedule> schedules = scheduleService.getSchedulesRunning();
        if (!CollectionUtils.isEmpty(schedules)) {
            return;
        }

        ActionLogSchedule actionLogSchedule = generateSchedule();
        List<LogFileWrapper> logFileWrappers = dataValidate.initializeAllActionLogFileWrappers(path, actionLogSchedule);

        if (0 == scheduleService.save(actionLogSchedule)) {
            String errMsg = "schedule save failure";
            errorHandlingService.errorHandling(errMsg, actionLogSchedule);
            throw new IllegalStateException(errMsg);
        }

        actionLogAnalyzedService.actionLogAnalyzed(actionLogSchedule, logFileWrappers);

        logger.debug("end start analyzing action log, time {}ms", stopWatch.elapsedTime());
    }

    private ActionLogSchedule generateSchedule() {
        ActionLogSchedule schedule = new ActionLogSchedule();
        schedule.setStartDate(dataValidate.dateConverToString(new Date()));
        schedule.setEndDate(dataValidate.dateConverToString(new Date()));
        schedule.setEffectiveStartTime(new Date());
        schedule.setProject(ServiceConstant.TYPE_ALL);
        schedule.setInstance(ServiceConstant.TYPE_ALL);
        schedule.setStatus(ActionLogSchedule.ScheduleStatus.RUNNING);
        schedule.setNote("start default schedule. ");
        return schedule;
    }

    @Inject
    public void setDataValidate(DataValidate dataValidate) {
        this.dataValidate = dataValidate;
    }

    @Inject
    public void setPath(@Value("${portal.path}") String path) {
        this.path = path;
    }

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Inject
    public void setActionLogAnalyzedService(ActionLogAnalyzedService actionLogAnalyzedService) {
        this.actionLogAnalyzedService = actionLogAnalyzedService;
    }

    @Inject
    public void setErrorHandlingService(ErrorHandlingService errorHandlingService) {
        this.errorHandlingService = errorHandlingService;
    }

}
