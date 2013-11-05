package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 
 * @author Lumine.Wu
 * 
 */

@Service
public class ActionLogAnalyzedService extends JobService {
    private final Logger logger = LoggerFactory.getLogger(ActionLogAnalyzedService.class);

    @Override
    protected void processJob(Date currentTime, SchedulerJobOperation schedulerJobOperation) {
        StopWatch stopWatch = new StopWatch();
        logger.info("analyzing action log use time:{}", stopWatch.elapsedTime());
    }

}
