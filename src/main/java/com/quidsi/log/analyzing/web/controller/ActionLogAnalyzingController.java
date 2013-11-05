package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Schedule;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.DataValidate;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScanService;
import com.quidsi.log.analyzing.service.ScheduleService;
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

import java.util.Date;
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
    private ScheduleService scheduleService;

    @RequestMapping(value = "/project/instance/schedule", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> generateSchedule(@Valid @RequestBody ActionLogAnalyzingRequest request) {
        Map<String, Object> map = new HashMap<>();
        int id = scheduleService.save(dataConverToSchedule(request));
        if (0 == id) {
            map.put("status", "failure");
            return map;
        }
        map.put("status", "success");
        return map;
    }

    private void actionLogAnalyzing(Schedule schedule) {

        List<LogFileWrapper> logFileWrappers = dataValidate.initializeLogFileWrappers(schedule, path);

        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            logFileOperation.saveLogFilesNotExisted(logFileWrapper);
            logFileOperation.decompression(logFileWrapper);
            logFileOperation.saveActionLogDetail(logFileWrapper);
        }
    }

    private Schedule dataConverToSchedule(ActionLogAnalyzingRequest request) {
        Schedule schedule = new Schedule();
        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());
        schedule.setProject(request.getProjectName());
        schedule.setInstance(request.getServerName());
        schedule.setEffectiveStartTime(new Date());
        schedule.setStatus(Schedule.ScheduleStatus.WAIT);
        return schedule;
    }

    @RequestMapping(value = "/project/project", method = RequestMethod.POST)
    @ResponseBody
    public void scanProject() {
        scanService.scanProject(path);
    }

    @RequestMapping(value = "/instance/server", method = RequestMethod.POST)
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

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
