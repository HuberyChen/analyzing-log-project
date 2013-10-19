package com.quidsi.log.analyzing.dao;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.AnalyzedLog;

@Repository
public class AnalyzedLogDao {

	private JPAAccess jpaAccess;

	public int save(AnalyzedLog log) {
		jpaAccess.save(log);
		return log.getId();
	}

	@Inject
	public void setJpaAccess(JPAAccess jpaAccess) {
		this.jpaAccess = jpaAccess;
	}

}
