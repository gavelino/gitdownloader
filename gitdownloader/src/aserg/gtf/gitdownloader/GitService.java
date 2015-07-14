package aserg.gtf.gitdownloader;

import org.eclipse.jgit.lib.Repository;

import aserg.gtf.model.ProjectInfo;

public interface GitService {

	Repository cloneIfNotExists(ProjectInfo projectInfo) throws Exception;

}
