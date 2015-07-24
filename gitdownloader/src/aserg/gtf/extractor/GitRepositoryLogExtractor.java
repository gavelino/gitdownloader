package aserg.gtf.extractor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.util.CRLFLineReader;

public class GitRepositoryLogExtractor {
	private String pathCommits;
	private String pathCommitFiles;
	
	
	
	
	public GitRepositoryLogExtractor(String pathCommits, String pathCommitFiles) {
		super();
		this.pathCommits = pathCommits;
		this.pathCommitFiles = pathCommitFiles;
	}


	static String path = "/Users/Guilherme/Dropbox/docs/doutorado UFMG/pesquisas/github/dataset/";
	static String repositoryName = "iojs/io.js";
//	static String defaultPath = "/Users/Guilherme/Dropbox/docs/doutorado UFMG/pesquisas/github/dataset/";
	public static void main(String[] args) throws IOException {
		if (args.length>0)
			repositoryName = args[0];
		if (args.length>1)
			path = args[1];
		GitRepositoryLogExtractor gitLoggerExtractor = new GitRepositoryLogExtractor(path+"_commitinfo/",path+"_commitfileinfo/");
		System.out.println("BEGIN at "+ new Date() + "\n\n");
		gitLoggerExtractor.simpleExtract();
		System.out.println("\n\nEND at "+ new Date());
	}
	

	int MAXBUFFER = 100000;
	public void simpleExtract() throws IOException{
		LogCommitDAO lcDAO = new LogCommitDAO();
		int countcfs = 0;
		try{			
			Map<String, LogCommitInfo> mapCommits = new HashMap<String, LogCommitInfo>();
			System.out.println(repositoryName
					+ ": Extracting logCommits...  "+pathCommits);
			String fileName = repositoryName.replace('/', '-')
					+ ".txt";
			BufferedReader br = new BufferedReader(new FileReader(
					pathCommits + fileName));
			CRLFLineReader lineReader = new CRLFLineReader(br);
			String sCurrentLine;
			String[] values;
			while ((sCurrentLine = lineReader.readLine()) != null) {
				values = sCurrentLine.split(";");
				if (values.length<7)
					System.err.println("Erro na linha " + countcfs);
				Date authorDate = !values[3].isEmpty() ? new Timestamp(Long.parseLong(values[3]) * 1000L) : null;
				Date commiterDate = !values[6].isEmpty() ? new Timestamp(Long.parseLong(values[6]) * 1000L) : null;
				String msg = (values.length == 8) ? values[7] : "";

				mapCommits.put(values[0],
						new LogCommitInfo(repositoryName,
								values[0], values[1], values[2],
								authorDate, values[4], values[5],
								commiterDate, msg));
				countcfs++;
			}
			//			if (countcfs >= MAXBUFFER){
			//				System.out.println("Persistindo CommitFilesLog = "+countcfs);
			//				countcfs = 0;
			//				lcDAO.persistAll(logCommits);					
			//				logCommits = new ArrayList<LogCommitFileInfo>();
			//			}
			insertFiles(repositoryName, mapCommits);
			br.close();
			lcDAO.persistAll(mapCommits.values());
			
		}
		catch(Exception e ){
			System.err.format("Error in file %s, line %d\n%s", repositoryName, countcfs, e.getMessage() );
		}
				
			
		
//		if (logCommits.size()>0)
//			lcDAO.persistAll(logCommits);	
		
		
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
		br.close();
		return map;
	}
	
	private void insertFiles(String projectName, Map<String, LogCommitInfo> mapCommit) throws IOException{
		System.out.println(projectName+": Extracting logCommitFiles...");
		String fileName = projectName.replace('/', '-')+".txt";
		BufferedReader br = new BufferedReader(new FileReader(pathCommitFiles+fileName));
		String sCurrentLine;
		String[] values;

		while ((sCurrentLine = br.readLine()) != null) {
			values = sCurrentLine.split(";");
			String sha = values[0];
			mapCommit.get(sha).addCommitFile(new LogCommitFileInfo(values[1], values[2], values[3]));
		}
		br.close();
	}

	public String getPathCommits() {
		return pathCommits;
	}

	public void setPathCommits(String pathCommits) {
		this.pathCommits = pathCommits;
	}

	public String getPathCommitFiles() {
		return pathCommitFiles;
	}

	public void setPathCommitFiles(String pathCommitFiles) {
		this.pathCommitFiles = pathCommitFiles;
	}
}
