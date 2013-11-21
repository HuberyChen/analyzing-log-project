package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.DataConvert;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.utils.TimeConvertUtil;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.request.ConditionDetailShowRequest;
import com.quidsi.log.analyzing.web.request.DetailShowRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hubery.chen
 */
@LoginRequired
@RequireCookie
@RequireSession
@Controller
public class ActionLogDetailShowController extends RESTController {

    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;
    private DataConvert dataConvert;

    @RequestMapping(value = "/project/instance/log/action/search/change", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailConditionPageManagement(@Valid @RequestBody ConditionDetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();
        SearchDetailCondition searchDetailCondition = dataConvert.dataConvertToSearchDetailCondition(request);
        searchDetailCondition.setOffset(request.getOffset());
        searchDetailCondition.setTotalCount(request.getTotalCount());
        getDetailsByCondition(map, searchDetailCondition);
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action/search/show", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailConditionManagement(@Valid @RequestBody ConditionDetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();
        SearchDetailCondition searchDetailCondition = dataConvert.dataConvertToSearchDetailCondition(request);
        int totalCount = actionLogDetailService.getTotalCountByCondition(searchDetailCondition);

        searchDetailCondition.setTotalCount(totalCount);
        searchDetailCondition.setOffset(0);
        getDetailsByCondition(map, searchDetailCondition);
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action/change", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailPageManagement(@Valid @RequestParam List<Integer> logIdList, @Valid @RequestParam int offset, @Valid @RequestParam int totalCount) {
        Map<String, Object> map = new HashMap<>();
        getDetailsByLogIdList(logIdList, map, offset, totalCount);
        return map;
    }


    @RequestMapping(value = "/project/instance/log/action/show", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailManagement(@Valid @RequestBody DetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<String> dateList = TimeConvertUtil.getDateRange(request.getStartDate(), request.getEndDate());
        List<LogFile> logFiles = new ArrayList<>();

        if (CollectionUtils.isEmpty(dateList)) {
            map.put("actionLogDetails", null);
            return map;
        }

        for (String date : dateList) {
            logFiles.addAll(logFileService.getLogFilesByFuzzyName(date, request.getProject(), request.getServerName()));
        }

        List<Integer> logIdList = new ArrayList<>();
        for (LogFile logFile : logFiles) {
            logIdList.add(logFile.getId());
        }

        int totalCount = actionLogDetailService.getTotalCount(logIdList);
        getDetailsByLogIdList(logIdList, map, request.getOffset(), totalCount);
        return map;
    }

    private void getDetailsByCondition(Map<String, Object> map, SearchDetailCondition searchDetailCondition) {
        int totalCount = searchDetailCondition.getTotalCount();

        int offset = searchDetailCondition.getOffset();

        if (totalCount != 0 && totalCount == offset) {
            offset -= offset - ServiceConstant.DEFAULTFETCHSIZE;
            searchDetailCondition.setOffset(offset);
        }

        List<ActionLogDetail> details = actionLogDetailService.findConditionLimit(searchDetailCondition);

        putMessageToMap(map, searchDetailCondition.getLogIdList(), details, searchDetailCondition.getOffset(), searchDetailCondition.getTotalCount());
    }

    private void getDetailsByLogIdList(List<Integer> logIdList, Map<String, Object> map, int initOffset, int totalCount) {

        int offset = initOffset;

        if (totalCount != 0 && totalCount == offset) {
            offset -= offset - ServiceConstant.DEFAULTFETCHSIZE;
        }

        List<ActionLogDetail> details = actionLogDetailService.findDetail(logIdList, offset);

        putMessageToMap(map, logIdList, details, offset, totalCount);
    }

    private void putMessageToMap(Map<String, Object> map, List<Integer> logIdList, List<ActionLogDetail> details, int offset, int totalCount) {
        map.put("logIdList", logIdList);
        map.put("actionLogDetails", details);
        map.put("fetchSize", ServiceConstant.DEFAULTFETCHSIZE);
        map.put("offset", offset);
        map.put("totalCount", totalCount);
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

    @Inject
    public void setDataConvert(DataConvert dataConvert) {
        this.dataConvert = dataConvert;
    }
}
