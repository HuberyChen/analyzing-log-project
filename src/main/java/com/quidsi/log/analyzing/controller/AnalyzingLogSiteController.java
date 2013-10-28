package com.quidsi.log.analyzing.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quidsi.core.platform.web.site.SiteController;

@Controller
public class AnalyzingLogSiteController extends SiteController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String analyzingLog(Map<String, Object> model) {
        return "home";
    }
}
