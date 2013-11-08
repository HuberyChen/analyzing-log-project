package com.quidsi.log.analyzing.web.response;

import java.util.ArrayList;
import java.util.List;

public class InstanceDetail {

    private int projectId;

    private String projectName;

    private List<String> servers = new ArrayList<>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<String> getServers() {
        return servers;
    }

}
