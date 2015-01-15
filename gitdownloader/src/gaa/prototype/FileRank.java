package gaa.prototype;

import gaa.model.CommitFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;


public class FileRank {
	private String fileName;
	private List<CommitFile> commits;
	private Map<String, UserFileRank> mapUserRank;
	private String fileCreateUser;
	private UserFileRank best;
	private boolean needCalculate;
	private boolean wasRemoved;
	private UserFileRank fileAuthor;
	
	public FileRank(String fileName, List<CommitFile> commits) {
		super();
		this.fileName = fileName;
		this.commits = commits;
		this.mapUserRank = new HashMap<String, UserFileRank>();
		this.needCalculate = true;
		this.wasRemoved = false;
	}

	public String getFileName() {
		return fileName;
	}
	protected void addCommit(CommitFile commit){
		this.commits.add(commit);
		this.needCalculate = true;
	}
	public UserFileRank getOwner(){
		if (needCalculate)
			calculateRank();
		if (wasRemoved)
			return null;
		return best;
	}
	
	public UserFileRank getFileAuthor(){
		if (needCalculate)
			calculateRank();
		boolean flag = true;
		for (CommitFile commitFile : commits) {
			if (commitFile.getStatus()==Status.ADDED && flag){ 
				this.fileAuthor = mapUserRank.get(commitFile.getLogin());
				flag = false;
			}
			else if (commitFile.getStatus()==Status.RENAMED)
				this.fileAuthor = mapUserRank.get(commitFile.getLogin());
			else if (commitFile.getStatus()==Status.REMOVED)
				fileAuthor = null;
		}
		return fileAuthor;
	}

	private void calculateRank() {
//		testPrintCommits(commits);
		Collections.sort(commits);
//		System.out.println("\n\n Depois \n\n");
//		testPrintCommits(commits);
		
		for (CommitFile commitFile : commits) {
			if (commitFile.getStatus()==Status.REMOVED) {
				this.wasRemoved = true;
			}
			else{
				UserFileRank userFile = null;
				if (mapUserRank.containsKey(commitFile.getLogin()))
					userFile = mapUserRank.get(commitFile.getLogin());
				else {
					userFile = new UserFileRank(fileName, commitFile.getLogin());
					mapUserRank.put(commitFile.getLogin(), userFile);
				}
				if (commitFile.getStatus() == Status.ADDED
						|| commitFile.getStatus() == Status.RENAMED) {
					clear();
					fileCreateUser = userFile.getUser();
					userFile.setValue(1.0f);
					fileAuthor = userFile;
				}
				if (commitFile.getStatus() == Status.MODIFIED) {
					userFile.incValue(0.5f);
					if (fileCreateUser != null) {
						if (!commitFile.getLogin().equals(fileCreateUser))
							mapUserRank.get(fileCreateUser).decValue(0.1f);
					} else
						System.out
								.println("\n\nMuito errado! Analizar porque commit Added nao esta antes\n"
										+ commitFile.getNewFileName()
										+ commitFile.getSha()
										+ commitFile.getStatus() + "\n");
				}
				if (best == null || best.getValue() < userFile.getValue())
					best = userFile;
			} 
		}
		needCalculate = false;
	}

	private void testPrintCommits(List<CommitFile> commits2) {
		System.out.println();
		for (CommitFile commitFile : commits2) {
			System.out.println(commitFile.getDate() + " , " + commitFile.getCommitId() + " , " + commitFile.getNewFileName() + " , " + commitFile.getStatus());
		}
		System.out.println();
		
	}

	private void clear() {
		for (Entry<String, UserFileRank> entry : mapUserRank.entrySet()) {
			entry.getValue().setValue(0.0f);
		}
		
	}
	
	
}
