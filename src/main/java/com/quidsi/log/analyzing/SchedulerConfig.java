package com.quidsi.log.analyzing;

import com.quidsi.core.platform.DefaultSchedulerConfig;
import com.quidsi.core.platform.JobRegistry;
import com.quidsi.log.analyzing.scheduler.jobs.ActionLogAnalyzedAllJob;
import com.quidsi.log.analyzing.scheduler.service.SchedulerSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

/**
 * @author neo
 */
@Configuration
public class SchedulerConfig extends DefaultSchedulerConfig {
    private final Logger logger = LoggerFactory.getLogger(SchedulerConfig.class);
    @Inject
    Environment env;

    @Inject
    SchedulerSettings schedulerSettings;

    @Override
    protected void configure(JobRegistry registry) {
        boolean isMasterScheduler = env.getProperty("logAnalyzed.isMasterScheduler", boolean.class, false);
        if (isMasterScheduler) {
            logger.info("start master scheduler");
            registry.triggerByCronExpression(ActionLogAnalyzedAllJob.class.getSimpleName(), ActionLogAnalyzedAllJob.class, schedulerSettings.getLogAnalyzedCron());
        }
    }
}
