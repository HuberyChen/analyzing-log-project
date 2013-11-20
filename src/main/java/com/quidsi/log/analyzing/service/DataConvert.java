package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;
import com.quidsi.log.analyzing.web.request.ConditionDetailShowRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;

/**
 * @author hubery.chen
 */
@Service
public class DataConvert {

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

    public SearchDetailCondition dataConvertToSearchDetailCondition(ConditionDetailShowRequest request) {
        SearchDetailCondition searchDetailCondition = new SearchDetailCondition();
        searchDetailCondition.setInterfaceName(request.getInterfaceName());
        searchDetailCondition.setErrorCode(request.getErrorCode());
        searchDetailCondition.setStatus(request.getStatus());
        if (!CollectionUtils.isEmpty(request.getLogIdList())) {
            for (int logId : request.getLogIdList()) {
                searchDetailCondition.getLogIdList().add(logId);
            }
        }
        return searchDetailCondition;
    }
}
