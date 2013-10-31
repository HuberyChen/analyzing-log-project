package com.quidsi.log.analyzing.web.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action-log-analyzing-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionLogAnalyzingRequest {

    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "project")
    private String projectName;

    @XmlElement(name = "instance")
    private String serverName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
