package gaa.gitdownloader;

import java.io.IOException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import com.jcabi.http.Request;
import com.jcabi.http.response.JsonResponse;

public class GitProjectFinder {

	Logger logger = LoggerFactory.getLogger(GitProjectFinder.class);
	
	public static void main(String[] args) throws IOException {
		new GitProjectFinder().findRepos();
	}

	private void findRepos() throws IOException {
		Github github = new RtGithub("asergufmg", "aserg.ufmg2009");
		Request request = github.entry()
				.uri().path("/search/repositories")
				.queryParam("q", "language:Java created:<=2014-06-01")
				.queryParam("sort", "stars")
				.queryParam("order", "desc")
				.queryParam("per_page", "100").back()
				.method(Request.GET);

		JsonArray items = request.fetch().as(JsonResponse.class).json().readObject().getJsonArray("items");
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

			this.logger.info("Project {} {}", repoData.getString("clone_url"), repoData.getString("default_branch"));
		}
	}

}
