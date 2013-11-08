package com.quidsi.log.analyzing.web.response;

import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;

import java.util.ArrayList;
import java.util.List;

public class InstanceDetail {

    private Project project;

    private final List<Server> servers = new ArrayList<>();

    public List<Server> getServers() {
        return servers;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
