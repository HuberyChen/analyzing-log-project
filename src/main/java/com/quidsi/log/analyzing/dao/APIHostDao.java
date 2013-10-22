package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.APIHost;

@Repository
public class APIHostDao {

    private JPAAccess jpaAccess;

    public int save(APIHost apiHost) {
        jpaAccess.save(apiHost);
        return apiHost.getId();
    }

    public List<APIHost> getHostsByAPIName(String apiName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("APIName", apiName);
        sql.append("from ").append(APIHost.class.getName()).append(" where APIName = :APIName");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<APIHost> getApiHosts() {
        return jpaAccess.find("from " + APIHost.class.getName(), null);
    }

    public APIHost getHostsByAPIAndHost(String apiName, String hostName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("APIName", apiName);
        params.put("HostName", hostName);
        sql.append("from ").append(APIHost.class.getName()).append(" where APIName = :APIName and HostName = :HostName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
