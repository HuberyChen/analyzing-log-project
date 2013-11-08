package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.Server;

@Repository
public class ServerDao {

    private JPAAccess jpaAccess;

    public int save(Server server) {
        jpaAccess.save(server);
        return server.getId();
    }

    public List<Server> getServersByProjectId(int projectId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        sql.append("from ").append(Server.class.getName()).append(" where ProjectId = :ProjectId");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<Server> getServersByServerName(String serverName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ServerName", serverName);
        sql.append("from ").append(Server.class.getName()).append(" where ServerName = :ServerName");
        return jpaAccess.find(sql.toString(), params);
    }

    public List<Server> getServers() {
        return jpaAccess.find("from " + Server.class.getName(), null);
    }

    public Server getServerByProjectIdAndServerName(int projectId, String serverName) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("ProjectId", projectId);
        params.put("ServerName", serverName);
        sql.append("from ").append(Server.class.getName()).append(" where ProjectId = :ProjectId and ServerName = :ServerName");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
