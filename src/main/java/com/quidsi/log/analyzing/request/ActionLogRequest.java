package com.quidsi.log.analyzing.request;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "action-log-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionLogRequest {

    @NotNull(message = "path is required.")
    @XmlElement(name = "path")
    private String path;

    @NotNull(message = "logDate is required.")
    @XmlElement(name = "log-date")
    private Date logDate;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
}
