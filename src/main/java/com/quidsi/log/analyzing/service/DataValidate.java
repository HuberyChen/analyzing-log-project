package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.web.request.ActionLogAnalyzingRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DataValidate {

    private final Logger logger = LoggerFactory.getLogger(DataValidate.class);

    private ProjectService projectService;
    private ServerService serverService;

    public List<LogFileWrapper> initializeScheduleLogFileWrappers(String path) {
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        return getLogFileWrapperWithProjectAndServerAll(dateConverToString(new Date()), dateConverToString(new Date()), path, logFileWrappers);
    }

    private String dateConverToString(Date date) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(DateUtils.getMonth(date))).append("/").append(String.valueOf(DateUtils.getDay(date) - 1)).append("/").append(String.valueOf(DateUtils.getYear(date)));
        return builder.toString();
    }

    public List<LogFileWrapper> initializeLogFileWrappers(ActionLogAnalyzingRequest request, String path) {

        List<LogFileWrapper> logFileWrappers = new ArrayList<>();

        String projectName = request.getProjectName();
        String serverName = request.getServerName();
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();

        if (!projectName.equals(ServiceConstant.TYPE_ALL) && serverName.equals(ServiceConstant.TYPE_ALL)) {
            return getLogFileWrapperWithServerAll(startDate, endDate, path, projectName, logFileWrappers);
        }

        if (projectName.equals(ServiceConstant.TYPE_ALL) && serverName.equals(ServiceConstant.TYPE_ALL)) {
            return getLogFileWrapperWithProjectAndServerAll(startDate, endDate, path, logFileWrappers);
        }

        Project project = projectService.getProjectByName(projectName);

        if (null == project) {
            throw new IllegalStateException("Project is not existed");
        }
        Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);
        if (null == server) {
            throw new IllegalStateException(String.format("%s does not have instance", project.getName()));
        }
        logFileWrappers.add(new LogFileWrapper(project, server, startDate, endDate, path));
        return logFileWrappers;

    }

    private List<LogFileWrapper> getLogFileWrapperWithProjectAndServerAll(String startDate, String endDate, String path, List<LogFileWrapper> logFileWrappers) {
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            throw new IllegalStateException("Projects are not existed");
        }
        for (Project project : projects) {
            List<Server> servers = serverService.getServersByProjectId(project.getId());
            if (CollectionUtils.isEmpty(servers)) {
                logger.error("%s does not have instance", project.getName());
                continue;
            }
            for (Server server : servers) {
                logFileWrappers.add(new LogFileWrapper(project, server, startDate, endDate, path));
            }
        }

        if (CollectionUtils.isEmpty(logFileWrappers)) {
            throw new IllegalStateException("Servers are not existed");
        }
        return logFileWrappers;
    }

    private List<LogFileWrapper> getLogFileWrapperWithServerAll(String startDate, String endDate, String path, String projectName, List<LogFileWrapper> logFileWrappers) {
        Project project = projectService.getProjectByName(projectName);
        if (null == project) {
            throw new IllegalStateException("Project is not existed");
        }
        List<Server> servers = serverService.getServersByProjectId(project.getId());
        if (CollectionUtils.isEmpty(servers)) {
            throw new IllegalStateException(String.format("%s does not have instance", projectName));
        }
        for (Server server : servers) {
            logFileWrappers.add(new LogFileWrapper(project, server, startDate, endDate, path));
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
