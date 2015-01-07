package gaa.gitdownloader;

import gaa.gitdownloader.model.ProjectGit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.lf5.viewer.configure.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.http.Request;
import com.jcabi.http.response.JsonResponse;

public class GitProjectFinder {

	Logger logger = LoggerFactory.getLogger(GitProjectFinder.class);
//	public static void main(String[] args) throws IOException {
//		new GitProjectFinder().findRepos();
//	}
	public GitProjectFinder() {
		BasicConfigurator.configure();
		LogManager.getRootLogger().setLevel(Level.INFO);
	}

	public List<ProjectGit> findRepos(Request request) throws IOException {
		
		

		JsonArray items = request.fetch().as(JsonResponse.class).json().readObject().getJsonArray("items");
		List<ProjectGit> projects = new ArrayList<ProjectGit>();
		for (JsonValue item : items) {
			JsonObject repoData = (JsonObject) item;
			ProjectGit p = new ProjectGit();
			p.setName(repoData.getString("name"));
			p.setSize(repoData.getInt("size"));
			p.setFork(repoData.getBoolean("fork"));
			p.setStargazers_count(repoData.getInt("stargazers_count"));
			p.setWatchers_count(repoData.getInt("watchers_count"));
			p.setForks_count(repoData.getInt("forks_count"));
			p.setDefault_branch(repoData.getString("default_branch"));
			p.setOpen_issues(repoData.getInt("open_issues"));
//			p.setCreated_at(StringToDate.parseDatePatterns(repoData.getString("created_at")));
//			p.setUpdated_at(StringToDate.parseDatePatterns(repoData.getString("updated_at")));
//			p.setPushed_at(StringToDate.parseDatePatterns(repoData.getString("pushed_at")));
			p.setLanguage(repoData.getString("language"));
			p.setCloneUrl(repoData.getString("clone_url"));

			if (!repoData.isNull("description")) {
//				p.setDescription(repoData.getString("description"));
			}
			projects.add(p);
			logger.info("Project {} {}", repoData.getString("clone_url"), repoData.getString("default_branch"));
		}
		return projects;
	}	
}
