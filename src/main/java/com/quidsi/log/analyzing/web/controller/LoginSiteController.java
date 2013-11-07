package com.quidsi.log.analyzing.web.controller;

import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quidsi.core.json.JSONBinder;
import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.core.platform.web.site.session.SessionContext;
import com.quidsi.log.analyzing.domain.User;
import com.quidsi.log.analyzing.service.UserService;
import com.quidsi.log.analyzing.web.SessionConstants;

@Controller
@RequireCookie
@RequireSession
public class LoginSiteController extends SiteController {

    private SessionContext sessionContext;

    private UserService userService;

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
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

}
