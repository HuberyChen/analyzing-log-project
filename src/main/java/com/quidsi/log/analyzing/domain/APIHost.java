package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "API_Host_List")
public class APIHost {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "HostName")
    private String hostName;

    @Column(name = "APIName")
    private String apiName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

}
