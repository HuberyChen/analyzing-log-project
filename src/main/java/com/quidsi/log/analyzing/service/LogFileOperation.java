package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.utils.FileFactory;

@Component
public class LogFileOperation {

    private LogFileService logFileService;

    public void decompression() {
        List<LogFile> uncompressionActionLogs = logFileService.getLogFilesByIsDecomposed(LogFile.IsDecomposed.N.toString());
        if (CollectionUtils.isEmpty(uncompressionActionLogs)) {
            return;
        }
        for (LogFile actionLog : uncompressionActionLogs) {
            String absolutePath = FileFactory.unGz(new File(actionLog.getAbsolutePath()));
            actionLog.setIsDecomposed(LogFile.IsDecomposed.Y);
            actionLog.setAbsolutePath(absolutePath);
            logFileService.update(actionLog);
        }
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

}
