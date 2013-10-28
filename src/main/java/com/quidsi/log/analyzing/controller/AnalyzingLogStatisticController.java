package com.quidsi.log.analyzing.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.response.InformationResponse;
import com.quidsi.log.analyzing.response.ProjectStatistic;
import com.quidsi.log.analyzing.response.ServerStatistic;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;

@Controller
public class AnalyzingLogStatisticController {

    private ProjectService projectService;

    private ServerService serverService;

    @RequestMapping(value = "/information/analyzing-log", method = RequestMethod.GET)
    @ResponseBody
    public InformationResponse statistic() {
        InformationResponse response = new InformationResponse();
        List<Project> projectList = projectService.getProjects();
        if (CollectionUtils.isEmpty(projectList)) {
            return response;
        }
        for (Project project : projectList) {
            ProjectStatistic projectStatistic = new ProjectStatistic();
            projectStatistic.setProject(project.getName());

            List<Server> serverList = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(serverList)) {
                projectStatistic.getServerList().add(null);
                continue;
            }
            for (Server server : serverList) {
                ServerStatistic serverStatistic = new ServerStatistic();
                serverStatistic.setServer(server.getServerName());
                projectStatistic.getServerList().add(serverStatistic);
            }
            response.getProjects().add(projectStatistic);
        }
        return response;
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
