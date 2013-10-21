package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.AnalyzedLog;
import com.quidsi.log.analyzing.domain.DecomposedLog;
import com.quidsi.log.analyzing.domain.SystemLogRecord;

@Service
public class DataConver {

    public List<DecomposedLog> dataConverToDecomposedLogs(List<File> logs) {
        List<DecomposedLog> decomposedLogs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(logs)) {
            for (File log : logs) {
                DecomposedLog decomposedLog = new DecomposedLog();
                decomposedLog.setName(log.getName().replace(log.getParent(), ""));
                decomposedLog.setDecomposedDate(new Date());
                decomposedLogs.add(decomposedLog);
            }
        }
        return decomposedLogs;
    }

    public AnalyzedLog dataConverToAnalyzedActionLog(String logName) {
        AnalyzedLog log = new AnalyzedLog();
        log.setAnalyzedDate(new Date());
        log.setName(logName);
        log.setType("ActionLog");
        return log;
    }

    public SystemLogRecord dataConverToRecord(String[] messages, String system, String host, int logId) {
        SystemLogRecord record = new SystemLogRecord();
        record.setLogId(logId);
        record.setSystem(system);
        record.setHost(host);
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

    public Date dataConverToDate(String dateMessage) {
        final int size = 6;
        String[] date = dateMessage.split("-");
        int[] dateTime;
        dateTime = new int[size];
        for (int i = 0; i < size; i++) {
            dateTime[i] = Integer.parseInt(date[i]);
        }
        return DateUtils.date(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4], dateTime[5]);
    }

    public String dataConverToString(Date date) {
        StringBuilder current = new StringBuilder();
        current.append(DateUtils.getYear(date)).append("-");
        current.append(DateUtils.getMonth(date)).append("-");
        current.append(DateUtils.getDay(date));
        return current.toString();
    }
}
