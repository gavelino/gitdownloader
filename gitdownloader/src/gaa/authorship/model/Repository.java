package gaa.authorship.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class Repository {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column(unique=true)
	private String fullName;
	@OneToMany(cascade = { CascadeType.ALL })
	private List<File> files;
	

	@javax.persistence.OneToMany(cascade = CascadeType.ALL)
	@javax.persistence.MapKey(name = "name")
	private Map<String, Developer> developerMap = new HashMap<String, Developer>(); 
	
	@javax.persistence.OneToMany(cascade = CascadeType.ALL)
	@javax.persistence.MapKey(name = "name")
	private Map<String, AuthorshipInfo> authorshipInfoMap = new HashMap<String, AuthorshipInfo>();
	
	public Repository() {
		// TODO Auto-generated constructor stub
	}
	public Repository(String fullName) {
		this.fullName = fullName;
	}
	
	private Developer addDeveloper(String name, String email) {
		Developer developer;
		if(developerMap.containsKey(email))
			developer = developerMap.get(email);
		else{
			developer = new Developer(name, email);
			developerMap.put(email, new Developer(name, email));
		}
		return developer;
		
	}

	public AuthorshipInfo getAuthorshipInfo(String name, String email, File file) {
		Developer developer = this.addDeveloper(name, email);
		String authorshipKey = (file.getPath() + developer.getUserName());
		AuthorshipInfo authorshipInfo;
		if(authorshipInfoMap.containsKey(authorshipKey))
			authorshipInfo = authorshipInfoMap.get(authorshipKey);
		else {
			authorshipInfo =  new AuthorshipInfo(file, developer);
			authorshipInfoMap.put(authorshipKey, authorshipInfo);
		}
		return authorshipInfo;		
	}
	
	
	
	
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public Long getId() {
		return id;
	}

	public Map<String, AuthorshipInfo> getAuthorshipInfoMap() {
		return authorshipInfoMap;
	}
	
	public void setAuthorshipInfoMap(Map<String, AuthorshipInfo> authorshipInfoMap) {
		this.authorshipInfoMap = authorshipInfoMap;
	}

	
	
	
}
