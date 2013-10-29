package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.utils.FileFactory;

@Component
public class LogFileOperation {

    private LogFileService logFileService;

    private LogFilesLoader logFilesLoader;

    private LogDetailReader logDetailReader;

    private LogFileWrapper logFileWrapper;

    public void initializeData(LogFileWrapper initializeLogFileWrapper) {
        logFileWrapper = initializeLogFileWrapper;
    }

    public void saveActionLogDetail() {
        logDetailReader.saveActionLogDetail(logFileWrapper);
    }

    public void saveLogFilesNotExisted() {
        logFilesLoader.initializeData(logFileWrapper);
        Map<String, LogFile> logFilesNotExisted = getLogFileNotExisted(logFilesLoader.logLoader());
        if (CollectionUtils.isEmpty(logFilesNotExisted)) {
            return;
        }
        logFileService.saveMap(logFilesNotExisted);
        logFileWrapper.addLogFilesHistories(logFilesNotExisted);
    }

    public void decompression() {
        Map<String, LogFile> uncompressionActionLogs = logFileWrapper.getLogFilesHistories();

        if (CollectionUtils.isEmpty(uncompressionActionLogs)) {
            return;
        }
        for (Entry<String, LogFile> entry : uncompressionActionLogs.entrySet()) {
            LogFile actionLog = entry.getValue();
            if (actionLog.getIsDecomposed().equals(LogFile.IsDecomposed.Y)) {
                continue;
            }
            String absolutePath = FileFactory.unGz(new File(actionLog.getAbsolutePath()));
            actionLog.setIsDecomposed(LogFile.IsDecomposed.Y);
            actionLog.setAbsolutePath(absolutePath);
            logFileService.update(actionLog);
        }

    }

    private Map<String, LogFile> getLogFileNotExisted(LogFileWrapper logFileWrapper) {
        Map<String, LogFile> logFilesNotExisted = new HashMap<>();
        Map<String, LogFile> allLogFiles = logFileWrapper.getAllLogFiles();

        if (CollectionUtils.isEmpty(allLogFiles)) {
            return null;
        }

        for (Entry<String, LogFile> entry : allLogFiles.entrySet()) {
            LogFile logFile = entry.getValue();

            Map<String, LogFile> logFilesHistories = logFileWrapper.getLogFilesHistories();
            if (logFilesHistories.containsKey(entry.getKey())) {
                continue;
            }
            LogFile logFileHistory = logFilesNotExisted.get(entry.getKey());
            if (null == logFileHistory) {
                logFilesNotExisted.put(entry.getKey(), logFile);
                continue;
            }
            if (!logFileHistory.getAbsolutePath().equals(logFile.getAbsolutePath()) && logFile.getAbsolutePath().contains(LogFile.DECOMPRESSION)) {
                logFileHistory.setAbsolutePath(logFile.getAbsolutePath());
                logFileHistory.setIsDecomposed(LogFile.IsDecomposed.Y);
            }
        }
        return logFilesNotExisted;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setLogFilesLoader(LogFilesLoader logFilesLoader) {
        this.logFilesLoader = logFilesLoader;
    }

    @Inject
    public void setLogDetailReader(LogDetailReader logDetailReader) {
        this.logDetailReader = logDetailReader;
    }

}
