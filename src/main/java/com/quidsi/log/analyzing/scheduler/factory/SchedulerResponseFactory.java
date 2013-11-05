package com.quidsi.log.analyzing.scheduler.factory;

import com.quidsi.log.analyzing.domain.scheduler.SchedulerJobStatus;
import com.quidsi.log.analyzing.scheduler.service.ProcessJobResponse;

import org.springframework.stereotype.Component;

@Component
public class SchedulerResponseFactory {

    public ProcessJobResponse build(String message, SchedulerJobStatus status) {
        ProcessJobResponse response = new ProcessJobResponse();
        response.setMessage(message);
        response.setStatus(status);
        return response;
    }
}