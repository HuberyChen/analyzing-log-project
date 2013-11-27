package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author hubery.chen
 */
@Entity(name = "Temp_Log_Id")
public class TempId {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int id;

    @Column(name = "LogId")
    private int logId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }
}
