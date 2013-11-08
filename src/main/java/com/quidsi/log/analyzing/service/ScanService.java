package com.quidsi.log.analyzing.service;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.utils.ScanUtils;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScanService {

    private ProjectService projectService;

    private ServerService serverService;

    public List<String> scanProject(String root) {
        List<String> projectsAll = ScanUtils.scanDirectoryFileName(root);
        List<String> projectsNotExisted = new ArrayList<>();
        if (CollectionUtils.isEmpty(projectsAll)) {
            return null;
        }
        for (String projectName : projectsAll) {
            if (null == projectService.getProjectByName(projectName)) {
                projectsNotExisted.add(projectName);
            }
        }
        return projectsNotExisted;
    }

    public List<String> scanServer(String root, String projectName) {
        Project project = projectService.getProjectByName(projectName);
        if (null == project) {
            throw new IllegalStateException("Project is not existed.");
        }

        List<String> serversAll = ScanUtils.scanSecondaryDirectoryFileName(root, projectName);
        List<String> serversNotExisted = new ArrayList<>();
        if (CollectionUtils.isEmpty(serversAll)) {
            return null;
        }
        for (String serverName : serversAll) {
            if (null == serverService.getServerByProjectIdAndServerName(project.getId(), serverName)) {
                serversNotExisted.add(serverName);
            }
        }
        return serversNotExisted;
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
