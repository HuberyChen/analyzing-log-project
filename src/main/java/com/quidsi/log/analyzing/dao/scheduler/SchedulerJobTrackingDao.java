package com.quidsi.log.analyzing.dao.scheduler;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobTracking;

import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * 
 * @author Lumine.Wu
 * 
 */
@Repository
public class SchedulerJobTrackingDao {

    private JPAAccess jpaAccess;

    public void save(SchedulerJobTracking schedulerJobTracking) {
        jpaAccess.save(schedulerJobTracking);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

}
