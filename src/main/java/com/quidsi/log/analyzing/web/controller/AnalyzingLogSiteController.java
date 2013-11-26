package com.quidsi.log.analyzing.web.controller;

import com.quidsi.core.platform.web.site.SiteController;
import com.quidsi.core.platform.web.site.cookie.RequireCookie;
import com.quidsi.core.platform.web.site.session.RequireSession;
import com.quidsi.log.analyzing.domain.ActionLogSchedule;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.service.ProjectService;
import com.quidsi.log.analyzing.service.ScheduleService;
import com.quidsi.log.analyzing.service.ServerService;
import com.quidsi.log.analyzing.service.ServiceConstant;
import com.quidsi.log.analyzing.utils.LogReadUtils;
import com.quidsi.log.analyzing.utils.ScanUtils;
import com.quidsi.log.analyzing.web.interceptor.LoginRequired;
import com.quidsi.log.analyzing.web.response.InstanceDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@LoginRequired
@RequireCookie
@RequireSession
@Controller
public class AnalyzingLogSiteController extends SiteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzingLogSiteController.class);

    private String globalPath;
    private ProjectService projectService;
    private ServerService serverService;
    private ScheduleService scheduleService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String analyzingLog(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "home";
    }

    @RequestMapping(value = "/schedule/details", method = RequestMethod.GET)
    public String scheduleDetails(Map<String, Object> model) {
        List<ActionLogSchedule> schedules = scheduleService.getSchedulesIntraday();
        model.put("schedules", schedules);
        return "schedule/schedule";
    }

    @RequestMapping(value = "/schedule/detail", method = RequestMethod.GET)
    public String scheduleDetail(@Valid @RequestParam int scheduleId, Map<String, Object> model) {
        ActionLogSchedule schedule = scheduleService.getScheduleById(scheduleId);
        model.put("messages", schedule.getNote().replace(".", "\n"));
        return "schedule/detail";
    }

    @RequestMapping(value = "/project/instance/details", method = RequestMethod.GET)
    public String instanceDetails(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        if (CollectionUtils.isEmpty(projects)) {
            model.put("instanceDetails", null);
            return "project/project";
        }
        List<InstanceDetail> instanceDetails = new ArrayList<>();
        for (Project project : projects) {
            InstanceDetail instanceDetail = new InstanceDetail();
            instanceDetail.setProject(project);

            List<Server> servers = serverService.getServersByProjectId(project.getId());
            instanceDetail.getServers().addAll(servers);

            instanceDetails.add(instanceDetail);
        }
        model.put("instanceDetails", instanceDetails);
        return "project/project";
    }

    @RequestMapping(value = "/project/instance/log/action/detail", method = RequestMethod.GET)
    public String actionLogDetail(Map<String, Object> model) {
        List<Project> projects = projectService.getProjects();
        model.put("projects", projects);
        return "log/detail";
    }

    @RequestMapping(value = "/project/instance/log/action/showLog", method = RequestMethod.GET)
    public String logShow(@Valid @RequestParam String path, Map<String, Object> model) {
        model.put("log", LogReadUtils.logRead(convertToPath(path)));
        return "log/preview";
    }

    private String convertToPath(String path) {
        String[] folders = path.split("/");

        List<String> nameFilters = new ArrayList<>();
        nameFilters.add(dataMatchAll(folders[folders.length - 1]));

        List<String> realPaths = new ArrayList<>();
        for (int i = 0; i < folders.length - 1; i++) {
            if (folders[i].contains("prod")) {
                getFilePath(folders, i, realPaths, nameFilters);
                break;
            }
        }

        if (CollectionUtils.isEmpty(realPaths)) {
            throw new IllegalStateException("file is not fund");
        }
        String realPath = realPaths.get(0);
        LOGGER.info("log real path is " + realPath);
        return realPath;
    }

    private void getFilePath(String[] folders, int recordPoint, List<String> realPaths, List<String> nameFilters) {
        List<Server> servers = serverService.getServersByServerName(folders[recordPoint]);
        if (CollectionUtils.isEmpty(servers)) {
            throw new IllegalStateException("server not exists");
        }
        for (Server server : servers) {
            getServerDirectory(server, folders, realPaths, nameFilters, recordPoint);
        }
    }

    private void getServerDirectory(Server server, String[] folders, List<String> realPaths, List<String> nameFilters, int recordPoint) {
        List<String> pathFilters = new ArrayList<>();
        Project project = projectService.getProjectById(server.getProjectId());
        pathFilters.add(dataMatchAll(project.getName()));
        pathFilters.add(dataMatchAll(server.getServerName()));
        for (int k = recordPoint + 1; k < folders.length - 1; k++) {
            pathFilters.add(dataMatchAll(folders[k]));
        }
        realPaths.addAll(ScanUtils.scan(globalPath, pathFilters, nameFilters));
    }

    private String dataMatchAll(String data) {
        return ServiceConstant.MATCH_ALL + data + ServiceConstant.MATCH_ALL;
    }

    @Autowired
    public void setGlobalPath(@Value("${portal.path}") String globalPath) {
        this.globalPath = globalPath;
    }

    @Inject
    public void setServerService(ServerService serverService) {
        this.serverService = serverService;
    }

    @Inject
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Inject
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

}
