package gaa.util;

public class NotAlias {
	public String repository;
	public String dev1;
	public String dev2;
	
	public NotAlias(String repository, String dev1, String dev2) {
		super();
		this.repository = repository;
		this.dev1 = dev1;
		this.dev2 = dev2;
	}
	
	public static boolean isAlias(String repository, String dev1, String dev2){
		NotAlias newNot = new NotAlias(repository, dev1, dev2);
		for (int i = 0; i < notAliases.length; i++) {
			if (notAliases[i].equals(newNot))
				return false;
		}
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		NotAlias other = (NotAlias)obj;
		if(this.repository.equals(other.repository) && this.dev1.equals(other.dev1) && this.dev2.equals(other.dev2))
			return true;
		return false;
	}
	
	private static NotAlias[] notAliases = {new NotAlias("rails/rails","Nick", "rick"), new NotAlias("rails/rails","Nick", "Nico"),
		new NotAlias("ruby/ruby","kou", "knu"), new NotAlias("ruby/ruby","kou", "ko1"),new NotAlias("ruby/ruby","nahi", "nari"),
		new NotAlias("ruby/ruby","eban", "evan"),};  
}