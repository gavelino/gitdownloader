package gaa.authorship;

import java.util.ArrayList;
import java.util.List;

import gaa.authorship.model.File;
import gaa.authorship.model.Repository;
import gaa.dao.CommitFileDAO;
import gaa.dao.FileInfoDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

public class DOACalculator {
	public static void main(String[] args) {
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		List<Repository> repositories = getRepositories(piDAO.findNotFiltered());
	}

	private static List<Repository> getRepositories(List<ProjectInfo> projects) {
		List<Repository> repositories = new ArrayList<>();
		for (ProjectInfo projectInfo : projects) {
			Repository repo = new Repository(projectInfo.getFullName());
			repo.setFiles(getFiles(repo.getFullName()));
		}
		
		return repositories;
	}

	private static List<File> getFiles(String fullName) {
		FileInfoDAO fiDAO =  new FileInfoDAO();
		List<File> files = new ArrayList<>();
		List<String> paths = fiDAO.getPathsOfNotFilteredProjectFiles(fullName);
		for (String path : paths) {
			File file = new File(path);
			setFileHistory(file, fullName);
			files.add(file);
			
		}
		return files;
	}

	private static void setFileHistory(File file, String repositoryName) {
		CommitFileDAO cfDAO = new CommitFileDAO();
		
		
	}
	
	
	
}
