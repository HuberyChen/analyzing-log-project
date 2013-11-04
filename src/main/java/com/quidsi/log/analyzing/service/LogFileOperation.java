package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.utils.FileFactory;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class LogFileOperation {

    private LogFileService logFileService;

    private LogFilesLoader logFilesLoader;

    private LogDetailReader logDetailReader;

    public LogFileWrapper saveLogFilesNotExisted(LogFileWrapper logFileWrapper) {
        List<LogFile> logFilesNotExisted = getLogFileNotExisted(logFilesLoader.loaderAllLog(logFileWrapper));
        if (CollectionUtils.isEmpty(logFilesNotExisted)) {
            return logFileWrapper;
        }
        logFileService.saveList(logFilesNotExisted);
        logFileWrapper.getLogFilesHistories().addAll(logFilesNotExisted);
        return logFileWrapper;
    }

    public LogFileWrapper decompression(LogFileWrapper logFileWrapper) {
        List<LogFile> uncompressionActionLogs = logFileWrapper.getLogFilesHistories();

        if (CollectionUtils.isEmpty(uncompressionActionLogs)) {
            return logFileWrapper;
        }
        for (LogFile logFile : uncompressionActionLogs) {
            if (logFile.getIsDecomposed().equals(LogFile.IsDecomposed.N) && logFile.getLogType().equals(ServiceConstant.LOG_TYPE_ACTION)) {
                String absolutePath = FileFactory.unGz(new File(logFile.getAbsolutePath()));
                if (!StringUtils.hasText(absolutePath)) {
                    throw new IllegalStateException("Log is decomposed");
                }
                logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
                logFile.setAbsolutePath(absolutePath);
                logFileService.update(logFile);
            }
        }
        return logFileWrapper;

    }

    public LogFileWrapper saveActionLogDetail(LogFileWrapper logFileWrapper) {
        List<LogFile> unAnalyzedlogFiles = logFileWrapper.getLogFilesHistories();
        if (CollectionUtils.isEmpty(unAnalyzedlogFiles)) {
            return logFileWrapper;
        }

        for (LogFile logFile : unAnalyzedlogFiles) {
            logDetailReader.saveActionLogDetail(logFile);
        }
        return logFileWrapper;
    }

    private List<LogFile> getLogFileNotExisted(LogFileWrapper logFileWrapper) {
        List<LogFile> logFilesNotExisted = new ArrayList<>();
        Map<String, LogFile> allLogFiles = listConverToMap(logFileWrapper.getAllLogFiles());

        if (CollectionUtils.isEmpty(allLogFiles)) {
            return null;
        }

        for (Entry<String, LogFile> entry : allLogFiles.entrySet()) {
            LogFile logFile = entry.getValue();

            Map<String, LogFile> logFilesHistories = listConverToMap(logFileWrapper.getLogFilesHistories());
            if (!CollectionUtils.isEmpty(logFilesHistories) && logFilesHistories.containsKey(entry.getKey())) {
                continue;
            }
            logFilesNotExisted.add(logFile);
        }
        return logFilesNotExisted;
    }

    private Map<String, LogFile> listConverToMap(List<LogFile> list) {
        Map<String, LogFile> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (LogFile logFile : list) {
            map.put(logFile.getLogName(), logFile);
        }
        return map;
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
