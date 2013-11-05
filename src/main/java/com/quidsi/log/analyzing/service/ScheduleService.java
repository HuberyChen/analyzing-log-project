package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.dao.ScheduleDao;
import com.quidsi.log.analyzing.domain.Schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

@Service
public class ScheduleService {

    private ScheduleDao scheduleDao;

    @Transactional
    public int save(Schedule schedule) {
        return scheduleDao.save(schedule);
    }

    @Transactional
    public void update(Schedule schedule) {
        scheduleDao.update(schedule);
    }

    public List<Schedule> getSchedules() {
        return scheduleDao.getSchedules();
    }

    @Inject
    public void setScheduleDao(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }
}
