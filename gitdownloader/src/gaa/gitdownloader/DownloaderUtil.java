package gaa.gitdownloader;

import gaa.dao.CommitInfoDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitFileInfo;
import gaa.model.CommitInfo;
import gaa.model.ProjectInfo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;

public class DownloaderUtil {
	
	static final String PATH = "F:/tmp/gitrepositories/";

	public static Map<String, List<CommitInfo>> getAllCommits(List<ProjectInfo> projectsInfo) throws Exception {
		Map<String, List<CommitInfo>> allCommitFiles = new HashMap<String, List<CommitInfo>>();
		for (ProjectInfo projectInfo : projectsInfo) {
			allCommitFiles.put(projectInfo.getName(), getCommits(projectInfo));
}
		return allCommitFiles;
	}
	public static List<CommitInfo> getCommits(ProjectInfo projectInfo) throws Exception {
		GitServiceImpl s = new GitServiceImpl();
		Repository repository = s.getClonedRepository(PATH+projectInfo.getName(), projectInfo.getDefault_branch());
		RevCommit currentCommit = null;
		RevWalk walk = new RevWalk(repository);
		List<CommitInfo> commitsInfo =  new ArrayList<CommitInfo>();
		try {
			walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
			Iterator<RevCommit> i = walk.iterator();
			int count =0;
			while (i.hasNext()) {
				currentCommit = i.next();
				commitsInfo.add(new CommitInfo(projectInfo.getFullName(), currentCommit.getName(), 
						   currentCommit.getShortMessage(), 
						   currentCommit.getAuthorIdent().getName(), 
						   currentCommit.getAuthorIdent().getEmailAddress(),
						   new Timestamp(currentCommit.getAuthorIdent().getWhen().getTime()), 
						   getDiff(repository, currentCommit, projectInfo.getFullName())));
				count++;
			}
			System.out.println(projectInfo.getName() + "/"+projectInfo.getDefault_branch()+" = "+count);
		} finally {
			walk.dispose();
		}
		return commitsInfo;
	}
	static int MAXBUFEER = 10000;
	public static void getAndPersistCommitsBlock(ProjectInfo projectInfo) throws Exception {
		GitServiceImpl s = new GitServiceImpl();
		Repository repository = s.getClonedRepository(PATH+projectInfo.getName(), projectInfo.getDefault_branch());
		RevCommit currentCommit = null;
		RevWalk walk = new RevWalk(repository);
		CommitInfoDAO commitDAO = new CommitInfoDAO();
		List<CommitInfo> commitsInfo =  new ArrayList<CommitInfo>();
		try {
			walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
			Iterator<RevCommit> i = walk.iterator();
			int count = 0;
			int countcfs = 0;
			while (i.hasNext()) {
				currentCommit = i.next();
				List<CommitFileInfo> commitFiles = getDiff(repository, currentCommit, projectInfo.getFullName());
				commitsInfo.add(new CommitInfo(projectInfo.getFullName(), currentCommit.getName(), 
						   currentCommit.getShortMessage(), 
						   currentCommit.getAuthorIdent().getName(), 
						   currentCommit.getAuthorIdent().getEmailAddress(),
						   new Timestamp(currentCommit.getAuthorIdent().getWhen().getTime()), 
						   commitFiles));
				countcfs+=commitFiles.size();
//				if (++count%MAXBUFEER == 0){
				if (++countcfs >= MAXBUFEER){
					System.out.println("entrou "+count);
					commitDAO.persistAll(commitsInfo);					
					commitsInfo = new ArrayList<CommitInfo>();
				}
			}
			if (commitsInfo.size()>0)
				commitDAO.persistAll(commitsInfo);	
			System.out.println(projectInfo.getName() + "/"+projectInfo.getDefault_branch()+" = "+count);
		} finally {
			walk.dispose();
		}
	}
	
	
	public static void getAndPersistCommits(ProjectInfo projectInfo) throws Exception {
		GitServiceImpl s = new GitServiceImpl();
		Repository repository = s.getClonedRepository(PATH+projectInfo.getName(), projectInfo.getDefault_branch());
		RevCommit currentCommit = null;
		RevWalk walk = new RevWalk(repository);
		CommitInfoDAO commitDAO = new CommitInfoDAO();
		try {
			walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
			Iterator<RevCommit> i = walk.iterator();
			int count =0;
			while (i.hasNext()) {
				currentCommit = i.next();
				commitDAO.merge(new CommitInfo(projectInfo.getFullName(), currentCommit.getName(), 
						   currentCommit.getShortMessage(), 
						   currentCommit.getAuthorIdent().getName(), 
						   currentCommit.getAuthorIdent().getEmailAddress(),
						   new Timestamp(currentCommit.getAuthorIdent().getWhen().getTime()), 
						   getDiff(repository, currentCommit, projectInfo.getFullName())));
				count++;
			}
			System.out.println(projectInfo.getName() + "/"+projectInfo.getDefault_branch()+" = "+count);
		} finally {
			walk.dispose();
		}
	}
	
