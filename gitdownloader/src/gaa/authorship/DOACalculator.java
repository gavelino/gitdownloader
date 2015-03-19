package gaa.authorship;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gaa.authorship.model.AuthorshipInfo;
import gaa.authorship.model.Developer;
import gaa.authorship.model.File;
import gaa.authorship.model.Repository;
import gaa.dao.CommitFileDAO;
import gaa.dao.FileInfoDAO;
import gaa.dao.LogCommitFileDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;
import gaa.model.Status;

public class DOACalculator {
	public static void main(String[] args) {
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		List<Repository> repositories = getRepositories(piDAO.findNotFiltered());
	}

	private static List<Repository> getRepositories(List<ProjectInfo> projects) {
		List<Repository> repositories = new ArrayList<>();
		for (ProjectInfo projectInfo : projects) {
			Repository repo = new Repository(projectInfo.getFullName());
			repo.setFiles(getFiles(repo));
			//printRepository(repo);
		}
		
		
		
		return repositories;
	}

	private static void printRepository(Repository repo) {
		System.out.println("Repository: "+repo.getFullName());
		for (File file : repo.getFiles()) {
			printFile(file);
		}
		
	}

	private static void printFile(File file) {
		System.out.println("--File: "+file.getPath());
		for (AuthorshipInfo authorshioinfo : file.getAuthorshipInfos()) {
			printAuthorshipInfo(authorshioinfo);
		}
		
	}

	private static void printAuthorshipInfo(AuthorshipInfo authorshioinfo) {
		System.out.format("---- %s: %b - %d - %d\n", 
				authorshioinfo.getDeveloper().getUserName(),
				authorshioinfo.isFirstAuthor(), 
				authorshioinfo.getnDeliveries(),
				authorshioinfo.getnAcceptances());
		
	}

	private static List<File> getFiles(Repository repository) {
		FileInfoDAO fiDAO =  new FileInfoDAO();
		List<File> files = new ArrayList<>();
		List<String> paths = fiDAO.getPathsOfNotFilteredProjectFiles(repository.getFullName());
		for (String path : paths) {
			File file = new File(path);
			setFileHistory(file, repository);
			files.add(file);
			printFile(file);
		}
		
		return files;
	}

	private static void setFileHistory(File file, Repository repository) {
		LogCommitFileDAO lcfDAO = new LogCommitFileDAO();
		Map<String, AuthorshipInfo> authorshipInfoMap = new HashMap<String, AuthorshipInfo>();
		List<Object[]> logFilesObjectInfo = lcfDAO.getLogCommitFileInfoOrderByDate(repository.getFullName(), file.getPath());
		List<File> renamesBuffer = new ArrayList<File>();
		int numDeliveries = 0;
		for (Object[] objects : logFilesObjectInfo) {
			//ci.name, ci.email, lcfi.oldfilename, lcfi.newfilename, lcfi.status
			AuthorshipInfo authorshipInfo = repository.getAuthorshipInfo((String)objects[0], (String)objects[1], file);
			Status status = Status.getStatus((String)objects[4]);
			
			if (status == Status.ADDED){
				if (authorshipInfo.isFirstAuthor())
					System.err.println("New add for a file that already has fist author! file = "+file.getPath());
				authorshipInfo.setAsFirstAuthor();
			}
			else if (status == Status.MODIFIED){
				authorshipInfo.addNewDelivery();
				file.addNewChange();					
			}
			else if (status == Status.RENAMED){
				File oldFile = new File((String)objects[2]);
				setFileHistory(oldFile, repository);
				renamesBuffer.add(oldFile);
			}
			else System.out.println("Invalid Status");
		}
		
		for (File oldFile : renamesBuffer) {
			file.addRenamedHistory(oldFile);
		}
//		for (AuthorshipInfo authorshipInfo : file.getAuthorshipInfos()) {
//			authorshipInfo.setnAcceptances(numDeliveries-authorshipInfo.getnDeliveries());
//		}
		
		
	}
	
	
	
}
