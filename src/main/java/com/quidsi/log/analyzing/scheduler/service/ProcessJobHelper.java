package com.quidsi.log.analyzing.scheduler.service;

import com.quidsi.log.analyzing.dao.scheduler.SchedulerJobOperationDao;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobOperation;
import com.quidsi.log.analyzing.utils.SetFlushModeToCommitHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Service
public class ProcessJobHelper {

    private SchedulerJobOperationDao schedulerJobOperationDao;

    private SetFlushModeToCommitHelper setFlushModeToCommitHelper;

    private final Logger logger = LoggerFactory.getLogger(ProcessJobHelper.class);

    @Transactional
    public void processJob(JobService jobService) {
        setFlushModeToCommitHelper.setFlushModeToCommit();
        logger.info("process jobService is {}", jobService.getClass().getSimpleName());
        try {
            SchedulerJobOperation schedulerJobOperation = schedulerJobOperationDao.getSchedulerJobOperationByJobName(jobService.getJobName());
            jobService.processJob(jobService.getUpdateOperationTime(), schedulerJobOperation);
            if (null == schedulerJobOperation) {
                schedulerJobOperation = new SchedulerJobOperation();
                schedulerJobOperation.setJobName(jobService.getJobName());
                schedulerJobOperation.setLastOperationTime(jobService.getUpdateOperationTime());
                schedulerJobOperationDao.save(schedulerJobOperation);
            } else {
                schedulerJobOperation.setLastOperationTime(jobService.getUpdateOperationTime());
                schedulerJobOperationDao.update(schedulerJobOperation);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Inject
    public void setSchedulerJobOperationDao(SchedulerJobOperationDao schedulerJobOperationDao) {
        this.schedulerJobOperationDao = schedulerJobOperationDao;
    }

    @Inject
    public void setSetFlushModeToCommitHelper(SetFlushModeToCommitHelper setFlushModeToCommitHelper) {
        this.setFlushModeToCommitHelper = setFlushModeToCommitHelper;
    }

}
