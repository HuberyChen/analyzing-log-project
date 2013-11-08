package com.quidsi.log.analyzing.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.web.response.InformationResponse;
import com.quidsi.log.analyzing.web.response.ProjectInfomation;
import com.quidsi.log.analyzing.web.response.ServerInformation;

@Controller
public class AnalyzingLogInformationController {

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
            ProjectInfomation projectInfomation = new ProjectInfomation();
            projectInfomation.setProject(project.getName());

            List<Server> serverList = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(serverList)) {
                projectInfomation.getServerList().add(null);
                continue;
            }
            for (Server server : serverList) {
                ServerInformation serverInformation = new ServerInformation();
                serverInformation.setServer(server.getServerName());
                projectInfomation.getServerList().add(serverInformation);
            }
            response.getProjects().add(projectInfomation);
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
