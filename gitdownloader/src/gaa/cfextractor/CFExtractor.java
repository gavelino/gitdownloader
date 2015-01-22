package gaa.cfextractor;

import gaa.dao.CommitFileDAO;
import gaa.dao.GitProjectDAO;
import gaa.gitdownloader.DownloaderUtil;
import gaa.model.GitProject;
import gaa.model.ProjectInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CFExtractor {
	public static void main(String[] args) throws Exception {
		List<ProjectInfo> projectsInfo =  DownloaderUtil.getProjects();
		GitProjectDAO gpDAO = new GitProjectDAO();
		for (ProjectInfo projectInfo : projectsInfo) {
			GitProject gitProject = new GitProject();
			gitProject.setProjectInfo(projectInfo);			
			gitProject.setCommitFiles(DownloaderUtil.getCommitFiles(projectInfo));
			System.out.println(projectInfo+": Persistindo CommitFiles...");
			gpDAO.merge(gitProject);
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
