package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.junit.Test;

import com.quidsi.log.analyzing.SpringTest;

public class ProjectDaoTest extends SpringTest {

    private ProjectDao projectDao;

    @Test
    public void getMaxProjectIdTest() {
        System.out.println(projectDao.getMaxProjectId());
    }

    @Inject
    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

}
