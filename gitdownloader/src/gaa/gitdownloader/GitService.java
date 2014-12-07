package gaa.gitdownloader;

import org.eclipse.jgit.lib.Repository;

public interface GitService {

	Repository cloneIfNotExists(String folder, String cloneUrl, String branch) throws Exception;

}
