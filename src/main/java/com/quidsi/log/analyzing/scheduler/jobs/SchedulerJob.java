package com.quidsi.log.analyzing.scheduler.jobs;

import com.quidsi.core.platform.runtime.RuntimeEnvironment;
import com.quidsi.core.platform.scheduler.Job;
import com.quidsi.log.analyzing.web.LogAnalyzedJobConfig;
import com.quidsi.log.analyzing.web.LogAnalyzedJobStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;

public abstract class SchedulerJob extends Job {
    private final Logger logger = LoggerFactory.getLogger(SchedulerJob.class);
    private LogAnalyzedJobConfig logAnalyzedJobConfig;
    private String env;

    @Override
    protected void execute() throws Throwable {
        if (RuntimeEnvironment.PROD.name().equalsIgnoreCase(env)) {
            this.run();
        } else {
            String jobName = this.getClass().getSimpleName();
            LogAnalyzedJobStatus logAnalyzedJobStatus = logAnalyzedJobConfig.getLogAnalyzedJobStatusConfig().get(jobName);
            logger.info("the env is {} and the job {} status is {}", env, jobName, logAnalyzedJobStatus);
            if (LogAnalyzedJobStatus.RUNABLE.equals(logAnalyzedJobStatus)) {
                this.run();
            }
        }
    }

    protected abstract void run();

    @Inject
    public void setEnv(@Value("${site.environment}") String env) {
        this.env = env;
    }

    @Inject
    public void setLogAnalyzedJobConfig(LogAnalyzedJobConfig logAnalyzedJobConfig) {
        this.logAnalyzedJobConfig = logAnalyzedJobConfig;
    }

}
