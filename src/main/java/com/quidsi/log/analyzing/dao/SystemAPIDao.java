package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.SystemAPI;

@Repository
public class SystemAPIDao {

    private JPAAccess jpaAccess;

    public int save(SystemAPI systemAPI) {
        jpaAccess.save(systemAPI);
        return systemAPI.getId();
    }

    public List<SystemAPI> getSystemAPIs() {
        return jpaAccess.find("from " + SystemAPI.class.getName(), null);
    }

    public SystemAPI getSystemAPIByName(String apiName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("APIName", apiName);
        sql.append("from ").append(SystemAPI.class.getName()).append(" where APIName = :APIName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
