package com.quidsi.log.analyzing.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "log-information")
@XmlAccessorType(XmlAccessType.FIELD)
public class InformationResponse {

	@XmlElementWrapper(name = "projects")
	@XmlElement(name = "project")
	private List<ProjectInfomation> projects = new ArrayList<>();

	public List<ProjectInfomation> getProjects() {
		return projects;
	}

}
