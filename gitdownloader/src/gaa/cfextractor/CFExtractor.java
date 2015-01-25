package gaa.cfextractor;

import gaa.dao.CommitInfoDAO;
import gaa.dao.CommitFileDAO;
import gaa.dao.GitRepositoryDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.gitdownloader.DownloaderUtil;
import gaa.model.CommitInfo;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;
import gaa.model.ProjectStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CFExtractor {
	public static void main(String[] args) throws Exception {
		
		List<ProjectInfo> projectsInfo =  DownloaderUtil.getProjects();
		GitRepositoryDAO grDAO = new GitRepositoryDAO();
		String especificProject = "linux";
		ProjectInfoDAO projectDAO = new ProjectInfoDAO();
		for (ProjectInfo projectInfo : projectsInfo) {
//			if (especificProject !=null && projectInfo.getName().equalsIgnoreCase(especificProject)) {
			if (projectInfo.getStatus() == ProjectStatus.DOWNLOADED) {
//				System.out.println(new Date());
//				GitRepository gitRepository = new GitRepository(projectInfo, DownloaderUtil.getCommits(projectInfo));
//				System.out.println(projectInfo + ": Persisting CommitFiles...");
//				grDAO.merge(gitRepository);
//				System.out.println(projectInfo + ": CommitFiles were persisted");
//				System.out.println(new Date());
				
				try {
					System.out.println(new Date());
					projectInfo.setStatus(ProjectStatus.ANALYZING);
					projectDAO.update(projectInfo);
					System.out.println(projectInfo + ": Persisting CommitFiles...");
					DownloaderUtil.getAndPersistCommitsBlock(projectInfo);
					System.out.println(projectInfo + ": CommitFiles were persisted");
					projectInfo.setStatus(ProjectStatus.ANALYZED);
					projectDAO.update(projectInfo);
					System.out.println(new Date());
				} catch (Exception e) {
					projectInfo.setErrorMsg("CFExtractor error: "+e.toString());
					projectInfo.setStatus(ProjectStatus.ERROR);
					projectDAO.update(projectInfo);
				}
			}
		}
		
		
//		CommitFileDAO cfDAO = new CommitFileDAO();
//		for (ProjectInfo projectInfo : projectsInfo) {
//			for (CommitFile cFile : map.get(projectInfo.getName())) {
//				cFile.setProjectName(projectInfo.getName());
//				cfDAO.merge(cFile);				
//			}
//		}
	}
//	static void printCommit(Map<String, List<CommitFile>> map ){
//		for (Entry<String, List<CommitFile>> entry : map.entrySet()) {
//			for (CommitFile cfiles : entry.getValue()) {
//				System.out.println(cfiles);
//			}
//		}
//	}
}
