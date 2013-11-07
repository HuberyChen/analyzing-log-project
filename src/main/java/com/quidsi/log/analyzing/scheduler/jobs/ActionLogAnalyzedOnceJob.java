package com.quidsi.log.analyzing.scheduler.jobs;

import com.quidsi.core.platform.scheduler.Job;
import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.scheduler.service.ActionLogAnalyzedService;
import com.quidsi.log.analyzing.service.DataValidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;

import java.util.List;

public class ActionLogAnalyzedOnceJob extends Job {
    private final Logger logger = LoggerFactory.getLogger(ActionLogAnalyzedOnceJob.class);

    private DataValidate dataValidate;
    private String path;
    ActionLogSchedule actionLogSchedule;
    private ActionLogAnalyzedService actionLogAnalyzedService;

    @Override
    protected void execute() throws Throwable {

        StopWatch stopWatch = new StopWatch();

        List<LogFileWrapper> logFileWrappers = dataValidate.initializeLogFileWrappers(actionLogSchedule, path);

        actionLogAnalyzedService.actionLogAnalyzed(actionLogSchedule, logFileWrappers);

        logger.info("analyzing action log use time:{}", stopWatch.elapsedTime());
    }

    public ActionLogSchedule getActionLogSchedule() {
        return actionLogSchedule;
    }

    public void setActionLogSchedule(ActionLogSchedule actionLogSchedule) {
        this.actionLogSchedule = actionLogSchedule;
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
    public void setActionLogAnalyzedService(ActionLogAnalyzedService actionLogAnalyzedService) {
        this.actionLogAnalyzedService = actionLogAnalyzedService;
    }

}
