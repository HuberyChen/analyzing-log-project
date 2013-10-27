package com.quidsi.log.analyzing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.quidsi.log.analyzing.domain.LogFileWrapper;
import com.quidsi.log.analyzing.domain.Project;
import com.quidsi.log.analyzing.domain.Server;
import com.quidsi.log.analyzing.request.ActionLogAnalyzingRequest;

@Service
public class DataConver {

	private final Logger logger = LoggerFactory.getLogger(DataConver.class);

	private ProjectService projectService;
	private ServerService serverService;

	public List<LogFileWrapper> initializeLogFileWrappers(
			ActionLogAnalyzingRequest request) {

		String projectName = request.getProjectName();
		String serverName = request.getServerName();
		Date date = request.getDate();
		String path = request.getPath();

		if (null == projectName && null != serverName) {
			throw new IllegalStateException("Server can not find the project.");
		}

		if (null != projectName && null == serverName) {
			return getLogFileWrapperWithServerNull(date, path, projectName);
		}

		if (null == projectName && null == serverName) {
			return getLogFileWrapperWithProjectAndServerBothNull(date, path);
		}

		Project project = projectService.getProjectByName(projectName);

		if (null == project) {
			throw new IllegalStateException("Project is not existed");
		}
		Server server = serverService.getServerByProjectIdAndServerName(
				project.getId(), serverName);
		if (null == server) {
			throw new IllegalStateException(String.format(
					"%d does not have instance", project.getName()));
		}
		List<LogFileWrapper> logFileWrappers = new ArrayList<>();
		logFileWrappers.add(new LogFileWrapper(project, server, date, path));
		return logFileWrappers;

	}

	private List<LogFileWrapper> getLogFileWrapperWithProjectAndServerBothNull(
			Date date, String path) {
		List<LogFileWrapper> logFileWrappers = new ArrayList<>();
		List<Project> projects = projectService.getProjects();
		if (CollectionUtils.isEmpty(projects)) {
			throw new IllegalStateException("Projects are not existed");
		}
		for (Project project : projects) {
			List<Server> servers = serverService.getServersByProjectId(project
					.getId());
			if (CollectionUtils.isEmpty(servers)) {
				logger.error("%d does not have instance", project.getName());
				continue;
			}
			for (Server server : servers) {
				logFileWrappers.add(new LogFileWrapper(project, server, date,
						path));
			}
		}

		if (CollectionUtils.isEmpty(logFileWrappers)) {
			throw new IllegalStateException("Servers are not existed");
		}
		return logFileWrappers;
	}

	private List<LogFileWrapper> getLogFileWrapperWithServerNull(Date date,
			String path, String projectName) {
		List<LogFileWrapper> logFileWrappers = new ArrayList<>();
		Project project = projectService.getProjectByName(projectName);
		if (null == project) {
			throw new IllegalStateException("Project is not existed");
		}
		List<Server> servers = serverService.getServersByProjectId(project
				.getId());
		if (CollectionUtils.isEmpty(servers)) {
			throw new IllegalStateException(String.format(
					"%d does not have instance", projectName));
		}
		for (Server server : servers) {
			logFileWrappers
					.add(new LogFileWrapper(project, server, date, path));
		}
		return logFileWrappers;

	}

	@Inject
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	@Inject
	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}
}
