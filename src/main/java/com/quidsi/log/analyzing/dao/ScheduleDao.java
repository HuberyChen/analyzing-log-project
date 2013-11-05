package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.Schedule;

import org.springframework.stereotype.Repository;

import javax.inject.Inject;

import java.util.List;

@Repository
public class ScheduleDao {

    private JPAAccess jpaAccess;

    public int save(Schedule schedule) {
        jpaAccess.save(schedule);
        return schedule.getId();
    }

    public void update(Schedule schedule) {
        jpaAccess.update(schedule);
    }

    public List<Schedule> getSchedules() {
        return jpaAccess.find("from " + Schedule.class.getName(), null);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
