package com.quidsi.log.analyzing.web.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hubery.chen
 */
@XmlRootElement(name = "detail-show-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailShowRequest {

    @XmlElement(name = "date")
    private String date;

    @XmlElement(name = "project")
    private String project;

    @XmlElement(name = "instance")
    private String serverName;


    @XmlElement(name = "offset")
    private int offset;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
