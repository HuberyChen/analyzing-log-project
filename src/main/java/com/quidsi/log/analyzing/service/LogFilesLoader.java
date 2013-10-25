package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Component
public class LogFilesLoader {

    private LogFileService logFileService;

    private ActionLogDetailService actionLogDetailService;

    private LogFileWrapper logFileWrapper;

    public LogFileWrapper logLoader(LogFileWrapper initializeLogFileWrappers) {

        logFileWrapper = initializeLogFileWrappers;

        loadAllFile();
        loadActionLogFilesIsExisted();

        return logFileWrapper;
    }

    private void loadActionLogFilesIsExisted() {
        List<LogFile> logFilesIsExisted = logFileService.getLogFilesByLogFileWrapper(logFileWrapper);
        logFileWrapper.addLogFilesHistories(logFilesIsExisted);
    }

    private void loadAllFile() {
        Map<String, List<String>> filterMap = logFileService.initializeFilters(logFileWrapper);
        List<String> logPaths = ScanUtils.scan(logFileWrapper.getPath(), filterMap.get("pathFilters"), filterMap.get("nameFilters"));
        logFileWrapper.addAllLogFiles(generateLogFiles(logPaths, logFileWrapper));
    }

    private List<LogFile> generateLogFiles(List<String> logPaths, LogFileWrapper logFileWrapper) {
        List<LogFile> logFiles = new ArrayList<>();
        if (CollectionUtils.isEmpty(logPaths)) {
            return null;
        }
        for (String logPath : logPaths) {
            logFiles.add(generateLogFile(logPath, logFileWrapper.getServer()));
        }
        return logFiles;
    }

    private LogFile generateLogFile(String logPath, Server server) {
        File log = new File(logPath);

        LogFile logFile = generateLogFileByLogName(log.getName());
        logFile.setProjectId(server.getProjectId());
        logFile.setServerId(server.getId());
        logFile.setAbsolutePath(logPath);
        if (logPath.contains(LogFile.IsDecomposed.Y.toString())) {
            logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
        } else {
            logFile.setIsDecomposed(LogFile.IsDecomposed.N);
        }

        if (judgeLogFileIsAnalyzed(logFile)) {
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
        } else {
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.N);
        }
        return logFile;
    }

    private boolean judgeLogFileIsAnalyzed(LogFile logFile) {
        boolean result = true;
        List<ActionLogDetail> records = actionLogDetailService.getRecordsByLogId(logFile.getId());
        if (CollectionUtils.isEmpty(records)) {
            result = false;
        }
        return result;
    }

    private LogFile generateLogFileByLogName(String logName) {
        LogFile logFile = new LogFile();
        if (logName.endsWith(LogFile.GZ_SUFFIX)) {
            logName = logName.replace(LogFile.GZ_SUFFIX, "");
        }
        if (logName.contains(LogFile.LOG_TYPE_ACTION)) {
            logFile.setLogType(LogFile.LOG_TYPE_ACTION);
        }
        logFile.setLogName(logName.replace(LogFile.LOG_SUFFIX, ""));
        return logFile;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

}
