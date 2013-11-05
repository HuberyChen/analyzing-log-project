package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.ScheduleDao;
import com.quidsi.log.analyzing.domain.Schedule;

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

	public List<Schedule> getSchedulesWait() {
		return scheduleDao.getSchedulesWait();
	}

	public List<Schedule> getSchedules() {
		return scheduleDao.getSchedules();
	}

	@Inject
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
}
