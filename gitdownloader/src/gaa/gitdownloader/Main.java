package gaa.gitdownloader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonValue;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.Request;
import com.jcabi.http.response.JsonResponse;

public class Main {
	public static void main(String[] args) throws Exception {
		
//		RepositoryBuilder builder = new RepositoryBuilder();
//		Repository repository = builder
//				.setGitDir(new File("C:\\Users\\Guilherme\\git\\gitresearch\\.git"))
//				.readEnvironment()
//				.findGitDir()
//				.build();
		Github github = new RtGithub("asergufmg", "aserg.ufmg2009");
		Request request = github.entry()
				.uri().path("/search/repositories")
				.queryParam("q", "language:Java created:<=2014-06-01")
				.queryParam("sort", "stars")
				.queryParam("order", "desc")
				.queryParam("per_page", "5").back()
				.method(Request.GET);
		
		List<ProjectGit> projectsInfo = new GitProjectFinder().findRepos(request);
		for (ProjectGit projectInfo : projectsInfo) {
			System.out.println("Clonando " + projectInfo.getName());
			GitServiceImpl s = new GitServiceImpl();
			Repository repository = s.cloneIfNotExists("tmp/"+projectInfo.getName(), projectInfo.getCloneUrl(), projectInfo.getDefault_branch());
			System.out.println("Clonou");
			
			RevCommit currentCommit = null;
			RevWalk walk = new RevWalk(repository);
			try {
				walk.markStart(walk.parseCommit(repository.resolve("HEAD")));
				Iterator<RevCommit> i = walk.iterator();
				while (i.hasNext()) {
					currentCommit = i.next();
					if (currentCommit.getParentCount() == 1) {
						//checkoutCommand(git, currentCommit);
						//currentCommit.get
						System.out.println(currentCommit.getId().getName());
					}
				}

			} finally {
				walk.dispose();
			}
		}
		
	}

//	private void checkoutCommand(Git git, RevCommit commit) throws Exception {
//		CheckoutCommand checkout = git.checkout().setStartPoint(commit).setName(commit.getId().getName());
//		checkout.call();		
//	}

}
