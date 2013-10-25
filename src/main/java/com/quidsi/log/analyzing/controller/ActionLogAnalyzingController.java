package com.quidsi.log.analyzing.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.request.ActionLogAnalyzingRequest;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.LogDetailReader;
import com.quidsi.log.analyzing.service.LogFileOperation;

@Controller
public class ActionLogAnalyzingController {

    private LogDetailReader logDetailReader;
    private LogFileOperation logFileOperation;
    private DataConver dataConver;

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.POST)
    @ResponseBody
    public void actionLogAnalyzing(@Valid @RequestBody ActionLogAnalyzingRequest request) {

        List<LogFileWrapper> logFileWrappers = dataConver.initializeLogFileWrappers(request);

        if (CollectionUtils.isEmpty(logFileWrappers)) {
            return;
        }

        for (LogFileWrapper logFileWrapper : logFileWrappers) {
            logFileOperation.saveLogFilesNotExisted(logFileWrapper);
            logFileOperation.decompression(logFileWrapper);
            logDetailReader.scanActionLogDetail(logFileWrapper);
        }
    }

    @Inject
    public void setLogDetailReader(LogDetailReader logDetailReader) {
        this.logDetailReader = logDetailReader;
    }

    @Inject
    public void setLogFileOperation(LogFileOperation logFileOperation) {
        this.logFileOperation = logFileOperation;
    }

    @Inject
    public void setDataConver(DataConver dataConver) {
        this.dataConver = dataConver;
    }

}
