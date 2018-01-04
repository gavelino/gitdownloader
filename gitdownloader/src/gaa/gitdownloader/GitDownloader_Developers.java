package gaa.gitdownloader;

import gaa.dao.GitHubDeveloperDAO;
import gaa.dao.ProjectDevelopersDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.GitHubDeveloper;
import gaa.model.ProjectDevelopers;
import gaa.model.ProjectInfo;
import gaa.model.ProjectStatus;
import gaa.util.github.MyGitHubClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitDownloader_Developers {

	public static void main(String[] args) {
		String[] tokens = new String[] {"233d0f25b3fa8f0cca3a0e9e63e2487c47d19030", "1dd2ec420df935e917faa9c66538dd9afe824744", "e39dedaf4d899d6c145972ae7d71d22aa0bb9928"};
		
		String op = "1";
		
		if (args.length>0)
			op = args[0];
		
		
		GitHubClient client = new MyGitHubClient(new HashSet<String>(Arrays.asList(tokens)));
		//        client.setCredentials("asergprogram", "aserg.ufmg2009");
		//		client.setOAuth2Token("233d0f25b3fa8f0cca3a0e9e63e2487c47d19030");
		RepositoryService service = new RepositoryService(client);
		
		GitHubDeveloperDAO gitHubDevelopersDao = new GitHubDeveloperDAO();
		List<GitHubDeveloper> devs = new ArrayList<GitHubDeveloper>();
		if (op=="1"){
			devs = gitHubDevelopersDao.findAll(null);
			if (devs!=null && devs.size()>0)
				GitHubDeveloper.initiateGitHubDeveloper(devs);
		}
		
		ProjectInfoDAO projectDAO = new ProjectInfoDAO();
		List<ProjectInfo> projects = projectDAO.findAll(null);
		ProjectDevelopersDAO projectDevsDAO = new ProjectDevelopersDAO(); 
		
		String currentProject = "";
		try {
			for (ProjectInfo project : projects){
				if (project.getStatus() == ProjectStatus.GETINFO) {
					Repository repo = service.getRepository(project.getOwner(),
							project.getName());
					project.setStatus(ProjectStatus.ANALYZING);
					projectDAO.update(project);
					currentProject = project.getFullName();
					ProjectDevelopers projectDevs = new ProjectDevelopers(currentProject);
					System.out.println(currentProject + " - Analyzing ...");
					CommitService commitService = new CommitService(client);
					List<RepositoryCommit> commits = commitService.getCommits(repo);
					int nCommits1=0;
					int nCommits2=0;
					try {
						for (RepositoryCommit repoCommit : commits) {
							Commit commit = repoCommit.getCommit();

							if (commit != null) {
								User author = repoCommit.getAuthor();
								User committer = repoCommit.getCommitter();
								if (author!= null||committer!=null)
									nCommits1++;
								if (author!= null)
									nCommits2++;
								GitHubDeveloper.addGitHubDeveloper(
										author,
										commit.getAuthor(), projectDevs);
								GitHubDeveloper.addGitHubDeveloper(
										committer,
										commit.getCommitter(), projectDevs);

							}

						}
						System.out.println("**"+currentProject+";"+commits.size()+";"+nCommits1+";"+nCommits2+";"+projectDevs.getGitHubDevs().size()+";"+projectDevs.getNotGitHubDevs().size());
						projectDevs.setnCommits(commits.size());
						projectDevs.setnCommitsGitHubAuthorOrCommitter(nCommits1);
						projectDevs.setnCommitsGitHubAuthor(nCommits2);
						
						System.out.println("Persisting GitHub developers...");
						gitHubDevelopersDao.persistOrUpdateAll();
						project.setStatus(ProjectStatus.ANALYZED);
						projectDAO.update(project);
						
						projectDevsDAO.persist(projectDevs);
						
							
					} catch (Exception e) {
						project.setStatus(ProjectStatus.ERROR);
						project.setErrorMsg("Error in GitHub developers collector:  "+e.getMessage());
						projectDAO.update(project);
					
						System.err.println(e.getMessage());
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
