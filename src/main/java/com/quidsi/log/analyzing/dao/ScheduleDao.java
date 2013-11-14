package com.quidsi.log.analyzing.dao;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScheduleDao {

    private JPAAccess jpaAccess;

    public int save(ActionLogSchedule schedule) {
        jpaAccess.save(schedule);
        return schedule.getId();
    }

    public void update(ActionLogSchedule schedule) {
        jpaAccess.update(schedule);
    }

    public List<ActionLogSchedule> getSchedulesRunning() {
        Map<String, Object> param = new HashMap<>();
        param.put("status", ActionLogSchedule.ScheduleStatus.RUNNING);
        return jpaAccess.find("from " + ActionLogSchedule.class.getName() + " where status = :status", param);
    }

    public ActionLogSchedule getScheduleRunning(String project, String instance) {
        Map<String, Object> param = new HashMap<>();
        param.put("project", project);
        param.put("instance", instance);
        param.put("status", ActionLogSchedule.ScheduleStatus.RUNNING);
        return jpaAccess.findUniqueResult("from " + ActionLogSchedule.class.getName() + " where status = :status and project = :project and instance = :instance", param);
    }

    public List<ActionLogSchedule> getSchedulesIntraday() {
        Map<String, Object> param = new HashMap<>();
        param.put("now", getTimesMorning());
        return jpaAccess.find("from " + ActionLogSchedule.class.getName() + " where EffectiveStartTime > :now order by EffectiveStartTime desc ", param);
    }

    public ActionLogSchedule getScheduleById(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return jpaAccess.findUniqueResult("from " + ActionLogSchedule.class.getName() + " where id = :id", param);
    }

    private Date getTimesMorning() {
        Date today = new Date();
        return DateUtils.date(DateUtils.getYear(today), DateUtils.getMonth(today), DateUtils.getDay(today));
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
