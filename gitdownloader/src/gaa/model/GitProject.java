package gaa.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class GitProject extends AbstractEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@OneToOne(cascade = { CascadeType.REFRESH })
	ProjectInfo projectInfo;
	@OneToMany(cascade = { CascadeType.ALL })
	List<CommitInfo> commits;

	public GitProject() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		id = null;
		return id;
	}

	public void setId(final Long id) {
		this.id = null;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public List<CommitInfo> getCommits() {
		return commits;
	}

	public void setCommits(List<CommitInfo> commits) {
		this.commits = commits;
	}

}
