package com.quidsi.log.analyzing.dao;

import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.domain.Project;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

/**
 * @author hubery.chen
 */
public class ProjectDaoTest extends SpringTest {
    private ProjectDao projectDao;

    @Test
    public void test() {
        List<Project> projects = projectDao.getProjectByCondition("gif");
        Assert.assertNotNull(projects);
    }

    @Inject
    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }
}
