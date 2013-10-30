package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.utils.FileFactory;

@Component
public class LogDetailReader {

    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;

    @Transactional
    public void saveActionLogDetail(List<LogFile> logFiles) {
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {

            if (logFile.getIsAnalyzed().equals(LogFile.IsAnalyzed.Y)) {
                continue;
            }

            List<ActionLogDetail> records = new ArrayList<>();
            File file = new File(logFile.getAbsolutePath());
            Map<Integer, String[]> messageMap = FileFactory.logRead(file);

            if (CollectionUtils.isEmpty(messageMap)) {
                continue;
            }

            for (Entry<Integer, String[]> entry : messageMap.entrySet()) {
                records.add(dataConverToRecord(entry.getValue(), logFile.getId()));
            }
            if (CollectionUtils.isEmpty(records)) {
                continue;
            }
            actionLogDetailService.saveList(records);
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
            logFileService.update(logFile);
        }
    }

    private ActionLogDetail dataConverToRecord(String[] messages, int logId) {
        ActionLogDetail record = new ActionLogDetail();
        record.setLogId(logId);
        record.setRecordTime(dataConverToDate(messages[0]));
        record.setStatus(trim(messages[1]));
        record.setInterfaceName(trim(messages[3]));
        record.setElapsedTime(Convert.toInt(trim(messages[4]), 0));
        record.setRequestMethod(trim(messages[7]));
        record.setErrorCode(trim(messages[8]));
        record.setExceptionMsg(trim(messages[9]));
        record.setLogAddress(trim(messages[11]));
        return record;
    }

    private String trim(String str) {
        if (StringUtils.hasText(str)) {
            return str.trim();
        }
        return str;
    }

    private Date dataConverToDate(String dateMessage) {
        final int size = 6;
        String[] date = dateMessage.split("-");
        int[] dateTime = new int[size];
        for (int i = 0; i < size; i++) {
            dateTime[i] = Integer.parseInt(date[i]);
        }
        return DateUtils.date(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4], dateTime[5]);
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
