package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.domain.ActionLogSchedule;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.util.Date;

@Service
public class ErrorHandlingService {

    private ScheduleService scheduleService;

    public void errorHandling(String errMsg, ActionLogSchedule schedule) {
        schedule.setStatus(ActionLogSchedule.ScheduleStatus.ERROR);
        schedule.setNote(schedule.getNote() + errMsg);
        schedule.setEffectiveEndTime(new Date());
        scheduleService.update(schedule);
    }

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
