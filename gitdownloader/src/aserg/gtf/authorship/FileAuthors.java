package aserg.gtf.authorship;

import java.util.List;

import aserg.gtf.authorship.model.Developer;

public class FileAuthors {
	private String fileName;
	private String mainAuthor;
	private List<Developer> developers;
	
	
	
	public FileAuthors(String fileName, String mainAuthor) {
		super();
		this.fileName = fileName;
		this.mainAuthor = mainAuthor;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMainAuthor() {
		return mainAuthor;
	}
	public void setMainAuthor(String mainAuthor) {
		this.mainAuthor = mainAuthor;
	}
	public List<Developer> getDevelopers() {
		return developers;
	}
	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}
	
	
}
