package gaa.dao;

import java.util.List;

import gaa.model.ProjectInfo;


public class ProjectInfoDAO extends GenericDAO<ProjectInfo> {
	
	
	@Override
	public void persist(ProjectInfo o) {
		ProjectInfo persistedProject = this.em.find(ProjectInfo.class, o.getFullName());
		if (persistedProject != null){
			persistedProject.setCloneUrl(o.getCloneUrl());
			persistedProject.setCommits_count(o.getCommits_count());
			persistedProject.setCreated_at(o.getCreated_at());
			persistedProject.setDefault_branch(o.getDefault_branch());
			persistedProject.setDescription(o.getDescription());
			persistedProject.setError_commits_count(o.getError_commits_count());
			persistedProject.setForks_count(o.getForks_count());
			persistedProject.setLanguage(o.getLanguage());
			persistedProject.setMerge_commits_count(o.getMerge_commits_count());
			persistedProject.setOpen_issues(o.getOpen_issues());
			persistedProject.setPushed_at(o.getPushed_at());
			persistedProject.setQuery(o.getQuery());
			persistedProject.setSize(o.getSize());
			persistedProject.setStargazers_count(o.getStargazers_count());
			persistedProject.setUpdated_at(o.getUpdated_at());
			persistedProject.setWatchers_count(o.getWatchers_count());
			super.persist(persistedProject);
			
			
		}
		else	
			super.persist(o);
	}

	@Override
	public ProjectInfo find(Object id) {
		return this.em.find(ProjectInfo.class, id);
	}
	
	@Override
	public List<ProjectInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(ProjectInfo.class);
	}
	
	@Override
	public void merge(ProjectInfo o) {
		ProjectInfo persistedProject = this.em.find(ProjectInfo.class, o.getFullName());
		if (persistedProject != null){
			persistedProject.setCloneUrl(o.getCloneUrl());
			persistedProject.setCommits_count(o.getCommits_count());
			persistedProject.setCreated_at(o.getCreated_at());
			persistedProject.setDefault_branch(o.getDefault_branch());
			persistedProject.setDescription(o.getDescription());
			persistedProject.setError_commits_count(o.getError_commits_count());
			persistedProject.setForks_count(o.getForks_count());
			persistedProject.setLanguage(o.getLanguage());
			persistedProject.setMerge_commits_count(o.getMerge_commits_count());
			persistedProject.setOpen_issues(o.getOpen_issues());
			persistedProject.setPushed_at(o.getPushed_at());
			persistedProject.setQuery(o.getQuery());
			persistedProject.setSize(o.getSize());
			persistedProject.setStargazers_count(o.getStargazers_count());
			persistedProject.setUpdated_at(o.getUpdated_at());
			persistedProject.setWatchers_count(o.getWatchers_count());
			super.persist(persistedProject);
			
			
		}
		else	
			super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}
}