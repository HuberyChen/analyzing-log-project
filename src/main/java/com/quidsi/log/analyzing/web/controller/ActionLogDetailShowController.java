package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.DataConverter;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.service.TempService;
import com.quidsi.log.analyzing.utils.TimeConvertUtil;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.request.DetailShowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionLogDetailShowController.class);

    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;
    private DataConverter dataConverter;
    private TempService tempService;

    @RequestMapping(value = "/project/instance/log/action/show", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailManagement(@Valid @RequestBody DetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();

        SearchDetailCondition searchDetailCondition = dataConverter.dataConvertToSearchDetailCondition(request);
        StopWatch watch = new StopWatch();

        List<Integer> logIdList = getLogIdList(request, watch);

        tempService.dataStoreIntoTemp(logIdList);
        LOGGER.info("Data store into temp takes " + watch.elapsedTime());
        watch.reset();

        searchDetailCondition.setTotalCount(actionLogDetailService.getTotalCountByCondition(searchDetailCondition));
        LOGGER.info("Getting detail condition total count takes " + watch.elapsedTime());
        watch.reset();

        getDetailsByCondition(map, searchDetailCondition, watch);
        return map;
    }

    private List<Integer> getLogIdList(DetailShowRequest request, StopWatch watch) {
        List<String> dateList = TimeConvertUtil.getDateRange(request.getStartDate(), request.getEndDate());
        List<LogFile> logFiles = new ArrayList<>();
        List<Integer> logIdList = new ArrayList<>();

        if (CollectionUtils.isEmpty(dateList)) {
            return null;
        }

        for (String date : dateList) {
            logFiles.addAll(logFileService.getLogFilesByCondition(date, request.getProject(), request.getServerName()));
        }

        for (LogFile logFile : logFiles) {
            logIdList.add(logFile.getId());
        }
        LOGGER.info("Getting log id list takes " + watch.elapsedTime());
        watch.reset();
        return logIdList;
    }

    private void getDetailsByCondition(Map<String, Object> map, SearchDetailCondition searchDetailCondition, StopWatch watch) {
        int totalCount = searchDetailCondition.getTotalCount();

        int offset = searchDetailCondition.getOffset();

        if (totalCount != 0 && totalCount == offset) {
            offset -= offset - ServiceConstant.DEFAULTFETCHSIZE;
            searchDetailCondition.setOffset(offset);
        }

        List<ActionLogDetail> details = actionLogDetailService.findConditionLimit(searchDetailCondition);
        LOGGER.info("Getting details by condition takes " + watch.elapsedTime());

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
    public void setDataConverter(DataConverter dataConverter) {
        this.dataConverter = dataConverter;
    }

    @Inject
    public void setTempService(TempService tempService) {
        this.tempService = tempService;
    }
}
