package com.quidsi.log.analyzing;

import com.quidsi.core.util.DigestUtils;
import com.quidsi.log.analyzing.domain.ActionLogDetail;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.domain.User;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.service.UserService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Date;

/**
 * @author hubery.chen
 */
public class DataBasePopulator {

    private ActionLogDetailService actionLogDetailService;
    private LogFileService logFileService;
    private ProjectService projectService;
    private ServerService serverService;
    private UserService userService;

    @PostConstruct
    public void initData() {
        Project project = mockProject();
        projectService.save(project);

        Server server = mockServer(project.getId());
        serverService.save(server);

        LogFile logFile = mockLogFile(project.getName(), server.getServerName());
        logFileService.save(logFile);

        User user = mockUser();
        userService.saveUser(user);

        ActionLogDetail actionLogDetail = mockActionLogDetail(logFile.getId());
        actionLogDetailService.save(actionLogDetail);
    }

    private ActionLogDetail mockActionLogDetail(int logId) {
        ActionLogDetail actionLogDetail = new ActionLogDetail();
        actionLogDetail.setLogId(logId);
        actionLogDetail.setStatus("SUCCESS");
        actionLogDetail.setElapsedTime(356);
        actionLogDetail.setErrorCode("200");
        actionLogDetail.setExceptionMsg("");
        actionLogDetail.setExtension(null);
        actionLogDetail.setRecordTime(new Date());
        actionLogDetail.setRequestMethod("POST");
        actionLogDetail.setInterfaceName("GiftCoServiceSearchController-getGCByGcCodes");
        actionLogDetail.setLogAddress("");
        return actionLogDetail;
    }

    private User mockUser() {
        User user = new User();
        user.setName("1");
        user.setPassword(DigestUtils.sha512("1"));
        return user;
    }

    private LogFile mockLogFile(String projectName, String serverName) {
        LogFile logFile = new LogFile();
        logFile.setProject(projectName);
        logFile.setInstance(serverName);
        logFile.setLogName("giftco-service-action.2013-10-18_05");
        logFile.setAbsolutePath("/logTest/Prod-gcsvc1/2013/10/18/action/giftco-service-action.2013-10-18_05.log");
        logFile.setLogType("action");
        logFile.setIsAnalyzed(LogFile.IsAnalyzed.Y);
        logFile.setIsDecomposed(LogFile.IsDecomposed.Y);
        return logFile;
    }

    private Project mockProject() {
        Project project = new Project();
        project.setName("giftco-service");
        return project;
    }

    private Server mockServer(int projectId) {
        Server server = new Server();
        server.setProjectId(projectId);
        server.setServerName("Prod-gcsvc1");
        return server;
    }

    @Inject
    public void setActionLogDetailService(ActionLogDetailService actionLogDetailService) {
        this.actionLogDetailService = actionLogDetailService;
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
