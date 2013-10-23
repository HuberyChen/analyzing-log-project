package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;

@Service
public class DataConver {

    public String dateConverToString(Date date) {
        StringBuilder dateString = new StringBuilder();
        dateString.append(DateUtils.getYear(date)).append("-").append(DateUtils.getMonth(date) + 1).append("-").append(DateUtils.getDay(date));
        return dateString.toString();
    }

    public ActionLogDetail dataConverToRecord(String[] messages, int logId) {
        ActionLogDetail record = new ActionLogDetail();
        record.setLogId(logId);
        record.setRecordTime(dataConverToDate(messages[0]));
        record.setStatus(messages[1]);
        record.setInterfaceName(messages[3]);
        record.setElapsedTime(Convert.toInt(messages[4].trim(), 0));
        record.setRequestMethod(messages[7]);
        record.setErrorCode(messages[8]);
        record.setExceptionMsg(messages[9]);
        record.setLogAddress(messages[11]);
        return record;
    }

    public LogFile dataConverToLogFile(File log, Server server) {
        String logName = log.getName();
        String absolutePath = log.getAbsolutePath();
        LogFile logFile = new LogFile();
        if (logName.endsWith(LogFile.GZ_SUFFIX)) {
            logName = logName.replace(LogFile.GZ_SUFFIX, "");
        }
        if (logName.contains(LogFile.LOG_TYPE_ACTION)) {
            logFile.setLogType(LogFile.LOG_TYPE_ACTION);
        }
        String[] name = logName.replace(LogFile.LOG_SUFFIX, "").split("_");
        logFile.setLogName(name[0]);
        logFile.setSequence(Integer.parseInt(name[1]));
        logFile.setProjectId(server.getProjectId());
        logFile.setServerId(server.getId());
        logFile.setAbsolutePath(absolutePath);
        if (absolutePath.contains(LogFile.IsDecomposed.Y.toString())) {
            logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
        } else {
            logFile.setIsDecomposed(LogFile.IsDecomposed.N);
        }
        return logFile;
    }

    public Project dataConverToProject(String projectName) {
        Project Project = new Project();
        Project.setName(projectName);
        return Project;
    }

    public Server dataConverToServer(int projectId, String serverName) {
        Server server = new Server();
        server.setProjectId(projectId);
        server.setServerName(serverName);
        return server;
    }

    private Date dataConverToDate(String dateMessage) {
        final int size = 6;
        String[] date = dateMessage.split("-");
        int[] dateTime;
        dateTime = new int[size];
        for (int i = 0; i < size; i++) {
            dateTime[i] = Integer.parseInt(date[i]);
        }
        return DateUtils.date(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4], dateTime[5]);
    }
}
