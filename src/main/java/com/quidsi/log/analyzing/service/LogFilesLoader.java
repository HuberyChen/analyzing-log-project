package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Component
public class LogFilesLoader {

    private LogFileService logFileService;

    private ActionLogDetailService actionLogDetailService;

    public LogFileWrapper loaderAllLog(LogFileWrapper logFileWrapper) {

        String date = logFileWrapper.getDate();
        Project project = logFileWrapper.getProject();
        Server server = logFileWrapper.getServer();
        String path = logFileWrapper.getPath();

        logFileWrapper.getAllLogFiles().addAll(getFileWrapperAllFile(project, server, date, path));

        logFileWrapper.getLogFilesHistories().addAll(logFileService.getLogFilesByFuzzyName(date, project.getId(), server.getId()));

        return logFileWrapper;
    }

    private List<LogFile> getFileWrapperAllFile(Project project, Server server, String date, String path) {
        Map<String, List<String>> filterMap = logFileService.initializeFilters(project.getName(), server.getServerName(), date);
        List<String> logPaths = ScanUtils.scan(path, filterMap.get(ServiceConstant.PATHFILTERS), filterMap.get(ServiceConstant.NAMEFILTERS));
        return mapConverToList(generateLogFiles(logPaths, server));
    }

    private List<LogFile> mapConverToList(Map<String, LogFile> map) {
        List<LogFile> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        for (Entry<String, LogFile> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    private Map<String, LogFile> generateLogFiles(List<String> logPaths, Server server) {
        Map<String, LogFile> logFiles = new HashMap<>();

        if (CollectionUtils.isEmpty(logPaths)) {
            return null;
        }
        for (String logPath : logPaths) {
            LogFile logFile = generateLogFile(logPath, server);
            LogFile logFileIsExisted = logFiles.get(logFile.getLogName());

            if (null != logFileIsExisted && !logFileIsExisted.getAbsolutePath().equals(logFile.getAbsolutePath()) && logFile.getAbsolutePath().contains(ServiceConstant.DECOMPRESSION)) {

                logFileIsExisted.setAbsolutePath(logFile.getAbsolutePath());
                logFileIsExisted.setIsDecomposed(LogFile.IsDecomposed.Y);
                continue;
            }

            logFiles.put(logFile.getLogName(), logFile);
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
        if (logName.endsWith(ServiceConstant.GZ_SUFFIX)) {
            logName = logName.replace(ServiceConstant.GZ_SUFFIX, "");
        }
        if (logName.contains(ServiceConstant.LOG_TYPE_ACTION)) {
            logFile.setLogType(ServiceConstant.LOG_TYPE_ACTION);
        }
        logFile.setLogName(logName.replace(ServiceConstant.LOG_SUFFIX, ""));
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
