package com.quidsi.log.analyzing.controller;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.service.LogDetailReader;
import com.quidsi.log.analyzing.service.LogFileLoader;
import com.quidsi.log.analyzing.service.LogFileOperation;

@Controller
public class ActionLogAnalyzingController {

    private LogFileOperation logFileOperation;
    private LogDetailReader logDetailReader;
    private LogFileLoader logFileLoader;

    @RequestMapping(value = "/project/instance/log/action", method = RequestMethod.GET)
    @ResponseBody
    public void actionLogAnalyzing(@Valid @RequestParam String root) {
        logFileLoader.scanProject(root);
        logFileLoader.scanServer(root);
        logFileLoader.logRead(new Date(), root);
        logFileOperation.decompression();
        logDetailReader.scanActionLogDetail();
    }

    @Inject
    public void setLogFileOperation(LogFileOperation logFileOperation) {
        this.logFileOperation = logFileOperation;
    }

    @Inject
    public void setLogDetailReader(LogDetailReader logDetailReader) {
        this.logDetailReader = logDetailReader;
    }

    @Inject
    public void setLogFileLoader(LogFileLoader logFileLoader) {
        this.logFileLoader = logFileLoader;
    }
}
