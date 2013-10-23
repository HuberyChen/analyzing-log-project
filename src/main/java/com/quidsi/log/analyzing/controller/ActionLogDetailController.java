package com.quidsi.log.analyzing.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.utils.FileFactory;

@Controller
public class ActionLogDetailController {

    private LogFileService logFileService;
    private ActionLogDetailService actionLogDetailService;
    private DataConver dataConver;

    @RequestMapping(value = "/project/instance/log/detail", method = RequestMethod.GET)
    @ResponseBody
    public void scanActionLogDetail() {
        List<LogFile> logFiles = logFileService.getLogFilesByIsAnalyzed(LogFile.IsAnalyzed.N.toString());
        if (CollectionUtils.isEmpty(logFiles)) {
            return;
        }
        for (LogFile logFile : logFiles) {
            List<ActionLogDetail> records = new ArrayList<>();
            File file = new File(logFile.getAbsolutePath());
            Map<Integer, String[]> messageMap = FileFactory.logRead(file);
            for (int i = 0; i < messageMap.size(); i++) {
                records.add(dataConver.dataConverToRecord(messageMap.get(i), logFile.getId()));
            }
            if (!CollectionUtils.isEmpty(records)) {
                continue;
            }
            actionLogDetailService.saveList(records);
            logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
            logFileService.update(logFile);
        }
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
    public void setDataConver(DataConver dataConver) {
        this.dataConver = dataConver;
    }

}
