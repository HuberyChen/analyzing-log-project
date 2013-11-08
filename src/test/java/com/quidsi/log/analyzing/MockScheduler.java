package com.quidsi.log.analyzing;

import com.quidsi.core.platform.scheduler.Job;
import com.quidsi.core.platform.scheduler.SchedulerImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author neo
 */
public class MockScheduler extends SchedulerImpl {
    private final List<Job> registeredJobs = new ArrayList<>();

    @Override
    public void triggerOnceAt(Job job, Date time) {
        registeredJobs.add(job);
    }

    public List<Job> getRegisteredJobs() {
        return registeredJobs;
    }
}
