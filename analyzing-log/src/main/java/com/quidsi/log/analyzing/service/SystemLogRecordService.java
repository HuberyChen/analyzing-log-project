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

import com.quidsi.log.analyzing.dao.SystemLogRecordDao;
import com.quidsi.log.analyzing.domain.SystemLogRecord;
import com.quidsi.log.analyzing.util.FileScan;
import com.quidsi.log.analyzing.util.FileScanDecorator;
import com.quidsi.log.analyzing.util.FileScanUtils;
import com.quidsi.log.analyzing.util.UnFileFactory;

@Service
public class SystemLogRecordService {

	private final Logger logger = LoggerFactory
			.getLogger(SystemLogRecordService.class);

	private SystemLogRecordDao systemLogRecordDao;
	private DataConver dataConver;

	public static final String GZ_POSTFIX = ".gz";

	@Transactional
	public int save(SystemLogRecord record) {
		return systemLogRecordDao.save(record);
	}

	@Transactional
	public void saveList(List<SystemLogRecord> records) {
		systemLogRecordDao.saveList(records);
	}

	// TODO ACTION-LOG AND Decompression not include gz
	public List<File> scanDecompressionLogs(String path, Date date,
			String system, String host) {
		return scanLogsWithType(path, date, system, host, null);
	}

	public void decompression(String path) {
		FileScan fileScan = new FileScan(path, new UnFileFactory());
		FileScanDecorator fsDecorator = new FileScanDecorator();
		fsDecorator.setOperation(fileScan);
		fsDecorator.getTime();
	}

	@SuppressWarnings("resource")
	public List<SystemLogRecord> logRead(File file, String system, String host,
			int logId) {
		List<SystemLogRecord> records = new ArrayList<>();

		String str = "";
		InputStreamReader inputReader = null;
		BufferedReader bufferReader = null;

		try (InputStream inputStream = new FileInputStream(file)) {
			inputReader = new InputStreamReader(inputStream);
			bufferReader = new BufferedReader(inputReader);

			while ((str = bufferReader.readLine()) != null) {
				String[] messages = str.split("\\|");
				records.add(dataConver.dataConverToRecord(messages, system,
						host, logId));
			}
		} catch (Exception e) {
			logger.error("");
		}

		return records;
	}

	private List<File> scanLogsWithType(String path, Date date, String system,
			String host, String type) {
		List<String> filters = new ArrayList<>();
		String dataFilter = dataConver.dataConverToString(date);
		if (null == dataFilter) {
			dataFilter = "\\S*";
		}
		filters.add(system);
		filters.add(host);
		filters.add(type);
		filters.add("\\D*." + dataFilter + "_\\d*.log");
		return FileScanUtils.scan(path, filters);
	}

	@Inject
	public void setSystemLogRecordDao(SystemLogRecordDao systemLogRecordDao) {
		this.systemLogRecordDao = systemLogRecordDao;
	}

	@Inject
	public void setDataConver(DataConver dataConver) {
		this.dataConver = dataConver;
	}

}
