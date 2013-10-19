package com.quidsi.log.analyzing.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.quidsi.core.util.Convert;
import com.quidsi.core.util.DateUtils;
import com.quidsi.log.analyzing.FileScan;
import com.quidsi.log.analyzing.FileScanDecorator;
import com.quidsi.log.analyzing.FileScanUtils;
import com.quidsi.log.analyzing.UnFileFactory;
import com.quidsi.log.analyzing.dao.SystemLogRecordDao;
import com.quidsi.log.analyzing.domain.AnalyzedLog;
import com.quidsi.log.analyzing.domain.SystemLogRecord;

@Service
public class SystemLogRecordService {

	private final Logger logger = LoggerFactory
			.getLogger(SystemLogRecordService.class);

	private SystemLogRecordDao systemLogRecordDao;

	@Transactional
	public int save(SystemLogRecord record) {
		return systemLogRecordDao.save(record);
	}

	@Transactional
	public void saveList(List<SystemLogRecord> records) {
		systemLogRecordDao.saveList(records);
	}

	public SystemLogRecord getRecordByLogTimeAndHost(Date logTime, String host) {
		return systemLogRecordDao.getRecordByLogTimeAndHost(logTime, host);
	}

	public List<SystemLogRecord> scanLogFilter(String path, String dataFilter) {
		List<String> filters = new ArrayList<>();
		if (null == dataFilter) {
			dataFilter = "\\S*";
		}
		filters.add("\\D*." + dataFilter + "_\\d*.log");
		return logsRead(FileScanUtils.scan(path, filters));
	}

	public void decompression(String SCAN_FOLDER) {
		FileScan fileScan = new FileScan(SCAN_FOLDER, new UnFileFactory());
		FileScanDecorator fsDecorator = new FileScanDecorator();
		fsDecorator.setOperation(fileScan);
		fsDecorator.getTime();
		// TODO Analyzing
		// TODO Save in database
	}

	private List<SystemLogRecord> logsRead(List<File> logs) {
		List<SystemLogRecord> records = new ArrayList<>();
		if (!CollectionUtils.isEmpty(logs)) {
			for (File file : logs) {
				dataConverToAnalyzedLog(file.getName());
				List<SystemLogRecord> logRecords = logRead(file);
				for (SystemLogRecord record : logRecords) {
					records.add(record);
				}
			}
		}
		return records;
	}

	@SuppressWarnings("resource")
	private List<SystemLogRecord> logRead(File file) {
		List<SystemLogRecord> records = new ArrayList<>();

		String pathName = file.getParent();
		String str = "";

		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;

		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);

			while ((str = bufferReader.readLine()) != null) {
				SystemLogRecord record = initializeSetSystemAndHost(pathName);
				String[] messages = str.split("\\|");
				records.add(dataConverToRecord(messages, record));
			}
		} catch (Exception e) {
			logger.error("");
		}

		return records;
	}

	private AnalyzedLog dataConverToAnalyzedLog(String logName) {
		AnalyzedLog log = new AnalyzedLog();
		log.setAnalyzingDate(new Date());
		log.setLogName(logName);
		return log;
	}

	private SystemLogRecord initializeSetSystemAndHost(String pathName) {
		SystemLogRecord record = new SystemLogRecord();
		if (pathName.contains(ServerConstants.GIFTCOSERVER)) {
			record.setSystem(ServerConstants.GIFTCOSERVER);
			if (pathName.contains(HostConstants.GCSVC1.toLowerCase())) {
				record.setHost(HostConstants.GCSVC1);
			}
			if (pathName.contains(HostConstants.GCSVC2.toLowerCase())) {
				record.setHost(HostConstants.GCSVC2);
			}
		}

		if (pathName.contains(ServerConstants.GIFTMESSAGESERVER)) {
			record.setSystem(ServerConstants.GIFTMESSAGESERVER);
			if (pathName.contains(HostConstants.GMSVC1.toLowerCase())) {
				record.setHost(HostConstants.GMSVC1);
			}
			if (pathName.contains(HostConstants.GMSVC2.toLowerCase())) {
				record.setHost(HostConstants.GMSVC2);
			}
		}

		if (pathName.contains(ServerConstants.TAXSERVER)) {
			if (pathName.contains(ServerConstants.VERTEXLOG)) {
				record.setSystem(ServerConstants.VERTEXLOG);
				if (pathName.contains(HostConstants.VTXSVC1.toLowerCase())) {
					record.setHost(HostConstants.VTXLOGVTXSVC1);
				}
				if (pathName.contains(HostConstants.VTXSVC2.toLowerCase())) {
					record.setHost(HostConstants.VTXLOGVTXSVC2);
				}
				if (pathName.contains(HostConstants.VTXSVC3.toLowerCase())) {
					record.setHost(HostConstants.VTXLOGVTXSVC3);
				}
			} else {
				record.setSystem(ServerConstants.TAXSERVER);
				if (pathName.contains(HostConstants.VTXSVC1.toLowerCase())) {
					record.setHost(HostConstants.VTXSVC1);
				}
				if (pathName.contains(HostConstants.VTXSVC2.toLowerCase())) {
					record.setHost(HostConstants.VTXSVC2);
				}
				if (pathName.contains(HostConstants.VTXSVC3.toLowerCase())) {
					record.setHost(HostConstants.VTXSVC3);
				}
			}
		}

		return record;
	}

	private SystemLogRecord dataConverToRecord(String[] messages,
			SystemLogRecord record) {
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

	private Date dataConverToDate(String dateMessage) {
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

	@Inject
	public void setSystemLogRecordDao(SystemLogRecordDao systemLogRecordDao) {
		this.systemLogRecordDao = systemLogRecordDao;
	}

}
