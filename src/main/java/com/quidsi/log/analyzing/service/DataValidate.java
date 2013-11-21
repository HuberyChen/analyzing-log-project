package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
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
    private ErrorHandlingService errorHandlingService;

    public List<LogFileWrapper> initializeAllActionLogFileWrappers(String path, ActionLogSchedule actionLogSchedule) {
        return getLogFileWrapperWithProjectAndServerAll(dateConvertToString(new Date()), dateConvertToString(new Date()), path, actionLogSchedule);
    }

    public String dateConvertToString(Date date) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(DateUtils.getMonth(date) + 1)).append("/").append(String.valueOf(DateUtils.getDay(date) - 1)).append("/").append(String.valueOf(DateUtils.getYear(date)));
        return builder.toString();
    }

    public List<LogFileWrapper> initializeLogFileWrappers(ActionLogSchedule schedule, String path) {

        String projectName = schedule.getProject();
        String serverName = schedule.getInstance();
        String startDate = schedule.getStartDate();
        String endDate = schedule.getEndDate();

        if (!projectName.equals(ServiceConstant.TYPE_ALL) && serverName.equals(ServiceConstant.TYPE_ALL)) {
            return getLogFileWrapperWithServerAll(startDate, endDate, path, projectName, schedule);
        }

        if (projectName.equals(ServiceConstant.TYPE_ALL) && serverName.equals(ServiceConstant.TYPE_ALL)) {
            return getLogFileWrapperWithProjectAndServerAll(startDate, endDate, path, schedule);
        }

        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        Project project = projectService.getProjectByName(projectName);

        if (null == project) {
            String errMsg = "Project is not existed. ";
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
        }
        Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);
        if (null == server) {
            String errMsg = String.format("%s does not have instance. ", project.getName());
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
        }
        logFileWrappers.add(new LogFileWrapper(project, server, startDate, endDate, path));
        return logFileWrappers;

    }

    private List<LogFileWrapper> getLogFileWrapperWithProjectAndServerAll(String startDate, String endDate, String path, ActionLogSchedule schedule) {
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            String errMsg = "Projects are not existed. ";
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
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
            String errMsg = "Servers are not existed. ";
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
        }
        return logFileWrappers;
    }

    private List<LogFileWrapper> getLogFileWrapperWithServerAll(String startDate, String endDate, String path, String projectName, ActionLogSchedule schedule) {
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        Project project = projectService.getProjectByName(projectName);
        if (null == project) {
            String errMsg = "Project is not existed. ";
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
        }
        List<Server> servers = serverService.getServersByProjectId(project.getId());
        if (CollectionUtils.isEmpty(servers)) {
            String errMsg = String.format("%s does not have instance. ", projectName);
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
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

    @Inject
    public void setErrorHandlingService(ErrorHandlingService errorHandlingService) {
        this.errorHandlingService = errorHandlingService;
    }
}
