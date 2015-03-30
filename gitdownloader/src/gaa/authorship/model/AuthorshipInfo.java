package gaa.authorship.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AuthorshipInfo implements Comparable<AuthorshipInfo>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	@ManyToOne(cascade = { CascadeType.REFRESH })
	private File file;
	@ManyToOne(cascade = { CascadeType.ALL })
	private Developer developer;
	private boolean firstAuthor;
	private int nDeliveries;
	private double doa;
	
	public AuthorshipInfo() {
	}
	
	public double getDOA(){
		if (doa == 0 )
			doa = 3.293 + 1.098*(firstAuthor?1:0) + 0.164*nDeliveries - 0.332* Math.log(1 + this.getnAcceptances());
		return doa;
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
		return firstAuthor;
	}
	public void setFirstAuthor(boolean firstAdd) {
		this.firstAuthor = firstAdd;
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
		this.firstAuthor = true;		
	}

	@Override
	public int compareTo(AuthorshipInfo o) {
		return Double.compare(this.getDOA(), o.getDOA());
	}


	
}
