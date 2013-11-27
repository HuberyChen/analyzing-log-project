package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;
import com.quidsi.log.analyzing.web.request.DetailShowRequest;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author hubery.chen
 */
@Service
public class DataConverter {

    public ActionLogSchedule dataConvertToSchedule(ActionLogAnalyzingRequest request) {
        ActionLogSchedule actionLogSchedule = new ActionLogSchedule();
        actionLogSchedule.setStartDate(request.getStartDate());
        actionLogSchedule.setEndDate(request.getEndDate());
        actionLogSchedule.setProject(request.getProjectName());
        actionLogSchedule.setInstance(request.getServerName());
        actionLogSchedule.setEffectiveStartTime(new Date());
        actionLogSchedule.setStatus(ActionLogSchedule.ScheduleStatus.RUNNING);
        actionLogSchedule.setNote("start schedule. ");
        return actionLogSchedule;
    }

    public SearchDetailCondition dataConvertToSearchDetailCondition(DetailShowRequest request) {
        SearchDetailCondition searchDetailCondition = new SearchDetailCondition();
        searchDetailCondition.setInterfaceName(request.getInterfaceName());
        searchDetailCondition.setErrorCode(request.getErrorCode());
        searchDetailCondition.setStatus(request.getStatus());
        searchDetailCondition.setOffset(request.getOffset());
        return searchDetailCondition;
    }
}
