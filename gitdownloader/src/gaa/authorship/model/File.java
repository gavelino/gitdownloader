package gaa.authorship.model;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	private String path;
	
	@javax.persistence.OneToMany(cascade = CascadeType.ALL)
	@javax.persistence.MapKey(name = "name")
	private Map<String, AuthorshipInfo> authorshipInfos;
	
	public File() {
	}

	public File(String path) {
		super();
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, AuthorshipInfo> getAuthorshipInfos() {
		return authorshipInfos;
	}
	
	
}
