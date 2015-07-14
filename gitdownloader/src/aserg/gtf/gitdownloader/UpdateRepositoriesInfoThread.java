package aserg.gtf.gitdownloader;

import java.io.IOException;
import java.util.List;

import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.FileInfo;
import aserg.gtf.model.LanguageInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.ProjectStatus;

import com.jcabi.github.Github;

public class UpdateRepositoriesInfoThread extends Thread {
	Github github;
	List<ProjectInfo> projects;
	ProjectInfoDAO projectDAO;
	GitServiceImpl gitService;
	public UpdateRepositoriesInfoThread(Github github, List<ProjectInfo> projects) {
		this.github =github;
		this.projects = projects;
		projectDAO = new ProjectInfoDAO();
		gitService = new GitServiceImpl(github);
	}
	@Override
	public void run() {
		for (ProjectInfo projectInfo : projects) {
			FileInfoAux fileAux;
			try {
				fileAux = gitService.getRepositoriesFiles(projectInfo);
				List<FileInfo> files = fileAux.files;
				projectInfo.setFiles(files);
				projectInfo.setNumFiles(fileAux.numFiles);
				
				List<LanguageInfo> languages = gitService.getRepositoriesLanguages(projectInfo);
				projectInfo.setLanguages(languages);
				LanguageInfo mainLanguage = gitService.getMainLanguage(languages);
				projectInfo.setMainLanguage(mainLanguage!=null?mainLanguage.getLanguage():"");
				projectDAO.update(projectInfo);
				System.out.println("THREAD: repository "+projectInfo.getFullName() + " updated!");
			} catch (IOException e) {
				e.printStackTrace();
				projectInfo.setErrorMsg("UpdateThread error: "
						+ e.toString());
				projectInfo.setStatus(ProjectStatus.ERROR);
				projectDAO.update(projectInfo);
			};
		}
	}
	private static void addProjectFilesInfo(Github github,
			GitServiceImpl gitService, ProjectInfo projectInfo)
			throws IOException {
		FileInfoAux fileAux =  gitService.getRepositoriesFiles(projectInfo);;
		List<FileInfo> files = fileAux.files;
		projectInfo.setFiles(files);
		projectInfo.setNumFiles(fileAux.numFiles);
	}


	private static void addProjectLanguages(Github github,
			GitServiceImpl gitService, ProjectInfo projectInfo)
			throws IOException {
		
	}
}
