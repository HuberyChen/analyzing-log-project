package com.quidsi.log.analyzing.web.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectInfomation {

    @XmlElement(name = "name")
    private String project;

    @XmlElementWrapper(name = "Instance")
    @XmlElement(name = "server")
    private final List<ServerInformation> servers = new ArrayList<>();

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<ServerInformation> getServerList() {
        return servers;
    }

}
