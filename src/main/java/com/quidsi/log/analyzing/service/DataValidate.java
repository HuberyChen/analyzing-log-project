package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.request.ActionLogAnalyzingRequest;

@Service
public class DataValidate {

    private final Logger logger = LoggerFactory.getLogger(DataValidate.class);

    private ProjectService projectService;
    private ServerService serverService;

    private List<LogFileWrapper> logFileWrappers = new ArrayList<>();

    public List<LogFileWrapper> initializeLogFileWrappers(ActionLogAnalyzingRequest request, String path) {

        String projectName = request.getProjectName();
        String serverName = request.getServerName();
        String date = request.getDate();

        if (!projectName.equals("All") && serverName.equals("All")) {
            return getLogFileWrapperWithServerAll(date, path, projectName);
        }

        if (projectName.equals("All") && serverName.equals("All")) {
            return getLogFileWrapperWithProjectAndServerAll(date, path);
        }

        Project project = projectService.getProjectByName(projectName);

        if (null == project) {
            throw new IllegalStateException("Project is not existed");
        }
        Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);
        if (null == server) {
            throw new IllegalStateException(String.format("%d does not have instance", project.getName()));
        }
        logFileWrappers.add(new LogFileWrapper(project, server, date, path));
        return logFileWrappers;

    }

    private List<LogFileWrapper> getLogFileWrapperWithProjectAndServerAll(String date, String path) {
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            throw new IllegalStateException("Projects are not existed");
        }
        for (Project project : projects) {
            List<Server> servers = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(servers)) {
                logger.error("%d does not have instance", project.getName());
                continue;
            }
            for (Server server : servers) {
                logFileWrappers.add(new LogFileWrapper(project, server, date, path));
            }
        }

        if (CollectionUtils.isEmpty(logFileWrappers)) {
            throw new IllegalStateException("Servers are not existed");
        }
        return logFileWrappers;
    }

    private List<LogFileWrapper> getLogFileWrapperWithServerAll(String date, String path, String projectName) {
        Project project = projectService.getProjectByName(projectName);
        if (null == project) {
            throw new IllegalStateException("Project is not existed");
        }
        List<Server> servers = serverService.getServersByProjectId(project.getId());
        if (CollectionUtils.isEmpty(servers)) {
            throw new IllegalStateException(String.format("%d does not have instance", projectName));
        }
        for (Server server : servers) {
            logFileWrappers.add(new LogFileWrapper(project, server, date, path));
        }
        return logFileWrappers;

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
