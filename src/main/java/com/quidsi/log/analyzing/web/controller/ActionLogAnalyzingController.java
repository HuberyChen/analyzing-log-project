package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.DataValidate;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScanService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LoginRequired
@RequireCookie
@RequireSession
@Controller
public class ActionLogAnalyzingController extends RESTController {

    private LogFileOperation logFileOperation;
    private DataValidate dataValidate;
    private String path;
    private ProjectService projectService;
    private ServerService serverService;
    private ScanService scanService;

    @RequestMapping(value = "/project/project", method = RequestMethod.POST)
    @ResponseBody
    public void scanProject() {
        scanService.scanProject(path);
    }

    @RequestMapping(value = "/server/server", method = RequestMethod.POST)
    @ResponseBody
    public void scanServer() {
        scanService.scanServer(path);
    }

    @RequestMapping(value = "/project/instance", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<Server>> findServerByProject(@Valid @RequestParam String projectName) {
        Project project = projectService.getProjectByName(projectName);
        List<Server> servers = serverService.getServersByProjectId(project.getId());

        Map<String, List<Server>> map = new HashMap<>();
        map.put("servers", servers);
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionLogAnalyzing(@Valid @RequestBody ActionLogAnalyzingRequest request) {

        Map<String, Object> map = new HashMap<>();
        List<LogFileWrapper> logFileWrappers = dataValidate.initializeLogFileWrappers(request, path);

        if (CollectionUtils.isEmpty(logFileWrappers)) {
            map.put("status", "failure");
            return map;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            logFileOperation.saveLogFilesNotExisted(logFileWrapper);
            logFileOperation.decompression(logFileWrapper);
            logFileOperation.saveActionLogDetail(logFileWrapper);
        }
        map.put("status", "success");
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action/schedule", method = RequestMethod.POST)
    @ResponseBody
    public void scheduleActionLogAnalyzing() {
        List<LogFileWrapper> logFileWrappers = dataValidate.initializeScheduleLogFileWrappers(path);
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            logFileOperation.saveLogFilesNotExisted(logFileWrapper);
            logFileOperation.decompression(logFileWrapper);
            logFileOperation.saveActionLogDetail(logFileWrapper);
        }

    }

    @Inject
    public void setLogFileOperation(LogFileOperation logFileOperation) {
        this.logFileOperation = logFileOperation;
    }

    @Inject
    public void setDataConver(DataValidate dataValidate) {
        this.dataValidate = dataValidate;
    }

    @Autowired
    public void setPath(@Value("${portal.path}") String path) {
        this.path = path;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setScanService(ScanService scanService) {
        this.scanService = scanService;
    }

}
