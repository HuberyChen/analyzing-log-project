package com.quidsi.log.analyzing.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "System_API_List")
public class SystemAPI {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "APIName")
    private String apiName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
