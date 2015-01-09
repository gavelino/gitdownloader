package gaa.gitdownloader.dao;

import java.util.List;

import gaa.gitdownloader.model.ProjectGit;


public class ProjectDAO extends GenericDAO<ProjectGit> {
	
	
	@Override
	public void persist(ProjectGit project) {
		super.persist(project);
	}

	@Override
	public ProjectGit find(Object id) {
		return this.em.find(ProjectGit.class, id);
	}
	
	@Override
	public List<ProjectGit> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(ProjectGit.class);
	}

}
