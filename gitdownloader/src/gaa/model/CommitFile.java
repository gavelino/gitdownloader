package gaa.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CommitFile extends AbstractEntity implements Comparable<CommitFile> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;



	private String sha;
	private String oldFileName;
	private String newFileName;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String login;
	private String name;
	private String email;
	private int additions;
	private int deletions;
	private int commitId;
	@Lob
	private String message;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	String projectName;

	public CommitFile() {
		// TODO Auto-generated constructor stub
	}

	public CommitFile(Timestamp date, String oldFileName, String newFileName, Status status, String login,
			int additions, int deletions, String sha, int commitId,
			String message) {
		super();
		this.date = date;
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.status = status;
		this.login = login;
		this.additions = additions;
		this.deletions = deletions;
		this.sha = sha;
		this.commitId = commitId;
		this.message = message;
	}


	public CommitFile(Timestamp date, String oldFileName, String newFileName, Status status, String login,
			String name, String email, int additions, int deletions,
			String sha, int commitId, String message) {
		super();
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.status = status;
		this.login = login;
		this.name = name;
		this.email = email;
		this.additions = additions;
		this.deletions = deletions;
		this.sha = sha;
		this.commitId = commitId;
		this.message = message;
		this.date = date;
	}

	public Long getId() {
		id = null;
		return id;
	}

	public void setId(final Long id) {
		this.id = null;
	}

	@Override
	public String toString() {
		return sha + ", " + date + ", " + newFileName + ", " + status + ", " + login + ", " + name + ", " + email; //+ ", " + additions  + ", " + deletions + ", " + sha + ", " + commitId + ", " + message; 
	}
	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String fileName) {
		this.newFileName = fileName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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


	public int getAdditions() {
		return additions;
	}

	public void setAdditions(int additions) {
		this.additions = additions;
	}

	public int getDeletions() {
		return deletions;
	}

	public void setDeletions(int deletions) {
		this.deletions = deletions;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public int getCommitId() {
		return commitId;
	}

	public void setCommitId(int commitId) {
		this.commitId = commitId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public int compareTo(CommitFile o) {
//		if (this.date.compareTo(o.date)==0){
//			if (this.status == Status.ADDED)
//				return -1;
//			if (o.status == Status.ADDED)
//				return 1;
//			else return 0;
//		}
		return this.date.compareTo(o.date);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}



}
