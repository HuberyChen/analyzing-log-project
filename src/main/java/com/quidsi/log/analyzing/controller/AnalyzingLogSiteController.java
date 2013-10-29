package com.quidsi.log.analyzing.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.service.ProjectService;

@Controller
public class AnalyzingLogSiteController extends SiteController {

    private ProjectService projectService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String analyzingLog(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "home";
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

}
