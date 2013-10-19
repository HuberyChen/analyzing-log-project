package com.quidsi.log.analyzing.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.AnalyzedLogDao;
import com.quidsi.log.analyzing.domain.AnalyzedLog;

@Service
public class AnalyzedLogService {

	private AnalyzedLogDao analyzedLogDao;

	@Transactional
	public int save(AnalyzedLog log) {
		return analyzedLogDao.save(log);
	}

	@Inject
	public void setAnalyzedLogDao(AnalyzedLogDao analyzedLogDao) {
		this.analyzedLogDao = analyzedLogDao;
	}

}
