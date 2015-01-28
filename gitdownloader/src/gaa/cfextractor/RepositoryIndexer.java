package gaa.cfextractor;

import gaa.dao.CommitInfoDAO;
import gaa.dao.CommitFileDAO;
import gaa.dao.GitRepositoryDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.gitdownloader.DownloaderUtil;
import gaa.gitdownloader.GitProjectFinder;
import gaa.model.CommitInfo;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;
import gaa.model.ProjectStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RepositoryIndexer {
	public static void main(String[] args) throws Exception {
		System.out.println(new Date());

		CommitInfoDAO ciDAO = new CommitInfoDAO();
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		List<ProjectInfo> projects = piDAO.findAll(null);
//		GitRepositoryDAO repDAO = new GitRepositoryDAO();
//		Map<String, ProjectInfo> mapProjects =  new HashMap<String, ProjectInfo>();
//		for (ProjectInfo projectInfo : projects) {
//			mapProjects.put(projectInfo.getFullName(), projectInfo);
//		}
//		projects = null;
//		System.out.println(new Date());
//		List<CommitInfo> commits = new CommitInfoDAO().findAllOrderByRepositoryName();
//		System.out.println(new Date());
//		CommitInfo lastCommit = commits.iterator().next();
//		GitRepository repository = new GitRepository(mapProjects.get(lastCommit.getRepositoryName()), new ArrayList<CommitInfo>());
//		repository.getCommits().add(lastCommit);
//		while (commits.iterator().hasNext()){
//			CommitInfo newCommit = commits.iterator().next();
//			if (newCommit.getRepositoryName().equals(lastCommit.getRepositoryName()))
//				repository.getCommits().add(newCommit);
//			else{
//				repDAO.persist(repository);
//				repository = new GitRepository(mapProjects.get(newCommit.getRepositoryName()), new ArrayList<CommitInfo>());
//				repository.getCommits().add(newCommit);
//			}
//			lastCommit = newCommit;	
//			
//		}
		for (ProjectInfo projectInfo : projects) {
			projectInfo.setNumAuthors(ciDAO.getNumberAuthors(projectInfo.getFullName()));
			System.out.println(projectInfo.getFullName() + " = "+projectInfo.getNumAuthors());
			piDAO.update(projectInfo);
		}
		
		System.out.println(new Date());
	}		
}
