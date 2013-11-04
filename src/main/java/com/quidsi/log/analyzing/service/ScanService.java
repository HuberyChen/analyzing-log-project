package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.utils.ScanUtils;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.List;

@Component
public class ScanService {

    private ProjectService projectService;

    private ServerService serverService;

    public void scanProject(String root) {
        List<String> projectNameList = ScanUtils.scanDirectoryFileName(root);
        if (CollectionUtils.isEmpty(projectNameList)) {
            return;
        }
        for (String projectName : projectNameList) {
            if (null == projectService.getProjectByName(projectName)) {
                projectService.save(generateProjectByName(projectName));
            }
        }
    }

    public void scanServer(String root) {
        List<String> pathList = ScanUtils.scanDirectoryFilePath(root);
        if (CollectionUtils.isEmpty(pathList)) {
            return;
        }
        for (String path : pathList) {
            List<String> serverNameList = ScanUtils.scanDirectoryFileName(path);
            Project project = projectService.getProjectByName(ScanUtils.scanPathFileName(path));
            if (CollectionUtils.isEmpty(serverNameList) && null == project) {
                continue;
            }
            for (String serverName : serverNameList) {
                if (null != serverService.getServerByProjectIdAndServerName(project.getId(), serverName)) {
                    continue;
                }
                serverService.save(generateServer(project.getId(), serverName));
            }
        }
    }

    private Project generateProjectByName(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        return project;
    }

    private Server generateServer(int projectId, String serverName) {
        Server server = new Server();
        server.setProjectId(projectId);
        server.setServerName(serverName);
        return server;
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
