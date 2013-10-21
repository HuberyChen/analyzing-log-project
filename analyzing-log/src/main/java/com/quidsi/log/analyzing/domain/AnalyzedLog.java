package com.quidsi.log.analyzing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Analyzed_Log_List")
public class AnalyzedLog {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private int Id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Type")
    private String type;

    @Column(name = "AnalyzedDate")
    private Date analyzedDate;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getAnalyzedDate() {
        return analyzedDate;
    }

    public void setAnalyzedDate(Date analyzedDate) {
        this.analyzedDate = analyzedDate;
    }

}
