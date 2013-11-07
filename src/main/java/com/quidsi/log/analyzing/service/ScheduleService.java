package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.ScheduleDao;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

@Service
public class ScheduleService {

    private ScheduleDao scheduleDao;

    @Transactional
    public int save(ActionLogSchedule schedule) {
        return scheduleDao.save(schedule);
    }

    @Transactional
    public void update(ActionLogSchedule schedule) {
        scheduleDao.update(schedule);
    }

    public List<ActionLogSchedule> getSchedulesRunning() {
        return scheduleDao.getSchedulesRunning();
    }

    public ActionLogSchedule getScheduleRunning(String project, String instance) {
        return scheduleDao.getScheduleRunning(project, instance);
    }

    public List<ActionLogSchedule> getSchedules() {
        return scheduleDao.getSchedules();
    }

    @Inject
    public void setScheduleDao(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }
}
