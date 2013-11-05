package com.quidsi.log.analyzing.scheduler.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Schedule;
import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobOperation;
import com.quidsi.log.analyzing.service.DataValidate;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.ScheduleService;

@Service
public class ActionLogAnalyzedService extends JobService {
	private final Logger logger = LoggerFactory
			.getLogger(ActionLogAnalyzedService.class);

	private ScheduleService scheduleService;
	private LogFileOperation logFileOperation;
	private DataValidate dataValidate;
	private String path;

	@Override
	@Transactional
	protected void processJob(Date currentTime,
			SchedulerJobOperation schedulerJobOperation) {
		StopWatch stopWatch = new StopWatch();

		List<Schedule> schedules = scheduleService.getSchedulesWait();

		if (CollectionUtils.isEmpty(schedules)) {
			return;
		}
		for (Schedule schedule : schedules) {
			schedule.setStatus(Schedule.ScheduleStatus.RUNNING);
			scheduleService.update(schedule);
			List<LogFileWrapper> logFileWrappers = dataValidate
					.initializeLogFileWrappers(schedule, path);

			if (CollectionUtils.isEmpty(logFileWrappers)) {
				return;
			}

			for (LogFileWrapper logFileWrapper : logFileWrappers) {
				logFileOperation.saveLogFilesNotExisted(logFileWrapper);
				logFileOperation.decompression(logFileWrapper);
				logFileOperation.saveActionLogDetail(logFileWrapper);
			}
			schedule.setEffectiveEndTime(new Date());
			schedule.setStatus(Schedule.ScheduleStatus.SUCCESS);
			scheduleService.update(schedule);
		}
		logger.info("analyzing action log use time:{}", stopWatch.elapsedTime());
	}

	@Inject
	public void setScheduleService(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	@Inject
	public void setLogFileOperation(LogFileOperation logFileOperation) {
		this.logFileOperation = logFileOperation;
	}

	@Inject
	public void setDataValidate(DataValidate dataValidate) {
		this.dataValidate = dataValidate;
	}

	@Inject
	public void setPath(@Value("${portal.path}") String path) {
		this.path = path;
	}

}
