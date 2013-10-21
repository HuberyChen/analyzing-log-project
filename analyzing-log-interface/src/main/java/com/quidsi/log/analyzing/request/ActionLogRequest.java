package com.quidsi.log.analyzing.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class ActionLogRequest {

	@NotNull(message = "path is required.")
	private String path;

	@NotNull(message = "logDate is required.")
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
