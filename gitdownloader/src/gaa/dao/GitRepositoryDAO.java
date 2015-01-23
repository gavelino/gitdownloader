package gaa.dao;

import java.util.List;

import gaa.model.GitRepository;
import gaa.model.ProjectInfo;


public class GitRepositoryDAO extends GenericDAO<GitRepository> {
	
	
	@Override
	public void persist(GitRepository project) {
		super.persist(project);
	}

	@Override
	public GitRepository find(Object id) {
		return this.em.find(GitRepository.class, id);
	}
	
	@Override
	public List<GitRepository> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(GitRepository.class);
	}
	
	@Override
	public void merge(GitRepository o) {
		GitRepository gitRepo = this.find(o.getRepositoryName());
		if (gitRepo != null){
			gitRepo.setCommits(o.getCommits());
			super.merge(gitRepo);
		}
		else
			super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}
}
