package com.quidsi.log.analyzing.request;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action-log-analyzing-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionLogAnalyzingRequest {

    @XmlElement(name = "root")
    private String root;

    @XmlElement(name = "date")
    private Date date;

    @XmlElement(name = "project")
    private String projectName;

    @XmlElement(name = "server")
    private String serverName;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
}
