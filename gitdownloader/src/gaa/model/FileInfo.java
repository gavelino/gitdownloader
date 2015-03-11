package gaa.model;

import gaa.filter.filefilter.FileType;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Index;

@Entity
public class FileInfo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Index(name="FILEPATHINDEX")
	private String path;
	private int size;
	private String mode;
	private String type;
	private String sha;
	private Boolean filtered;
	private String filterInfo;
	@Enumerated(EnumType.STRING)
	private FileType kind;
	
	@ManyToOne(cascade = {CascadeType.REFRESH})
	private ProjectInfo projectInfo;
	
	public FileInfo() {
		// TODO Auto-generated constructor stub
	}

	public FileInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
	
	public FileInfo(ProjectInfo projectInfo, String path, int size, String mode, String type, String sha, FileType kind) {
		super();
		this.projectInfo = projectInfo;
		this.path = path;
		this.size = size;
		this.mode = mode;
		this.type = type;
		this.sha = sha;
		this.filtered = false;
		this.filterInfo = new String();
		this.kind = kind;
	}
	
	public Long getId() {
		return id;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	public String getMode() {
		return mode;
	}



	public void setMode(String mode) {
		this.mode = mode;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getSha() {
		return sha;
	}



	public void setSha(String sha) {
		this.sha = sha;
	}

	public Boolean getFiltered() {
		return filtered;
	}

	public void setFiltered(Boolean filtered) {
		this.filtered = filtered;
	}

	public String getFilterInfo() {
		return filterInfo;
	}

	public void setFilterInfo(String filterInfo) {
		this.filterInfo = filterInfo;
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}

	public FileType getKind() {
		return kind;
	}

	public void setKind(FileType kind) {
		this.kind = kind;
	}




}
