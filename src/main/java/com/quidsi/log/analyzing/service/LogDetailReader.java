package com.quidsi.log.analyzing.service;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.core.util.StringUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.utils.FileFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class LogDetailReader {

    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;
    private ErrorHandlingService errorHandlingService;

    @Transactional
    public void saveActionLogDetail(LogFile logFile, ActionLogSchedule schedule) {

        List<ActionLogDetail> records = new ArrayList<>();
        File file = new File(logFile.getAbsolutePath());
        if (!file.exists()) {
            String errMsg = "file is not exists. ";
            errorHandlingService.errorHandling(errMsg, schedule);
            throw new IllegalStateException(errMsg);
        }
        Map<Integer, String[]> messageMap = FileFactory.logRead(file);

        if (CollectionUtils.isEmpty(messageMap)) {
            return;
        }

        for (Entry<Integer, String[]> entry : messageMap.entrySet()) {
            records.add(dataConvertToRecord(entry.getValue(), logFile.getId()));
        }
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        actionLogDetailService.saveList(records);
        logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
        logFileService.update(logFile);
    }

    private ActionLogDetail dataConvertToRecord(String[] messages, int logId) {
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
        return DateUtils.date(dateTime[0], dateTime[1] - 1, dateTime[2], dateTime[3], dateTime[4], dateTime[5]);
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

    @Inject
    public void setErrorHandlingService(ErrorHandlingService errorHandlingService) {
        this.errorHandlingService = errorHandlingService;
    }
}
