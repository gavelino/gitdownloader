package gaa.gitdownloader;

import gaa.gitdownloader.dao.ProjectDAO;
import gaa.gitdownloader.model.ProjectGit;
import gaa.prototype.CommitFile;

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

	public static Map<String, List<CommitFile>> getCommitFiles(List<ProjectGit> projectsInfo) throws Exception {
		Map<String, List<CommitFile>> allCommitFiles = new HashMap<String, List<CommitFile>>();
		for (ProjectGit projectInfo : projectsInfo) {
			GitServiceImpl s = new GitServiceImpl();
			Repository repository = s.getClonedRepository("tmp/"+projectInfo.getName(), projectInfo.getDefault_branch());
			RevCommit currentCommit = null;
			RevWalk walk = new RevWalk(repository);
			List<CommitFile> commitFiles =  new ArrayList<CommitFile>();
			try {
				walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
				Iterator<RevCommit> i = walk.iterator();
				int count =0;
				while (i.hasNext()) {
					currentCommit = i.next();
					commitFiles.addAll(getDiff(repository, currentCommit));
					count++;
				}
				System.out.println(projectInfo.getName() + "/"+projectInfo.getDefault_branch()+" = "+count);
			} finally {
				walk.dispose();
			}
			allCommitFiles.put(projectInfo.getName(), commitFiles);
		}
		return allCommitFiles;
	}
	public static List<CommitFile> getDiff(Repository repository,RevCommit commit) throws IncorrectObjectTypeException, IOException{
		List<CommitFile> commitFiles = new ArrayList<CommitFile>();
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
			commitFiles.add(new gaa.prototype.CommitFile(new Timestamp(commit.getAuthorIdent().getWhen().getTime()), 
					diff.getNewPath(), 
					gaa.prototype.Status.getStatus(diff.getChangeType().name()), 
					commit.getAuthorIdent().getName(), 
					commit.getAuthorIdent().getName(), 
					commit.getAuthorIdent().getEmailAddress(), 
					0, 0, 
					commit.getName(), 
					0, 
					commit.getShortMessage()));
			
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
	
	public static void persistProjects(List<ProjectGit> projectsInfo) {
		ProjectDAO projectDAO = new ProjectDAO();
		for (ProjectGit projectGit : projectsInfo) {
			projectDAO.merge(projectGit);
		}

	}
	
	public static List<ProjectGit> getProjects() {
		ProjectDAO projectDAO = new ProjectDAO();
		return projectDAO.findAll(null);
	}
}
