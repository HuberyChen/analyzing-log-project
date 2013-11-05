package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.Schedule;

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

	public List<Schedule> getSchedulesWait() {
		Map<String, Object> param = new HashMap<>();
		param.put("status", Schedule.ScheduleStatus.WAIT);
		return jpaAccess.find("from " + Schedule.class.getName()
				+ " where status = :status", param);
	}

	public List<Schedule> getSchedules() {
		return jpaAccess.find("from " + Schedule.class.getName(), null);
	}

	@Inject
	public void setJpaAccess(JPAAccess jpaAccess) {
		this.jpaAccess = jpaAccess;
	}
}