	public static List<CommitFileInfo> getDiff(Repository repository,RevCommit commit, String projectName) throws IncorrectObjectTypeException, IOException{
		List<CommitFileInfo> commitFiles = new ArrayList<CommitFileInfo>();
		RevWalk rw = new RevWalk(repository);
//		System.out.println("\nCommit =" + commit.name());
		RevCommit parent = null;
		if (commit.getParentCount() > 0) {
			parent = rw.parseCommit(commit.getParent(0).getId());
		}

		DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
		df.setRepository(repository);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(true);

		List<DiffEntry> diffs;
		if (parent == null)
			diffs = df.scan(new EmptyTreeIterator(),  new CanonicalTreeParser(null, rw.getObjectReader(), commit.getTree()));
		else
			diffs = df.scan(parent.getTree(), commit.getTree());

		for (DiffEntry diff : diffs) {
			CommitFileInfo commitFile = new CommitFileInfo(diff.getOldPath(), diff.getNewPath(), 
					gaa.model.Status.getStatus(diff.getChangeType().name()), 
					diff.getOldId().name(),
					diff.getNewId().name());
//			CommitFileInfo commitFile = new CommitFileInfo(new Timestamp(commit.getAuthorIdent().getWhen().getTime()), 
//					diff.getOldPath(),
//					diff.getNewPath(), 
//					gaa.model.Status.getStatus(diff.getChangeType().name()), 
//					commit.getAuthorIdent().getName(), 
//					commit.getAuthorIdent().getName(), 
//					commit.getAuthorIdent().getEmailAddress(), 
//					0, 0, 
//					commit.getName(),
//					diff.getOldId().name(),
//					diff.getNewId().name(),
//					0, 
//					commit.getShortMessage());
			
//			commitFile.setProjectName(projectName);
//			if (!diff.getNewId().name().equals(diff.getOldId().name()))
//				System.out.format("Diferentes %s %s %s\n",diff.getOldId().name(),diff.getNewId().name(), diff.getChangeType().name());
			commitFiles.add(commitFile);
			
//			System.out.println("changeType=" + diff.getChangeType().name()
//					+ " Mode=" + diff.getOldMode().getBits()
//					+ " Path=" + diff.getOldPath()
//					+ " newMode=" + diff.getNewMode().getBits()
//					+ " newPath=" + diff.getNewPath()
//					//	                 + " id=" + getHash()
//					);
			//	         if (!diff.getOldPath().equalsIgnoreCase(diff.getNewPath()) && (!diff.getChangeType().name().equalsIgnoreCase("ADD")))
			//	        	 System.out.println("\n\nDiferente\n\n");
		}
		rw.release();
		df.release();
		return commitFiles;
	}
	
	public static void persistProjects(List<ProjectInfo> projectsInfo) {
		ProjectInfoDAO projectDAO = new ProjectInfoDAO();
		for (ProjectInfo projectGit : projectsInfo) {
			projectDAO.persist(projectGit);
		}

	}
	
	public static List<ProjectInfo> getProjects() {
		ProjectInfoDAO projectDAO = new ProjectInfoDAO();
		return projectDAO.findAll(null);
	}
}
