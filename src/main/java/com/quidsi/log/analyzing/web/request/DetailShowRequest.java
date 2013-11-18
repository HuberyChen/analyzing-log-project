package com.quidsi.log.analyzing.web.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author hubery.chen
 */
@XmlRootElement(name = "detail-show-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailShowRequest {

    @XmlElement(name = "date")
    private Date date;

    @XmlElement(name = "project")
    private String project;

    @XmlElement(name = "instance")
    private String serverName;

    @XmlElement(name = "status")
    private String status;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
