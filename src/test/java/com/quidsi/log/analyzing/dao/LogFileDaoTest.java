package com.quidsi.log.analyzing.dao;

import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

import junit.framework.Assert;

public class LogFileDaoTest extends SpringTest {

    private LogFileDao logFileDao;
    private ProjectDao projectDao;

    @Test
    @Transactional
    public void getLogFilesByFuzzyNameTest() {
        LogFile logFile = logFileDao.getLogFilesByName("giftco-service-action.2013-11-02_14", "giftco-service", "Prod-gcsvc1");
        Assert.assertNotNull(logFile);
        List<LogFile> logFiles = logFileDao.getLogFilesByFuzzyName("2013-11-03", "giftmessage-service", "Prod-gmsvc2");
        Assert.assertNotNull(logFiles);
    }

    @Test
    public void getProjectTest() {
        Project project = projectDao.getProjectById(1);
        Assert.assertNotNull(project);
        project = projectDao.getProjectByName("giftco-service");
        Assert.assertNotNull(project);
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }

    @Inject
    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

}
