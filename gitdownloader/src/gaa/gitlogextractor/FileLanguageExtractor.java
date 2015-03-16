package gaa.gitlogextractor;

import gaa.dao.CommitInfoDAO;
import gaa.dao.FileInfoDAO;
import gaa.dao.LogCommitFileDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitInfo;
import gaa.model.FileInfo;
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
import java.util.Map.Entry;

public class FileLanguageExtractor {
	private String path;
	
	public FileLanguageExtractor(String path) {
		this.path = path;
	}
	
	public static void main(String[] args) throws IOException {
		FileLanguageExtractor gitLogerExtractor = new FileLanguageExtractor("C:/Users/Guilherme/Dropbox/docs/doutorado UFMG/pesquisas/github/dataset/_linguistfiles/");
		System.out.println("BEGIN at "+ new Date() + "\n\n");
		gitLogerExtractor.simpleExtract();
		System.out.println("\n\nEND at "+ new Date());
	}
	
	int MAXBUFFER = 100000;
	public void simpleExtract() throws IOException{
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		FileInfoDAO fiDAO = new FileInfoDAO();
		List<ProjectInfo> projects =  piDAO.findAll(ProjectInfo.class);
		for (ProjectInfo projectInfo : projects) {
			Map<String, List<String>> languageMap = new HashMap<String, List<String>>();
			System.out.format("%s (%s): Extracting file language information...", projectInfo.getFullName(), new Date());
			String fileName = projectInfo.getFullName().replace('/', '-')+".txt";
			BufferedReader br = new BufferedReader(new FileReader(path+fileName));
			String sCurrentLine;
			String[] values;
			
			while ((sCurrentLine = br.readLine()) != null) {
				values = sCurrentLine.split(";");
				String language = values[0];
				String path = values[1];
				List<String> paths;
				if (languageMap.containsKey(language))
					paths = languageMap.get(language);
				else {
					paths = new ArrayList<String>();
					languageMap.put(language, paths);
				}
				paths.add(path);
			}
			
			int count = 0;
			for (Entry<String, List<String>> entry : languageMap.entrySet()) {
				count += fiDAO.updateLanguageFileInfo(projectInfo.getFullName(), entry.getKey(), entry.getValue());
			}
			System.out.println("Arquivos selecionados = "+ count);
			br.close();
			
		}
		
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
