package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.scheduler.Scheduler;
import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.scheduler.jobs.ActionLogAnalyzedOnceJob;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScanService;
import com.quidsi.log.analyzing.service.ScheduleService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

    private String path;
    private ProjectService projectService;
    private ServerService serverService;
    private ScanService scanService;
    private ScheduleService scheduleService;
    private Scheduler scheduler;

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionLogAnalyzed(@Valid @RequestBody ActionLogAnalyzingRequest request) {
        Map<String, Object> map = new HashMap<>();
        ActionLogSchedule actionLogSchedule = dataConverToSchedule(request);

        if (null != scheduleService.getScheduleRunning(actionLogSchedule.getProject(), actionLogSchedule.getInstance())
                || null != scheduleService.getScheduleRunning(actionLogSchedule.getProject(), ServiceConstant.TYPE_ALL)
                || null != scheduleService.getScheduleRunning(ServiceConstant.TYPE_ALL, ServiceConstant.TYPE_ALL)) {
            map.put("errMsg", "job is running, please wait");
            return map;
        }

        if (0 == scheduleService.save(actionLogSchedule)) {
            map.put("errMsg", "job saves failure");
            return map;
        }

        ActionLogAnalyzedOnceJob job = new ActionLogAnalyzedOnceJob();
        job.setActionLogSchedule(actionLogSchedule);
        scheduler.triggerOnce(job);
        map.put("errMsg", "job is ready running");
        return map;
    }

    @RequestMapping(value = "/project/project", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> scanProject() {
        Map<String, Object> map = new HashMap<>();
        List<String> projectsNotExisted = scanService.scanProject(path);
        map.put("projectsNotExisted", projectsNotExisted);
        return map;
    }

    @RequestMapping(value = "/project/generate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> generateProject(@Valid @RequestParam String projectName) {
        Map<String, Object> map = new HashMap<>();
        Project project = new Project();
        project.setName(projectName);
        if (0 == projectService.save(project)) {
            map.put("status", "failure");
            return map;
        }
        map.put("status", "success");
        return map;
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

    private ActionLogSchedule dataConverToSchedule(ActionLogAnalyzingRequest request) {
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

    @Inject
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
