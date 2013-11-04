package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;

@LoginRequired
@RequireCookie
@RequireSession
@Controller
public class AnalyzingLogSiteController extends SiteController {

    private ProjectService projectService;
    private ServerService serverService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String analyzingLog(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "home";
    }

    @RequestMapping(value = "/project/details", method = RequestMethod.GET)
    public String projectDetails(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "Project/project";
    }

    @RequestMapping(value = "/server/details", method = RequestMethod.GET)
    public String serverDetails(Map<String, Object> model) {
        List<Server> servers = serverService.getServers();
        model.put("servers", servers);
        return "Server/server";
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

}
