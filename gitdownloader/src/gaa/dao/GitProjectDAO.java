package gaa.dao;

import java.util.List;

import gaa.model.GitProject;
import gaa.model.ProjectInfo;


public class GitProjectDAO extends GenericDAO<GitProject> {
	
	
	@Override
	public void persist(GitProject project) {
		super.persist(project);
	}

	@Override
	public GitProject find(Object id) {
		return this.em.find(GitProject.class, id);
	}
	
	@Override
	public List<GitProject> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(GitProject.class);
	}
	
	@Override
	public void merge(GitProject o) {
		super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}
}
