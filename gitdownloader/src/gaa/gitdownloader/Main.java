package gaa.gitdownloader;

import gaa.gitdownloader.dao.ProjectDAO;
import gaa.gitdownloader.model.ProjectGit;
import gaa.prototype.CommitFile;

import java.io.IOException;
import java.sql.Date;
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
import com.jcabi.github.RtGithub;
import com.jcabi.http.Request;

public class Main {
	public static void main(String[] args) throws Exception {

		//		RepositoryBuilder builder = new RepositoryBuilder();
		//		Repository repository = builder
		//				.setGitDir(new File("C:\\Users\\Guilherme\\git\\gitresearch\\.git"))
		//				.readEnvironment()
		//				.findGitDir()
		//				.build();
		Github github = new RtGithub("asergufmg", "aserg.ufmg2009");
//		String query = "language:Java repo:junit-team/junit";
		String query = "language:Java";
		Request request = github.entry()
				.uri().path("/search/repositories")
				//				.queryParam("language", "java")
				//				.queryParam("user", "gavelino")
				//				.queryParam("q", "language:Java created:<=2014-06-01")
				.queryParam("q", query )
								.queryParam("sort", "stars")
								.queryParam("order", "desc")
								.queryParam("per_page", "200")
				.back()
				.method(Request.GET);


		List<ProjectGit> projectsInfo = new GitProjectFinder().findRepos(request, query);
		DownloaderUtil.persistProjects(projectsInfo);
		for (ProjectGit projectInfo : projectsInfo) {
			System.out.println("Clonando " + projectInfo.getName());
			GitServiceImpl s = new GitServiceImpl();
			Repository repository = s.cloneIfNotExists("tmp/"+projectInfo.getName(), projectInfo.getCloneUrl(), projectInfo.getDefault_branch());
			System.out.println("Clonou");


		}
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
