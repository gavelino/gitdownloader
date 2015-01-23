package gaa.cfextractor.filter;

import gaa.model.GitRepository;

import java.util.List;

public abstract class ProjectFilter {
	List<GitRepository> projects;
	public ProjectFilter(List<GitRepository> projects) {
		this.projects = projects;
	}
	public abstract List<GitRepository> filter();
	public abstract void persistFilterInformations();
}
