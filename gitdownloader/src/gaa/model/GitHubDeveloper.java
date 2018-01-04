package gaa.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.User;
import org.eclipse.persistence.annotations.Index;

@Entity
public class GitHubDeveloper extends AbstractEntity{
	@Transient
	private static String SEP_STR= "**";
	@Transient
	private static Map<String, GitHubDeveloper> gitHubDevs = new HashMap<String, GitHubDeveloper>();
	@Id
	private String login;

	@Index(name="GITHUB_ID")
	private int gitHubId;
	
	@ElementCollection
	private Set<String> pairsNameEmail;
	private String name;
	private String email;
	private String company;
	
	@Transient
	private boolean updated = false;
	
	public GitHubDeveloper() {
		// TODO Auto-generated constructor stub
	}
	public GitHubDeveloper(User user) {
		this.gitHubId = user.getId();
		this.login = user.getLogin();
		this.name = user.getName();
		this.email = user.getEmail();
		this.company = user.getCompany();
		this.pairsNameEmail = new HashSet<String>();
		updated = true;
	}
	
	public static void initiateGitHubDeveloper(List<GitHubDeveloper> gitHubDevelopers){
		gitHubDevs = new HashMap<String, GitHubDeveloper>();
		for (GitHubDeveloper gitHubDeveloper : gitHubDevelopers) {
			gitHubDevs.put(gitHubDeveloper.getLogin(), gitHubDeveloper);
		}
	}

	public static GitHubDeveloper addGitHubDeveloper(User user,
			CommitUser commitUser, ProjectDevelopers projectDevs) {
		String userString = createUserString(commitUser);
		if(user!=null&&user.getLogin()!=null){
			if (!gitHubDevs.containsKey(user.getLogin()))
				gitHubDevs.put(user.getLogin(), new GitHubDeveloper(user));
			GitHubDeveloper gitHubDev = gitHubDevs.get(user.getLogin());
			if (!gitHubDev.pairsNameEmail.contains(userString)){
				gitHubDev.pairsNameEmail.add(createUserString(commitUser));
				gitHubDev.setUpdated(true);
			}
			projectDevs.addGitHubDevs(user.getLogin());
			return gitHubDev;
		}
		else{
			projectDevs.addNotGitHubDevs(userString);
		}
		return null;
	}
	private static String createUserString(CommitUser commitUser) {
		return commitUser.getName().toUpperCase()+SEP_STR+commitUser.getEmail().toUpperCase();
	}
	public int getGitHubId() {
		return gitHubId;
	}
	public void setGitHubId(int gitHubId) {
		this.gitHubId = gitHubId;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Set<String> getPairsNameEmail() {
		return pairsNameEmail;
	}
	public void setPairsNameEmail(Set<String> pairsNameEmail) {
		this.pairsNameEmail = pairsNameEmail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public static Map<String, GitHubDeveloper> getGitHubDevs() {
		return gitHubDevs;
	}
	
	@Override
	public String toString() {
		String str = String.format("%s;%s;%s;%s", login, name, email, pairsNameEmail);
		return str;
	}
	public boolean isUpdated() {
		return updated;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	
}
