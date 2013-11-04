package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.StopWatch;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.utils.ScanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LogFilesLoader {

    private final Logger logger = LoggerFactory.getLogger(LogFilesLoader.class);

    private LogFileService logFileService;

    private ActionLogDetailService actionLogDetailService;

    public LogFileWrapper loaderAllLog(LogFileWrapper logFileWrapper) {

        Project project = logFileWrapper.getProject();
        Server server = logFileWrapper.getServer();
        String path = logFileWrapper.getPath();
        List<String> dateList = logFileWrapper.getDateRange();

        if (CollectionUtils.isEmpty(dateList)) {
            throw new IllegalStateException("Date is null");
        }

        for (String date : dateList) {
            List<LogFile> allFiles = getFileWrapperAllFile(project, server, date, path);

            if (!CollectionUtils.isEmpty(allFiles)) {
                logFileWrapper.getAllLogFiles().addAll(allFiles);
            }

            logger.info("get log files by fuzzy name={}, projectId={}, serverId={}", date, project.getId(), server.getId());
            List<LogFile> logFilesHistories = logFileService.getLogFilesByFuzzyName(date, project.getId(), server.getId());

            if (!CollectionUtils.isEmpty(logFilesHistories)) {
                logFileWrapper.getLogFilesHistories().addAll(logFilesHistories);
            }
        }

        return logFileWrapper;
    }

    private List<LogFile> getFileWrapperAllFile(Project project, Server server, String date, String path) {
        StopWatch watch = new StopWatch();
        logger.info("scan log path={}", path);
        Map<String, List<String>> filterMap = logFileService.initializeFilters(project.getName(), server.getServerName(), date);
        List<String> logPaths = ScanUtils.scan(path, filterMap.get(ServiceConstant.PATHFILTERS), filterMap.get(ServiceConstant.NAMEFILTERS));
        if (CollectionUtils.isEmpty(logPaths)) {
            return null;
        }
        logger.info("scan log elapsedTime={}, log number={}", watch.elapsedTime(), logPaths.size());
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
        if (logName.contains(ServiceConstant.GZ_SUFFIX)) {
            logName.replace(ServiceConstant.GZ_SUFFIX, "");
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
