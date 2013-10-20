package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.Map;

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

	public AnalyzedLog getAnalyzedLogByName(String logName) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		params.put("LogName", logName);
		sql.append("from ").append(AnalyzedLog.class.getName())
				.append(" where LogName = :LogName");
		return jpaAccess.findUniqueResult(sql.toString(), params);
	}

	@Inject
	public void setJpaAccess(JPAAccess jpaAccess) {
		this.jpaAccess = jpaAccess;
	}

}
