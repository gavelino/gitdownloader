package gaa.gitlogextractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import gaa.dao.CommitInfoDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitInfo;
import gaa.model.LogCommitFileInfo;
import gaa.model.ProjectInfo;

public class GitLogerExtractor {
	private String path;
	
	public GitLogerExtractor(String path) {
		this.path = path;
	}
	
	public static void main(String[] args) throws IOException {
		GitLogerExtractor gitLogerExtractor = new GitLogerExtractor("C:/temp/_commitfileinfo/");
		gitLogerExtractor.extract();
	}
	
	public void extract() throws IOException{
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		CommitInfoDAO ciDAO =  new CommitInfoDAO();
		List<ProjectInfo> projects =  piDAO.findAll(ProjectInfo.class);
		for (ProjectInfo projectInfo : projects) {
			System.out.println(projectInfo.getFullName()+": Extracting logCommitFiles...");
			String fileName = projectInfo.getFullName().replace('/', '-')+".txt";
			BufferedReader br = new BufferedReader(new FileReader(path+fileName));
			String sCurrentLine;
			String[] values;
			List<LogCommitFileInfo> logCommitFiles = new ArrayList<LogCommitFileInfo>();
			Map<String, CommitInfo> commitsInfoMap =  new HashMap<String, CommitInfo>();
			
			while ((sCurrentLine = br.readLine()) != null) {
				CommitInfo commitInfo;
				values = sCurrentLine.split(";");
				String commitSha = values[0];
				if (commitsInfoMap.containsKey(commitSha))
					commitInfo = commitsInfoMap.get(commitSha);
				else{
					commitInfo = ciDAO.find(projectInfo.getFullName(), commitSha);
					commitInfo.setLogCommitFiles(new ArrayList<LogCommitFileInfo>());
					commitsInfoMap.put(commitSha, commitInfo);
				}					
				commitInfo.getLogCommitFiles().add(new LogCommitFileInfo(values[1], values[2], values[3]));				
			}
			System.out.println(projectInfo.getFullName()+": Persisting logCommitFiles...");
			for (CommitInfo commit : commitsInfoMap.values()) {
				ciDAO.update(commit);
			}
		}
	}
}
