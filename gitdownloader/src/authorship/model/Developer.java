package authorship.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Developer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	private String name;
	private String email;
	private String login;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<AuthorshipInfo> authorshipInfos;
	
	
}
