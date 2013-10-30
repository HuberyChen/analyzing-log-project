package com.quidsi.log.analyzing.service;

import javax.inject.Inject;

import org.junit.Test;

import com.quidsi.log.analyzing.SpringTest;

public class ProjectServerServiceTest extends SpringTest {

    private ProjectServerService projectServerService;

    @Test
    public void projectTest() {
        String root = "\\\\sharedoc\\文件交换区\\Java-Team\\prod log";
        projectServerService.scanProjectAndServer(root);
    }

    @Inject
    public void setProjectServerService(ProjectServerService projectServerService) {
        this.projectServerService = projectServerService;
    }

}
