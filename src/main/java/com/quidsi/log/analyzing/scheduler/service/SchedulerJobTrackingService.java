package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.dao.scheduler.SchedulerJobTrackingDao;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobTracking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.Date;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Service
public class SchedulerJobTrackingService {
    private SchedulerJobTrackingDao schedulerJobTrackingDao;

    @Transactional
    public void saveSchedulerJobTracking(String jobName, Date updateOperationTime, ProcessJobResponse response) {
        SchedulerJobTracking schedulerJobTracking = new SchedulerJobTracking();
        schedulerJobTracking.setJobName(jobName);
        schedulerJobTracking.setCreatedTime(updateOperationTime);
        schedulerJobTracking.setStatus(response.getStatus());
        schedulerJobTracking.setMessage(StringUtils.truncate(response.getMessage(), 100));
        schedulerJobTrackingDao.save(schedulerJobTracking);
    }

    @Inject
    public void setSchedulerJobTrackingDao(SchedulerJobTrackingDao schedulerJobTrackingDao) {
        this.schedulerJobTrackingDao = schedulerJobTrackingDao;
    }

}
