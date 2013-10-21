package com.quidsi.log.analyzing.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Decomposed_Log_List")
public class DecomposedLog {

    @GeneratedValue
    @Id
    @Column(name = "Id")
    private int Id;

    @Column(name = "Name")
    private String name;

    @Column(name = "DecomposedDate")
    private Date DecomposedDate;

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

    public Date getDecomposedDate() {
        return DecomposedDate;
    }

    public void setDecomposedDate(Date decomposedDate) {
        DecomposedDate = decomposedDate;
    }
}
