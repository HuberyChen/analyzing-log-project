package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.APIHost;
import com.quidsi.log.analyzing.domain.APILog;
import com.quidsi.log.analyzing.domain.SystemAPI;

@Service
public class DataConver {

	public APILog dataConverToApiLog(File log, APIHost apiHost) {
		String logName = log.getName();
		String absolutePath = log.getAbsolutePath();
		String apiName = apiHost.getApiName();
		String hostName = apiHost.getHostName();
		APILog apiLog = new APILog();
		if (logName.endsWith(".gz")) {
			logName = logName.replace(".gz", "");
		}
		if (logName.contains(APILog.LOG_TYPE)) {
			apiLog.setLogType(APILog.LOG_TYPE);
		}
		apiLog.setLogName(logName.replace(".log", ""));
		apiLog.setApiName(apiName);
		apiLog.setHostName(hostName);
		apiLog.setAbsolutePath(absolutePath);
		return apiLog;
	}

	public APIHost dataConverToApiHost(String apiName, String hostName) {
		APIHost apiHost = new APIHost();
		apiHost.setApiName(apiName);
		apiHost.setHostName(hostName);
		return apiHost;
	}

	public SystemAPI dataConverToSystemAPI(String apiName) {
		SystemAPI systemAPI = new SystemAPI();
		systemAPI.setApiName(apiName);
		return systemAPI;
	}

	public Date dataConverToDate(String dateMessage) {
		final int size = 6;
		String[] date = dateMessage.split("-");
		int[] dateTime;
		dateTime = new int[size];
		for (int i = 0; i < size; i++) {
			dateTime[i] = Integer.parseInt(date[i]);
		}
		return DateUtils.date(dateTime[0], dateTime[1], dateTime[2],
				dateTime[3], dateTime[4], dateTime[5]);
	}

	public String dataConverToString(Date date) {
		StringBuilder current = new StringBuilder();
		current.append(DateUtils.getYear(date)).append("-");
		current.append(DateUtils.getMonth(date)).append("-");
		current.append(DateUtils.getDay(date));
		return current.toString();
	}
}