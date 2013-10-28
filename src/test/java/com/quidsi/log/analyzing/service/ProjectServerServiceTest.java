package com.quidsi.log.analyzing.service;

import javax.inject.Inject;

import org.junit.Test;

import com.quidsi.log.analyzing.SpringTest;

public class ProjectServerServiceTest extends SpringTest {

    private ProjectServerService projectServerService;

    @Test
    public void newProjectTest() {
        String root = "\\\\sharedoc\\�ļ�������\\Java-Team\\prod log";
        projectServerService.scanNewProjectAndServer(root);
    }

    @Inject
    public void setProjectServerService(ProjectServerService projectServerService) {
        this.projectServerService = projectServerService;
    }

}
