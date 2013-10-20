package com.quidsi.log.analyzing.controller;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quidsi.core.platform.monitor.Track;
import com.quidsi.core.platform.web.site.scheme.HTTPSOnly;
import com.quidsi.log.analyzing.domain.AnalyzedLog;
import com.quidsi.log.analyzing.service.AnalyzedLogService;
import com.quidsi.log.analyzing.service.DataConver;
import com.quidsi.log.analyzing.service.HostConstants;
import com.quidsi.log.analyzing.service.ServerConstants;
import com.quidsi.log.analyzing.service.SystemLogRecordService;

@Controller
@HTTPSOnly
public class SystemLogRecordController {

	private String[] systems = { ServerConstants.GIFTCOSERVER,
			ServerConstants.GIFTMESSAGESERVER, ServerConstants.TAXSERVER,
			ServerConstants.VERTEXLOG };

	private String[] hosts = { HostConstants.GCSVC1, HostConstants.GCSVC2,
			HostConstants.GMSVC1, HostConstants.GMSVC2,
			HostConstants.VTXLOGVTXSVC1, HostConstants.VTXLOGVTXSVC2,
			HostConstants.VTXLOGVTXSVC3, HostConstants.VTXSVC1,
			HostConstants.VTXSVC2, HostConstants.VTXSVC3 };

	private SystemLogRecordService systemLogRecordService;
	private DataConver dataConver;
	private AnalyzedLogService analyzedLogService;

	@RequestMapping(value = "/log/path", method = RequestMethod.GET)
	@ResponseBody
	@Track(warningThresholdInMs = 5000)
	public void LogRead(@Valid @RequestParam String path) {
		for (int i = 0; i < systems.length; i++) {
			String system = systems[i];
			for (int j = 0; j < hosts.length; j++) {
				String host = hosts[j];
				List<File> logs = systemLogRecordService.scanLogFilter(path,
						system, host);
				String logFolder = logs.get(0).getParent();
				systemLogRecordService.decompression(logFolder);
				if (!CollectionUtils.isEmpty(logs)) {
					for (File file : logs) {
						AnalyzedLog analyzedLog = analyzedLogService
								.getAnalyzedLogByName(file.getName());
						if (null != analyzedLog) {
							analyzedLogService.save(dataConver
									.dataConverToAnalyzedLog(file.getName()));
							systemLogRecordService
									.saveList(systemLogRecordService.logRead(
											file, system, host));
						}
					}
				}
			}
		}
	}

	@Inject
	public void setSystemLogRecordService(
			SystemLogRecordService systemLogRecordService) {
		this.systemLogRecordService = systemLogRecordService;
	}

	@Inject
	public void setDataConver(DataConver dataConver) {
		this.dataConver = dataConver;
	}

	@Inject
	public void setAnalyzedLogService(AnalyzedLogService analyzedLogService) {
		this.analyzedLogService = analyzedLogService;
	}

}
