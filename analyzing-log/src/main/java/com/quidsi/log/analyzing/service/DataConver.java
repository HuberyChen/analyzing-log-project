package com.quidsi.log.analyzing.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.domain.AnalyzedLog;
import com.quidsi.log.analyzing.domain.SystemLogRecord;

@Service
public class DataConver {

	public AnalyzedLog dataConverToAnalyzedLog(String logName) {
		AnalyzedLog log = new AnalyzedLog();
		log.setAnalyzingDate(new Date());
		log.setLogName(logName);
		return log;
	}

	public SystemLogRecord dataConverToRecord(String[] messages, String system,
			String host) {
		SystemLogRecord record = new SystemLogRecord();
		record.setSystem(system);
		record.setHost(host);
		record.setLogTime(dataConverToDate(messages[0]));
		record.setStatus(messages[1]);
		record.setInterfaceName(messages[3]);
		record.setElapsedTime(Convert.toInt(messages[4].replace(" ", ""), 0));
		record.setRequestMethod(messages[7]);
		record.setErrorCode(messages[8]);
		record.setExceptionMsg(messages[9]);
		record.setLogAddress(messages[11]);
		return record;
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
}
