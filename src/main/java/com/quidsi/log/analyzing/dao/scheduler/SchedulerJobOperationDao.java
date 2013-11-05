package com.quidsi.log.analyzing.dao.scheduler;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobOperation;

import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Repository
public class SchedulerJobOperationDao {

    private JPAAccess jpaAccess;

    public void save(SchedulerJobOperation schedulerJobOperation) {
        jpaAccess.save(schedulerJobOperation);
    }

    public void update(SchedulerJobOperation schedulerJobOperation) {
        jpaAccess.update(schedulerJobOperation);
    }

    public SchedulerJobOperation getSchedulerJobOperationByJobName(String jobName) {
        return jpaAccess.get(SchedulerJobOperation.class, jobName);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

}
