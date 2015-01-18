package gaa.gitdownloader;

import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitFile;
import gaa.model.ProjectInfo;

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import com.jcabi.github.Github;
import com.jcabi.github.Github.Time;
import com.jcabi.github.RtGithub;
import com.jcabi.http.Request;

public class GitDownloader {
	public static void main(String[] args) throws Exception {

		//		RepositoryBuilder builder = new RepositoryBuilder();
		//		Repository repository = builder
		//				.setGitDir(new File("C:\\Users\\Guilherme\\git\\gitresearch\\.git"))
		//				.readEnvironment()
		//				.findGitDir()
		//				.build();
		Github github = new RtGithub("asergufmg", "aserg.ufmg2009");
		String query = "language:Java repo:gavelino/gitresearch";
//		String query = "language:Java repo:junit-team/junit";
//		String query = "language:Java";
		Request request = github.entry()
				.uri().path("/search/repositories")
				//				.queryParam("q", "language:Java created:<=2014-06-01")
				.queryParam("q", query )
								.queryParam("sort", "stars")
								.queryParam("order", "desc")
								.queryParam("per_page", "2")
				.back()
				.method(Request.GET);


		List<ProjectInfo> projectsInfo = new GitProjectFinder().findRepos(request, query);
		for (ProjectInfo projectInfo : projectsInfo) {
			System.out.println("Clonando " + projectInfo.getName());
			GitServiceImpl s = new GitServiceImpl();
			Repository repository = s.cloneIfNotExists("tmp/"+projectInfo.getName(), projectInfo.getCloneUrl(), projectInfo.getDefault_branch());
			System.out.println("Clonou");
			Iterable<RevCommit> logs = new Git(repository).log()
	                .call();
	        int count = 0;
	        Date lastCommitDate = null;
	        for (RevCommit rev : logs) {
	            //System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
	            count++;
	            if (lastCommitDate == null || lastCommitDate.compareTo(rev.getCommitterIdent().getWhen())<0)
	            	lastCommitDate = rev.getCommitterIdent().getWhen();
	            
	        }
	        System.out.println("Had " + count + " commits overall in repository "+lastCommitDate);
	        projectInfo.setCommits_count(count);
	        projectInfo.setLastCommit(lastCommitDate);
		}
		DownloaderUtil.persistProjects(projectsInfo);
		
//		System.out.println(" Percorrendo repositorios clonados ");
//		projectsInfo = null;
//		projectsInfo = DownloaderUtil.getProjects();
//		
//		for (Entry<String, List<CommitFile>> entry : DownloaderUtil.getCommitFiles(projectsInfo).entrySet()) {
//			System.out.println("\nRepositorio "+ entry.getKey());
//			for (CommitFile cf : entry.getValue()) {
//				System.out.println(cf);
//			}
//		}
	}

	
	static void printDiff(Repository repository, String oldHead, String head) throws AmbiguousObjectException, IOException, GitAPIException{

		// the diff works on TreeIterators, we prepare two for the two branches
		AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, oldHead);
		AbstractTreeIterator newTreeParser = prepareTreeParser(repository, head);

		// then the procelain diff-command returns a list of diff entries
		List<DiffEntry> diff = new Git(repository).diff().
				setOldTree(oldTreeParser).
				setNewTree(newTreeParser).
				setPathFilter(PathFilter.create("README.md")).
				call();
		for (DiffEntry entry : diff) {
			System.out.println("Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId());
			DiffFormatter formatter = new DiffFormatter(System.out);
			formatter.setRepository(repository);
			formatter.format(entry);
		}

		repository.close();
	}
	private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException,
	MissingObjectException,
	IncorrectObjectTypeException {
		// from the commit we can build the tree which allows us to construct the TreeParser
		RevWalk walk = new RevWalk(repository);
		RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
		RevTree tree = walk.parseTree(commit.getTree().getId());

		CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
		ObjectReader oldReader = repository.newObjectReader();
		try {
			oldTreeParser.reset(oldReader, tree.getId());
		} finally {
			oldReader.release();
		}

		walk.dispose();

		return oldTreeParser;
	}
	static void printDiff2(Repository repository, ObjectId oldHead, ObjectId head) throws AmbiguousObjectException, IOException, GitAPIException{

		System.out.println("Printing diff between tree: " + oldHead + " and " + head);

		// prepare the two iterators to compute the diff between
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
		oldTreeIter.reset(reader, oldHead);
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, head);

		// finally get the list of changed files
		List<DiffEntry> diffs= new Git(repository).diff()
				.setNewTree(newTreeIter)
				.setOldTree(oldTreeIter)
				.call();
		for (DiffEntry entry : diffs) {
			System.out.println("Entry: " + entry);
		}
		System.out.println("Done");

		repository.close();
	}	
	static void printDiff3(Repository repository) throws AmbiguousObjectException, IOException, GitAPIException{
		ObjectId oldHead = repository.resolve("HEAD^^^^{tree}");
		ObjectId head = repository.resolve("HEAD^{tree}");
		System.out.println("Printing diff between tree: " + oldHead + " and " + head);		// prepare the two iterators to compute the diff between
		ObjectReader reader = repository.newObjectReader();
		CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
		oldTreeIter.reset(reader, oldHead);
		CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
		newTreeIter.reset(reader, head);

		// finally get the list of changed files
		List<DiffEntry> diffs= new Git(repository).diff()
				.setNewTree(newTreeIter)
				.setOldTree(oldTreeIter)
				.setShowNameAndStatusOnly(true)
				.call();
		for (DiffEntry entry : diffs) {
			System.out.println("Entry: " + entry);
		}
		System.out.println("Done");

		repository.close();
	}

}
