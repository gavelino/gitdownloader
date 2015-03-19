package gaa.authorship.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuthorshipInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	private File file;
	private Developer developer;
	private boolean firstAdd;
	private int nDeliveries;
	
	public AuthorshipInfo() {
	}
	
	

	public AuthorshipInfo(File file, Developer developer) {
		super();
		this.file = file;
		this.developer = developer;
		this.nDeliveries = 0;

		developer.addAuthorshipInfo(this);
		file.addAuthorshipInfo(this);
	}


	public void addNewDelivery(){
		this.nDeliveries++;
	}
	
	public void incNDeliveries(int inc){
		nDeliveries+=inc;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Developer getDeveloper() {
		return developer;
	}
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	public boolean isFirstAuthor() {
		return firstAdd;
	}
	public void setFirstAdd(boolean firstAdd) {
		this.firstAdd = firstAdd;
	}
	public int getnDeliveries() {
		return nDeliveries;
	}
	public void setnDeliveries(int nDeliveries) {
		this.nDeliveries = nDeliveries;
	}
	public int getnAcceptances() {
		return this.file.getnChanges() - this.nDeliveries;
	}


	public void setAsFirstAuthor() {
		this.firstAdd = true;		
	}


	
}
