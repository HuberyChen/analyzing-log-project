package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.SearchDetailCondition;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.DataConverter;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.utils.LogReadUtils;
import com.quidsi.log.analyzing.utils.TimeConvertUtil;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
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
    private ProjectService projectService;
    private ServerService serverService;
    private ActionLogDetailService actionLogDetailService;
    private DataConverter dataConverter;

    @RequestMapping(value = "/project/instance/log/action/show", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailManagement(@Valid @RequestBody DetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();

        SearchDetailCondition searchDetailCondition = dataConverter.dataConvertToSearchDetailCondition(request);
        if (request.isChange()) {
            getTotalCount(map, searchDetailCondition, request);
        }

        getDetailsByCondition(map, searchDetailCondition);
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action/showLog", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> logShow(@Valid @RequestParam String path) {
        Map<String, Object> map = new HashMap<>();
        map.put("log", LogReadUtils.logRead(path));
        return map;
    }

    private void getTotalCount(Map<String, Object> map, SearchDetailCondition searchDetailCondition, DetailShowRequest request) {
        List<String> dateList = TimeConvertUtil.getDateRange(request.getStartDate(), request.getEndDate());
        List<LogFile> logFiles = new ArrayList<>();
        searchDetailCondition.getLogIdList().clear();

        if (CollectionUtils.isEmpty(dateList)) {
            map.put("actionLogDetails", null);
            return;
        }

        for (String date : dateList) {
            if (StringUtils.equals(ServiceConstant.TYPE_ALL, request.getServerName())) {
                Project project = projectService.getProjectByName(request.getProject());
                if (null == project) {
                    throw new IllegalStateException("project not exists");
                }
                List<Server> servers = serverService.getServersByProjectId(project.getId());
                if (CollectionUtils.isEmpty(servers)) {
                    throw new IllegalStateException("servers not exist");
                }
                for (Server server : servers) {
                    logFiles.addAll(logFileService.getLogFilesByFuzzyName(date, request.getProject(), server.getServerName()));
                }
            }
            logFiles.addAll(logFileService.getLogFilesByFuzzyName(date, request.getProject(), request.getServerName()));
        }

        for (LogFile logFile : logFiles) {
            searchDetailCondition.getLogIdList().add(logFile.getId());
        }

        searchDetailCondition.setTotalCount(actionLogDetailService.getTotalCount(searchDetailCondition.getLogIdList()));
    }

    private void getDetailsByCondition(Map<String, Object> map, SearchDetailCondition searchDetailCondition) {
        int totalCount = searchDetailCondition.getTotalCount();

        int offset = searchDetailCondition.getOffset();

        if (totalCount != 0 && totalCount == offset) {
            offset -= offset - ServiceConstant.DEFAULTFETCHSIZE;
            searchDetailCondition.setOffset(offset);
        }

        List<ActionLogDetail> details = actionLogDetailService.findConditionLimit(searchDetailCondition);

        map.put("logIdList", searchDetailCondition.getLogIdList());
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
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }
}
