package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.core.platform.web.site.session.SessionContext;
import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.User;
import com.quidsi.log.analyzing.service.ActionLogDetailService;
import com.quidsi.log.analyzing.service.LogFileService;
import com.quidsi.log.analyzing.service.UserService;
import com.quidsi.log.analyzing.web.SessionConstants;
import com.quidsi.log.analyzing.web.request.DetailShowRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Controller
@RequireCookie
@RequireSession
public class LoginSiteController extends SiteController {

    private SessionContext sessionContext;

    private UserService userService;

    private ActionLogDetailService actionLogAnalyzingService;

    private LogFileService logFileService;

    @RequestMapping(value = "/project/instance/log/action/detail", method = RequestMethod.POST)
    public String actionDetailManagement(@ModelAttribute DetailShowRequest request, Map<String, Object> model) {
        List<LogFile> logFiles = logFileService.getLogFilesByProjectAndServer(request.getProject(), request.getServerName());
        if (CollectionUtils.isEmpty(logFiles)) {
            model.put("actionLogDetails", null);
            return "project/action/detail";
        }
        for (LogFile logFile : logFiles) {
            int totalCount = actionLogAnalyzingService.getTotalCount(request.getDate(), request.getStatus(), logFile.getId());
        }
        return "project/action/detail";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Map<String, Object> model) {
        return "login/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "userName") String name, @RequestParam(value = "password") String password, Map<String, Object> model) {
        User user = userService.getUserByName(name.trim());
        if (null != user && userService.verifyPassword(password.trim(), user.getPassword())) {
            String json = JSONBinder.binder(User.class).toJSON(user);
            sessionContext.set(SessionConstants.LOGGED_IN, Boolean.TRUE);
            sessionContext.set(SessionConstants.USER_DETAILS, json);
            return "redirect:/home";
        }

        model.put("msgType", "error");
        return "login/login";
    }

    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public String signOut(Map<String, Object> model) {
        sessionContext.invalidate();
        return "redirect:/login";
    }

    @Inject
    public void setLogFileService(LogFileService logFileService) {
        this.logFileService = logFileService;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Inject
    public void setActionLogAnalyzingService(ActionLogDetailService actionLogAnalyzingService) {
        this.actionLogAnalyzingService = actionLogAnalyzingService;
    }

}
