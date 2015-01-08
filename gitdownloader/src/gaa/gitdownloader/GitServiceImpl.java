package gaa.gitdownloader;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GitServiceImpl implements GitService {

	Logger logger = LoggerFactory.getLogger(GitServiceImpl.class);

	public Repository cloneIfNotExists(String projectPath, String cloneUrl, String branch) throws Exception {
		File folder = new File(projectPath);
		Git git;
		if (folder.exists()) {
			logger.info("Project {} already cloned", cloneUrl);

			RepositoryBuilder builder = new RepositoryBuilder();
			Repository repository = builder
					.setGitDir(new File(folder, ".git"))
					.readEnvironment()
					.findGitDir()
					.build();
			git = new Git(repository);

			git.checkout()
			.setStartPoint(Constants.HEAD)
			.setName(branch)
			.call();
		} else {
			logger.info("Cloning {} ...", cloneUrl);
			git = Git.cloneRepository()
					.setDirectory(folder)
					.setURI(cloneUrl)
					.setCloneAllBranches(true)
					.call();
			logger.info("Done cloning {}", cloneUrl);
		}
		return git.getRepository();
	}
	
	public Repository getClonedRepository(String projectPath, String branch) throws Exception {
		File folder = new File(projectPath);
		Git git;
		if (folder.exists()) {
			RepositoryBuilder builder = new RepositoryBuilder();
			Repository repository = builder
					.setGitDir(new File(folder, ".git"))
					.readEnvironment()
					.findGitDir()
					.build();
			git = new Git(repository);

			git.checkout()
			.setStartPoint(Constants.HEAD)
			.setName(branch)
			.call();
		} else {
			System.err.println("Repositorio n�o clonado: "  + branch );
			return null;
		}
		return git.getRepository();
	}

}
