package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.utils.FileFactory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    private ScheduleService scheduleService;

    @Transactional
    public LogFileWrapper saveLogFilesNotExisted(LogFileWrapper logFileWrapper) {
        List<LogFile> logFilesNotExisted = getLogFileNotExisted(logFilesLoader.loaderAllLog(logFileWrapper));
        if (CollectionUtils.isEmpty(logFilesNotExisted)) {
            return logFileWrapper;
        }
        logFileService.saveList(logFilesNotExisted);
        logFileWrapper.getLogFilesHistories().addAll(logFilesNotExisted);

        ActionLogSchedule actionLogSchedule = logFileWrapper.getActionLogSchedule();
        actionLogSchedule.setNote(actionLogSchedule.getNote() + "save log is success, log number = " + logFilesNotExisted.size() + ". ");
        scheduleService.update(actionLogSchedule);
        return logFileWrapper;
    }

    @Transactional
    public LogFileWrapper decompression(LogFileWrapper logFileWrapper) {
        List<LogFile> actionLogFilesHistories = logFileWrapper.getLogFilesHistories();
        List<LogFile> uncompressionActionLogs = new ArrayList<>();

        if (CollectionUtils.isEmpty(actionLogFilesHistories)) {
            return logFileWrapper;
        }
        for (LogFile logFile : actionLogFilesHistories) {
            if (logFile.getIsDecomposed().equals(LogFile.IsDecomposed.N) && logFile.getLogType().equals(ServiceConstant.LOG_TYPE_ACTION)) {
                uncompressionActionLogs.add(logFile);
                String absolutePath = FileFactory.unGz(new File(logFile.getAbsolutePath()));
                if (!StringUtils.hasText(absolutePath)) {
                    throw new IllegalStateException("Log is decomposed");
                }
                logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
                logFile.setAbsolutePath(absolutePath);
                logFileService.update(logFile);
            }
        }

        ActionLogSchedule actionLogSchedule = logFileWrapper.getActionLogSchedule();
        actionLogSchedule.setNote(actionLogSchedule.getNote() + "decompression log is success, log number = " + uncompressionActionLogs.size() + ". ");
        scheduleService.update(actionLogSchedule);
        return logFileWrapper;

    }

    public LogFileWrapper saveActionLogDetail(LogFileWrapper logFileWrapper) {
        List<LogFile> actionLogFilesHistories = logFileWrapper.getLogFilesHistories();
        List<LogFile> unAnalyzedlogFiles = new ArrayList<>();
        if (CollectionUtils.isEmpty(actionLogFilesHistories)) {
            return logFileWrapper;
        }

        for (LogFile logFile : actionLogFilesHistories) {
            if (logFile.getIsAnalyzed().equals(LogFile.IsAnalyzed.Y)) {
                continue;
            }
            logDetailReader.saveActionLogDetail(logFile, logFileWrapper.getActionLogSchedule());
            unAnalyzedlogFiles.add(logFile);
        }

        ActionLogSchedule actionLogSchedule = logFileWrapper.getActionLogSchedule();
        actionLogSchedule.setNote(actionLogSchedule.getNote() + "analyzing log is success, log number = " + unAnalyzedlogFiles.size() + ". ");
        scheduleService.update(actionLogSchedule);
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

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
