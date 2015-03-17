package authorship.model;

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
	private int nAcceptances;
	
	public AuthorshipInfo() {
	}
	
	
	

	public AuthorshipInfo(File file, Developer developer, boolean firstAdd) {
		super();
		this.file = file;
		this.developer = developer;
		this.firstAdd = firstAdd;
		this.nDeliveries = 0;
		this.nAcceptances = 0;
	}


	public void addNewDelivery(){
		this.nDeliveries++;
	}
	public void addNewAcceptance(){
		this.nAcceptances++;
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
	public boolean isFirstAdd() {
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
		return nAcceptances;
	}
	public void setnAcceptances(int nAcceptances) {
		this.nAcceptances = nAcceptances;
	}
	
	
}
