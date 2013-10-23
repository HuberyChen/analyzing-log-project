package com.quidsi.log.analyzing.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.utils.FileFactory;
import com.quidsi.log.analyzing.utils.ScanUtils;

@Controller
public class LogFileController {

    private ServerService serverService;
    private LogFileService logFileService;
    private DataConver dataConver;
    private ProjectService projectService;
    private ActionLogDetailService actionLogDetailService;

    @RequestMapping(value = "/project/instance/log", method = RequestMethod.GET)
    @ResponseBody
    public void scanServerLog(@Valid @RequestParam String root) {
        List<Server> serverList = serverService.getServers();
        if (CollectionUtils.isEmpty(serverList)) {
            return;
        }

        for (Server server : serverList) {
            Project project = projectService.getProjectById(server.getId());
            Map<String, List<String>> filterMap = logFileService.initializeFilters(project.getName(), server.getServerName(), dataConver.dateConverToString(new Date()));

            List<File> logs = ScanUtils.scan(root, filterMap.get("pathFilters"), filterMap.get("nameFilters"));
            if (CollectionUtils.isEmpty(logs)) {
                continue;
            }

            Integer maxSequence = logFileService.getMaxHourLogFilesByDate(dataConver.dateConverToString(new Date()));
            for (File log : logs) {
                LogFile logFile = dataConver.dataConverToLogFile(log, server);
                if (null != maxSequence && logFile.getSequence() <= maxSequence) {
                    continue;
                }
                List<ActionLogDetail> records = actionLogDetailService.getRecordsByLogId(logFile.getId());
                logFile.setIsAnalyzed(LogFile.IsAnalyzed.N);
                if (!CollectionUtils.isEmpty(records)) {
                    logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
                }
                logFileService.save(logFile);
            }
        }
    }

    @RequestMapping(value = "/project/instance/log/decompression", method = RequestMethod.GET)
    @ResponseBody
    public void decompressionLog() {
        List<LogFile> uncompressionActionLogs = logFileService.getLogFilesByIsDecomposed(LogFile.IsDecomposed.N.toString());
        if (CollectionUtils.isEmpty(uncompressionActionLogs)) {
            return;
        }
        for (LogFile actionLog : uncompressionActionLogs) {
            FileFactory.unGz(new File(actionLog.getAbsolutePath()));
            actionLog.setIsDecomposed(LogFile.IsDecomposed.Y);
            logFileService.update(actionLog);
        }
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setDataConver(DataConver dataConver) {
        this.dataConver = dataConver;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

}
