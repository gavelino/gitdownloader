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
public class FileInfo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	private String path;
	private int size;
	private String mode;
	private String type;
	private String sha;
	

	public FileInfo() {
	}
	
	public FileInfo(String path, int size, String mode, String type, String sha) {
		super();
		this.path = path;
		this.size = size;
		this.mode = mode;
		this.type = type;
		this.sha = sha;
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




}
