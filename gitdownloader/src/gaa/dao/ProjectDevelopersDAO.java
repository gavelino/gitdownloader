package gaa.dao;

import gaa.model.GitHubDeveloper;
import gaa.model.LogCommitInfo;
import gaa.model.NewFileInfo;
import gaa.model.ProjectDevelopers;
import gaa.model.ProjectInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectDevelopersDAO extends GenericDAO<ProjectDevelopers>{

	@Override
	public ProjectDevelopers find(Object id) {
		return this.em.find(ProjectDevelopers.class, id);
	}

	@Override
	public boolean exist(ProjectDevelopers entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void persist(ProjectDevelopers o) {
		ProjectDevelopers persistedProjectDevelopers = this.em.find(ProjectDevelopers.class, o.getProjectName());
		if (persistedProjectDevelopers == null)
			super.persist(o);
		else{
			persistedProjectDevelopers.setGitHubDevs(o.getGitHubDevs());
			persistedProjectDevelopers.setNotGitHubDevs(o.getNotGitHubDevs());
			persistedProjectDevelopers.setnCommits(o.getnCommits());
			persistedProjectDevelopers.setnCommitsGitHubAuthor(o.getnCommitsGitHubAuthor());
			persistedProjectDevelopers.setnCommitsGitHubAuthorOrCommitter(o.getnCommitsGitHubAuthorOrCommitter());
			persistedProjectDevelopers.setnGitHubDevs(o.getnGitHubDevs());
			persistedProjectDevelopers.setnNotGitHubDevs(o.getnNotGitHubDevs());
			super.merge(persistedProjectDevelopers);
		}
		
	}

	
	@Override
	public List<ProjectDevelopers> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(ProjectDevelopers.class);
	}
	
	
}
