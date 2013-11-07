package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.ServerDao;
import com.quidsi.log.analyzing.domain.Server;

@Service
public class ServerService {

    private ServerDao serverDao;

    @Transactional
    public int save(Server server) {
        return serverDao.save(server);
    }

    public List<Server> getServersByServerName(String serverName) {
        return serverDao.getServersByServerName(serverName);
    }

    public Server getServerByProjectIdAndServerName(int projectId, String serverName) {
        return serverDao.getServerByProjectIdAndServerName(projectId, serverName);
    }

    public List<Server> getServersByProjectId(int projectId) {
        return serverDao.getServersByProjectId(projectId);
    }

    public List<Server> getServers() {
        return serverDao.getServers();
    }

    @Inject
    public void setServerDao(ServerDao serverDao) {
        this.serverDao = serverDao;
    }

}
