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

	@Column(name = "LogName")
	private String logName;

	@Column(name = "AnalyzingDate")
	private Date analyzingDate;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public Date getAnalyzingDate() {
		return analyzingDate;
	}

	public void setAnalyzingDate(Date analyzingDate) {
		this.analyzingDate = analyzingDate;
	}
}
