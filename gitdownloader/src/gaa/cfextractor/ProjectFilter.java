package gaa.cfextractor;

import gaa.model.GitProject;

import java.util.List;

public abstract class ProjectFilter {
	List<GitProject> projects;
	public ProjectFilter(List<GitProject> projects) {
		this.projects = projects;
	}
	public abstract List<GitProject> filter();
	public abstract void persistFilterInformations();
}
