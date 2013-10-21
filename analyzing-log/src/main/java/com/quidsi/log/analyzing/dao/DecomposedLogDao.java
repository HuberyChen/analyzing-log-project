package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.DecomposedLog;

@Repository
public class DecomposedLogDao {

    private JPAAccess jpaAccess;

    public int save(DecomposedLog log) {
        jpaAccess.save(log);
        return log.getId();
    }

    public DecomposedLog getDecomposedLogByName(String name) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        sql.append("from ").append(DecomposedLog.class.getName()).append(" where name = :name");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }

}
