package com.quidsi.log.analyzing.utils;

import com.quidsi.log.analyzing.SpringTest;
import com.quidsi.log.analyzing.dao.LogFileDao;
import com.quidsi.log.analyzing.domain.LogFile;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

/**
 * @author hubery.chen
 */
public class LogFileDaoTest extends SpringTest {

    private LogFileDao logFileDao;

    @Test
    public void logFileDaoTest() {
        List<LogFile> logFiles = logFileDao.getLogFilesByCondition("2013-11-04", "giftco-service", "Prod-gcsvc1");
        for (LogFile logFile : logFiles) {
            System.out.println(logFile.getId());
        }
        Assert.assertNotNull(logFiles);
    }

    @Inject
    public void setLogFileDao(LogFileDao logFileDao) {
        this.logFileDao = logFileDao;
    }
}
