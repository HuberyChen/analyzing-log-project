package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScheduleService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.response.InstanceDetail;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@LoginRequired
@RequireCookie
@RequireSession
@Controller
public class AnalyzingLogSiteController extends SiteController {

    private ProjectService projectService;
    private ServerService serverService;
    private ScheduleService scheduleService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String analyzingLog(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "home";
    }

    @RequestMapping(value = "/schedule/details", method = RequestMethod.GET)
    public String scheduletDetails(Map<String, Object> model) {

        List<ActionLogSchedule> schedules = scheduleService.getSchedulesRunning();
        model.put("schedules", schedules);
        return "schedule/schedule";
    }

    @RequestMapping(value = "/project/instance/details", method = RequestMethod.GET)
    public String instanceDetails(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            model.put("instanceDetails", null);
            return "project/project";
        }
        List<InstanceDetail> instanceDetails = new ArrayList<>();
        for (Project project : projects) {
            InstanceDetail instanceDetail = new InstanceDetail();
            instanceDetail.setProjectName(project.getName());
            instanceDetail.setProjectId(project.getId());

            List<Server> servers = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(servers)) {
                instanceDetail.getServers().add("null");
            } else {
                for (Server server : servers) {
                    instanceDetail.getServers().add(server.getServerName());
                }
            }
            instanceDetails.add(instanceDetail);
        }
        model.put("instanceDetails", instanceDetails);
        return "project/project";
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
