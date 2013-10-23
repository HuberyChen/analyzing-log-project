package com.quidsi.log.analyzing.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Controller
public class ProjectController {

    private ProjectService projectService;
    private DataConver dataConver;
    private ServerService serverService;

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    @ResponseBody
    public void scanProject(@Valid @RequestParam String root) {
        List<String> projectNameList = ScanUtils.scanDirectoryFileName(root);
        if (CollectionUtils.isEmpty(projectNameList)) {
            return;
        }
        for (String projectName : projectNameList) {
            if (null == projectService.getProjectByName(projectName)) {
                projectService.save(dataConver.dataConverToProject(projectName));
            }
        }
    }

    @RequestMapping(value = "/project/instance", method = RequestMethod.GET)
    @ResponseBody
    public void scanServer(@Valid @RequestParam String root) {
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
                if (null == serverService.getServerByProjectIdAndServerName(project.getId(), serverName)) {
                    serverService.save(dataConver.dataConverToServer(project.getId(), serverName));
                }
            }
        }
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setDataConver(DataConver dataConver) {
        this.dataConver = dataConver;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }
}
