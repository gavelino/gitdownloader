package gaa.cfextractor;

import gaa.dao.CommitFileDAO;
import gaa.dao.GitRepositoryDAO;
import gaa.gitdownloader.DownloaderUtil;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CFExtractor {
	public static void main(String[] args) throws Exception {
		List<ProjectInfo> projectsInfo =  DownloaderUtil.getProjects();
		GitRepositoryDAO grDAO = new GitRepositoryDAO();
		String especificProject = "junit";
		for (ProjectInfo projectInfo : projectsInfo) {
			if (especificProject !=null && projectInfo.getName().equalsIgnoreCase(especificProject)) {
				GitRepository gitRepository = new GitRepository(projectInfo, DownloaderUtil.getCommits(projectInfo));
				System.out.println(projectInfo + ": Persisting CommitFiles...");
				grDAO.merge(gitRepository);
				System.out.println(projectInfo + ": CommitFiles were persisted");

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
