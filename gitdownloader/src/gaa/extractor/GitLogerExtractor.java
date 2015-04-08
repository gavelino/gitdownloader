package gaa.extractor;

import gaa.dao.CommitInfoDAO;
import gaa.dao.LogCommitFileDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitInfo;
import gaa.model.LogCommitFileInfo;
import gaa.model.ProjectInfo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitLogerExtractor {
	private String path;
	
	public GitLogerExtractor(String path) {
		this.path = path;
	}
	
	public static void main(String[] args) throws IOException {
		GitLogerExtractor gitLogerExtractor = new GitLogerExtractor(args[0]);
		System.out.println("BEGIN at "+ new Date() + "\n\n");
		gitLogerExtractor.simpleExtract();
		System.out.println("\n\nEND at "+ new Date());
	}
	
	public void extract() throws IOException{
//		ProjectInfoDAO piDAO = new ProjectInfoDAO();
//		CommitInfoDAO ciDAO =  new CommitInfoDAO();
//		List<ProjectInfo> projects =  piDAO.findAll(ProjectInfo.class);
//		for (ProjectInfo projectInfo : projects) {
//			System.out.println(projectInfo.getFullName()+": Extracting logCommitFiles...");
//			String fileName = projectInfo.getFullName().replace('/', '-')+".txt";
//			BufferedReader br = new BufferedReader(new FileReader(path+fileName));
//			String sCurrentLine;
//			String[] values;
//			List<LogCommitFileInfo> logCommitFiles = new ArrayList<LogCommitFileInfo>();
//			Map<String, CommitInfo> commitsInfoMap =  new HashMap<String, CommitInfo>();
//			
//			while ((sCurrentLine = br.readLine()) != null) {
//				CommitInfo commitInfo;
//				values = sCurrentLine.split(";");
//				String commitSha = values[0];
//				if (commitsInfoMap.containsKey(commitSha))
//					commitInfo = commitsInfoMap.get(commitSha);
//				else{
//					commitInfo = ciDAO.find(projectInfo.getFullName(), commitSha);
//					commitInfo.setLogCommitFiles(new ArrayList<LogCommitFileInfo>());
//					commitsInfoMap.put(commitSha, commitInfo);
//				}					
//				commitInfo.getLogCommitFiles().add(new LogCommitFileInfo(values[1], values[2], values[3]));				
//			}
//			System.out.println(projectInfo.getFullName()+": Persisting logCommitFiles...");
//			for (CommitInfo commit : commitsInfoMap.values()) {
//				ciDAO.updateOnlyLogFiles(commit);
//			}
//		}
	}
	int MAXBUFFER = 100000;
	public void simpleExtract() throws IOException{
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		LogCommitFileDAO lcfDAO = new LogCommitFileDAO();
		List<ProjectInfo> projects =  piDAO.findAll(ProjectInfo.class);
		List<LogCommitFileInfo> logCommitFiles = new ArrayList<LogCommitFileInfo>();
		int countcfs = 0;
		for (ProjectInfo projectInfo : projects) {
			System.out.println(projectInfo.getFullName()+": Extracting logCommitFiles...");
			String fileName = projectInfo.getFullName().replace('/', '-')+".txt";
			BufferedReader br = new BufferedReader(new FileReader(path+fileName));
			String sCurrentLine;
			String[] values;
			
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				logCommitFiles.add(new LogCommitFileInfo(projectInfo.getFullName(), values[0], values[1], values[2], values[3]));	
				countcfs++;
			}
			
			if (countcfs >= MAXBUFFER){
				System.out.println("Persistindo CommitFilesLog = "+countcfs);
				countcfs = 0;
				lcfDAO.persistAll(logCommitFiles);					
				logCommitFiles = new ArrayList<LogCommitFileInfo>();
			}
		}
		if (logCommitFiles.size()>0)
			lcfDAO.persistAll(logCommitFiles);	
		
		
	}
	
	
	static public Map<String, List<LogCommitFileInfo>> extractProject(String localPath, String projectName) throws IOException{
		Map<String, List<LogCommitFileInfo>> map = new HashMap<String, List<LogCommitFileInfo>>();
		List<LogCommitFileInfo> logCommitFiles;
		int countcfs = 0;
		System.out.println(projectName+": Extracting logCommitFiles...");
		String fileName = projectName.replace('/', '-')+".txt";
		BufferedReader br = new BufferedReader(new FileReader(localPath+fileName));
		String sCurrentLine;
		String[] values;

		while ((sCurrentLine = br.readLine()) != null) {
			values = sCurrentLine.split(";");
			String sha = values[0];
			if (map.containsKey(sha))
				logCommitFiles = map.get(sha);
			else{
				logCommitFiles = new ArrayList<LogCommitFileInfo>();
				map.put(sha, logCommitFiles);
			}
			logCommitFiles.add(new LogCommitFileInfo(values[1], values[2], values[3]));
		}
		
		return map;
	}
}
