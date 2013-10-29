package com.quidsi.log.analyzing.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.quidsi.core.database.JPAAccess;
import com.quidsi.log.analyzing.domain.Project;

@Repository
public class ProjectDao {
    private JPAAccess jpaAccess;

    public int save(Project project) {
        jpaAccess.save(project);
        return project.getId();
    }

    public List<Project> getProjects() {
        return jpaAccess.find("from " + Project.class.getName(), null);
    }

    public int getMaxProjectId() {
        return jpaAccess.findUniqueResult("SELECT MAX(Id) FROM " + Project.class.getName(), null);
    }

    public Project getProjectById(int id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Id", id);
        sql.append("from ").append(Project.class.getName()).append(" where Id = :Id");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    public Project getProjectByName(String name) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("Name", name);
        sql.append("from ").append(Project.class.getName()).append(" where Name = :Name");
        return jpaAccess.findUniqueResult(sql.toString(), params);
    }

    @Inject
    public void setJpaAccess(JPAAccess jpaAccess) {
        this.jpaAccess = jpaAccess;
    }
}
