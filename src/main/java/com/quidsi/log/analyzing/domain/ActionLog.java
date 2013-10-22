package com.quidsi.log.analyzing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Action_Log_List")
public class ActionLog {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "LogId")
    private int logId;

    @Column(name = "Status")
    private String status;

    @Column(name = "OperatedDate")
    private Date operatedDate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOperatedDate() {
        return operatedDate;
    }

    public void setOperatedDate(Date operatedDate) {
        this.operatedDate = operatedDate;
    }
}
