package com.quidsi.log.analyzing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.request.ActionLogAnalyzingRequest;
import com.quidsi.log.analyzing.service.LogDetailReader;
import com.quidsi.log.analyzing.service.LogFileOperation;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.LogFilesLoader;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;

@Controller
public class ActionLogAnalyzingController {

    private final Logger logger = LoggerFactory.getLogger(ActionLogAnalyzingController.class);

    private LogDetailReader logDetailReader;
    private ProjectService projectService;
    private ServerService serverService;
    private LogFileOperation logFileOperation;
    private LogFilesLoader logFilesLoader;
    private LogFileService logFileService;

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.POST)
    @ResponseBody
    public void actionLogAnalyzing(@Valid @RequestBody ActionLogAnalyzingRequest request) {

        List<LogFileWrapper> logFileWrappers = initializeLogFileWrappers(request);
        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }
        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            List<LogFile> logFilesNotExisted = logFilesLoader.logLoader(logFileWrapper).getLogFileNotExisted();
            if (CollectionUtils.isEmpty(logFilesNotExisted)) {
                continue;
            }
            logFileService.saveList(logFilesNotExisted);
        }

        logFileOperation.decompression();

        logDetailReader.scanActionLogDetail();
    }

    private List<LogFileWrapper> initializeLogFileWrappers(ActionLogAnalyzingRequest request) {

        String projectName = request.getProjectName();
        String serverName = request.getServerName();
        Date date = request.getDate();
        String path = request.getRoot();

        if (null == projectName && null != serverName) {
            logger.error("Server can not find the project.");
            return null;
        }

        if (null != projectName && null == serverName) {
            return getLogFileWrapperWithServerNull(date, path, projectName);
        }

        if (null == projectName && null == serverName) {
            return getLogFileWrapperWithProjectAndServerBothNull(date, path);
        }

        Project project = projectService.getProjectByName(projectName);

        if (null == project) {
            logger.error("Project is not existed");
            return null;
        }
        Server server = serverService.getServerByProjectIdAndServerName(project.getId(), serverName);
        if (null == server) {
            logger.error("%d does not have instance", project.getName());
            return null;
        }
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        logFileWrappers.add(new LogFileWrapper(project, server, date, path));
        return logFileWrappers;

    }

    private List<LogFileWrapper> getLogFileWrapperWithProjectAndServerBothNull(Date date, String path) {
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            logger.error("Projects are not existed");
            return null;
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
        return logFileWrappers;
    }

    private List<LogFileWrapper> getLogFileWrapperWithServerNull(Date date, String path, String projectName) {
        List<LogFileWrapper> logFileWrappers = new ArrayList<>();
        Project project = projectService.getProjectByName(projectName);
        if (null == project) {
            logger.error("Project is not existed");
            return null;
        }
        List<Server> servers = serverService.getServersByProjectId(project.getId());
        if (CollectionUtils.isEmpty(servers)) {
            logger.error("%d does not have instance", projectName);
            return null;
        }
        for (Server server : servers) {
            logFileWrappers.add(new LogFileWrapper(project, server, date, path));
        }
        return logFileWrappers;

    }

    @Inject
    public void setLogDetailReader(LogDetailReader logDetailReader) {
        this.logDetailReader = logDetailReader;
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
    public void setLogFileOperation(LogFileOperation logFileOperation) {
        this.logFileOperation = logFileOperation;
    }

    @Inject
    public void setLogFilesLoader(LogFilesLoader logFilesLoader) {
        this.logFilesLoader = logFilesLoader;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

}
