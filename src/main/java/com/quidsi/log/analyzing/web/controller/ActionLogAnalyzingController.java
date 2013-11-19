package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.scheduler.Scheduler;
import com.quidsi.core.platform.web.rest.RESTController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.scheduler.jobs.ActionLogAnalyzedOnceJob;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScanService;
import com.quidsi.log.analyzing.service.ScheduleService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.utils.TimeConvertUtil;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;
import com.quidsi.log.analyzing.web.request.DetailShowRequest;
import com.quidsi.log.analyzing.web.response.InstanceDetail;
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
import java.util.ArrayList;
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
    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;

    @RequestMapping(value = "/project/instance/log/action/fuzzy", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailFuzzySearch(@Valid @RequestParam List<Integer> logIdList, @Valid @RequestParam String fuzzyMessage) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action/change", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailPageManagement(@Valid @RequestParam List<Integer> logIdList, @Valid @RequestParam int offset) {
        Map<String, Object> map = new HashMap<>();
        getDetailsByLogIdList(logIdList, map, offset);
        return map;
    }


    @RequestMapping(value = "/project/instance/log/action/show", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionDetailManagement(@Valid @RequestBody DetailShowRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<LogFile> logFiles = logFileService.getLogFilesByFuzzyName(TimeConvertUtil.formatDate(TimeConvertUtil.stringConvertToDate(request.getDate())), request.getProject(), request.getServerName());

        if (CollectionUtils.isEmpty(logFiles)) {
            map.put("actionLogDetails", null);
            return map;
        }

        List<Integer> logIdList = new ArrayList<>();
        for (LogFile logFile : logFiles) {
            logIdList.add(logFile.getId());
        }
        getDetailsByLogIdList(logIdList, map, request.getOffset());
        return map;
    }

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> actionLogAnalyzed(@Valid @RequestBody ActionLogAnalyzingRequest request) {
        Map<String, Object> map = new HashMap<>();
        ActionLogSchedule actionLogSchedule = dataConvertToSchedule(request);

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

    @RequestMapping(value = "/project/instance", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, List<Server>> findServerByProject(@Valid @RequestParam String projectName) {
        Project project = projectService.getProjectByName(projectName);
        List<Server> servers = serverService.getServersByProjectId(project.getId());

        Map<String, List<Server>> map = new HashMap<>();
        map.put("servers", servers);
        return map;
    }

    @RequestMapping(value = "/scan", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> scanProjectAndServer() {
        Map<String, Object> map = new HashMap<>();
        List<String> projectsAll = scanService.scanProject(path);
        List<InstanceDetail> detailNotExisted = new ArrayList<>();
        if (CollectionUtils.isEmpty(projectsAll)) {
            map.put("errMsg", "There is not project.");
            map.put("detailNotExisted", detailNotExisted);
            return map;
        }
        for (String projectName : projectsAll) {
            InstanceDetail instanceDetail = new InstanceDetail();
            Project project = projectService.getProjectByName(projectName);
            if (null == project) {
                projectNullCondition(projectName, instanceDetail, detailNotExisted);
                continue;
            }
            projectNotNullCondition(project, instanceDetail, detailNotExisted);
        }
        map.put("errMsg", "Scan success.");
        map.put("detailNotExisted", detailNotExisted);
        return map;
    }

    @RequestMapping(value = "/generation", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> generate(@Valid @RequestParam String projectName, @Valid @RequestParam String serverName) {
        Map<String, Object> map = new HashMap<>();
        Project project = projectService.getProjectByName(projectName);

        if (null == project) {
            int projectId = projectService.save(generateProject(projectName));
            serverService.save(generateServer(serverName, projectId));
            map.put("status", "SUCCESS");
            return map;
        }

        Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);

        if (null != server) {
            map.put("status", "FAILURE");
            return map;
        }

        serverService.save(generateServer(serverName, project.getId()));

        map.put("status", "SUCCESS");
        return map;
    }

    private void getDetailsByLogIdList(List<Integer> logIdList, Map<String, Object> map, int initOffset) {

        int totalCount = actionLogDetailService.getTotalCount(logIdList);
        int offset = initOffset;

        if (totalCount != 0 && totalCount == offset) {
            offset -= offset - ServiceConstant.DEFAULTFETCHSIZE;
        }

        List<ActionLogDetail> details = actionLogDetailService.findDetail(logIdList, offset);

        map.put("logIdList", logIdList);
        map.put("actionLogDetails", details);
        map.put("fetchSize", ServiceConstant.DEFAULTFETCHSIZE);
        map.put("offset", offset);
        map.put("maxPageNum", totalCount / ServiceConstant.DEFAULTFETCHSIZE);
    }

    private void projectNotNullCondition(Project project, InstanceDetail instanceDetail, List<InstanceDetail> detailNotExisted) {
        instanceDetail.setProject(project);
        List<Server> servers = scanServer(project);
        if (!CollectionUtils.isEmpty(servers)) {
            instanceDetail.getServers().addAll(servers);
            detailNotExisted.add(instanceDetail);
        }
    }

    private void projectNullCondition(String projectName, InstanceDetail instanceDetail, List<InstanceDetail> detailNotExisted) {
        Project project = generateProject(projectName);
        instanceDetail.setProject(project);
        List<Server> servers = scanServer(project);
        if (!CollectionUtils.isEmpty(servers)) {
            instanceDetail.getServers().addAll(servers);
        }
        detailNotExisted.add(instanceDetail);
    }

    private List<Server> scanServer(Project project) {
        List<String> serversAll = scanService.scanServer(path, project.getName());
        List<Server> serversNotExisted = new ArrayList<>();
        if (CollectionUtils.isEmpty(serversAll)) {
            return null;
        }
        for (String serverName : serversAll) {
            Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);
            if (null != server) {
                continue;
            }
            serversNotExisted.add(generateServer(serverName, project.getId()));
        }
        return serversNotExisted;
    }

    private Server generateServer(String serverName, int projectId) {
        Server server = new Server();
        server.setProjectId(projectId);
        server.setServerName(serverName);
        return server;
    }

    private Project generateProject(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        return project;
    }

    private ActionLogSchedule dataConvertToSchedule(ActionLogAnalyzingRequest request) {
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

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

}
