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

import com.quidsi.log.analyzing.dao.SystemLogRecordDao;
import com.quidsi.log.analyzing.domain.SystemLogRecord;
import com.quidsi.log.analyzing.utils.FileScanUtils;
import com.quidsi.log.analyzing.utils.UnFileFactory;

@Service
public class SystemLogRecordService {

    private final Logger logger = LoggerFactory.getLogger(SystemLogRecordService.class);

    private SystemLogRecordDao systemLogRecordDao;
    private DataConver dataConver;

    @Transactional
    public int save(SystemLogRecord record) {
        return systemLogRecordDao.save(record);
    }

    @Transactional
    public void saveList(List<SystemLogRecord> records) {
        if (!CollectionUtils.isEmpty(records)) {
            for (SystemLogRecord record : records) {
                systemLogRecordDao.save(record);
            }
        }
    }

    public List<File> scanGzLogs(String path, Date date, String system, String host) {
        List<String> pathFilters = initializeActionPathFilters(system, host);
        List<String> nameFilters = new ArrayList<>();
        nameFilters.add("[\\S]*.gz[\\S]*");
        return scanLogsWithType(path, date, pathFilters, nameFilters);
    }

    public List<File> scanDecompressionLogs(String path, Date date, String system, String host) {
        List<String> pathFilters = initializeActionPathFilters(system, host);
        pathFilters.add("[\\S]*decompression[\\S]*");
        List<String> nameFilters = new ArrayList<>();
        return scanLogsWithType(path, date, pathFilters, nameFilters);
    }

    public void decompression(List<File> uncompressedLogs) {
        if (!CollectionUtils.isEmpty(uncompressedLogs)) {
            for (File log : uncompressedLogs) {
                UnFileFactory.unGz(log);
            }
        }
    }

    @SuppressWarnings("resource")
    public List<SystemLogRecord> logRead(File file, String system, String host, int logId) {
        List<SystemLogRecord> records = new ArrayList<>();

        String str = "";
        InputStreamReader inputReader = null;
        BufferedReader bufferReader = null;

        try (InputStream inputStream = new FileInputStream(file)) {
            inputReader = new InputStreamReader(inputStream);
            bufferReader = new BufferedReader(inputReader);

            while ((str = bufferReader.readLine()) != null) {
                String[] messages = str.split("\\|");
                records.add(dataConver.dataConverToRecord(messages, system, host, logId));
            }
        } catch (Exception e) {
            logger.error("");
        }

        return records;
    }

    private List<String> initializeActionPathFilters(String system, String host) {
        List<String> pathFilters = new ArrayList<>();
        pathFilters.add("[\\S]*" + system.toLowerCase() + "[\\S]*");
        pathFilters.add("[\\S]*" + host.toLowerCase() + "[\\S]*");
        return pathFilters;
    }

    private List<File> scanLogsWithType(String path, Date date, List<String> pathFilters, List<String> nameFilters) {
        String dataFilter = dataConver.dataConverToString(date);
        if (null == dataFilter) {
            dataFilter = "\\S*";
        }

        nameFilters.add("\\D*." + dataFilter + "_\\d*\\.\\D*");
        nameFilters.add("[\\S]*action[\\S]*");
        return FileScanUtils.scan(path, pathFilters, nameFilters);
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
