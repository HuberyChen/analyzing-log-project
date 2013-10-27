package com.quidsi.log.analyzing.service;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.LogFile;
import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.utils.FileFactory;

@Component
public class LogFileOperation {

	private LogFileService logFileService;

	private LogFilesLoader logFilesLoader;

	public void saveLogFilesNotExisted(LogFileWrapper logFileWrapper) {
		List<LogFile> logFilesNotExisted = logFilesLoader.logLoader(
				logFileWrapper).getLogFileNotExisted();
		if (CollectionUtils.isEmpty(logFilesNotExisted)) {
			return;
		}
		logFileService.saveList(logFilesNotExisted);
	}

	public void decompression(LogFileWrapper logFileWrapper) {
		List<LogFile> uncompressionActionLogs = logFileService
				.getUncompressedLogFilesByLogFileWrapper(logFileWrapper);
		if (CollectionUtils.isEmpty(uncompressionActionLogs)) {
			return;
		}
		for (LogFile actionLog : uncompressionActionLogs) {
			String absolutePath = FileFactory.unGz(new File(actionLog
					.getAbsolutePath()));
			actionLog.setIsDecomposed(LogFile.IsDecomposed.Y);
			actionLog.setAbsolutePath(absolutePath);
			logFileService.update(actionLog);
		}
	}

	@Inject
	public void setLogFileService(LogFileService logFileService) {
		this.logFileService = logFileService;
	}

	@Inject
	public void setLogFilesLoader(LogFilesLoader logFilesLoader) {
		this.logFilesLoader = logFilesLoader;
	}

}
