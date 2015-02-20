package gaa.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
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
public class LogCommitFileInfo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Column(length = 1000)
	private String oldFileName;
	@Column(length = 1000)
	private String newFileName;
	@Enumerated(EnumType.STRING)
	private Status status;
//	private int additions;
//	private int deletions;
//	private int commitId;

	public LogCommitFileInfo() {
		// TODO Auto-generated constructor stub
	}


	public LogCommitFileInfo(String status, String oldFileName, String newFileName) {
		super();
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.status = Status.getStatus(status);
//		setShaKey(commitSha+oldFileSha+newFileSHA);
	}

//	public Long getId() {
//		id = null;
//		return id;
//	}
//
//	public void setId(final Long id) {
//		this.id = null;
//	}

	@Override
	public String toString() {
		return newFileName + ", " + status; //+ ", " + additions  + ", " + deletions + ", " + " + commitId + ", " + message; 
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

//	@Override
//	public int compareTo(CommitFileInfo o) {
////		if (this.status == Status.ADDED)
////			if (o.status == Status.ADDED)
////				return 0;
////			else 
////				return -1;
//		return this.date.compareTo(o.date);
//	}
	

	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}


	public Long getId() {
		return id;
	}




}
