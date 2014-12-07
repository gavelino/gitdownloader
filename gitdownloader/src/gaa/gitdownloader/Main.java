package gaa.gitdownloader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws Exception {
		
//		RepositoryBuilder builder = new RepositoryBuilder();
//		Repository repository = builder
//				.setGitDir(new File("C:\\Users\\Guilherme\\git\\gitresearch\\.git"))
//				.readEnvironment()
//				.findGitDir()
//				.build();
		
		System.out.println("Clonando");
		GitServiceImpl s = new GitServiceImpl();
		Repository repository = s.cloneIfNotExists("tmp/gavelino", "https://github.com/gavelino/gitresearch.git", "master");
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

//	private void checkoutCommand(Git git, RevCommit commit) throws Exception {
//		CheckoutCommand checkout = git.checkout().setStartPoint(commit).setName(commit.getId().getName());
//		checkout.call();		
//	}

}
