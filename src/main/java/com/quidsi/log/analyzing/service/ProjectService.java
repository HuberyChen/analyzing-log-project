package com.quidsi.log.analyzing.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quidsi.log.analyzing.dao.ProjectDao;
import com.quidsi.log.analyzing.domain.Project;

@Service
public class ProjectService {

    private ProjectDao projectDao;

    @Transactional
    public int save(Project project) {
        return projectDao.save(project);
    }

    public Project getProjectById(int id) {
        return projectDao.getProjectById(id);
    }

    public Project getProjectByName(String name) {
        return projectDao.getProjectByName(name);
    }

    public List<Project> getProjects() {
        return projectDao.getProjects();
    }

    @Inject
    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }
}
