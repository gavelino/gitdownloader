package gaa.filter.filefilter;

import gaa.model.FileInfo;
import gaa.model.ProjectInfo;

import java.util.List;

public class RemoveFileFilter extends FileFilter{
	
	public static void main(String[] args) {
		RemoveFileFilter removeFileFilter  = new RemoveFileFilter("%.min.css");
		removeFileFilter.filterAndPersist();
	}
	private String pattern;
	public RemoveFileFilter(String pattern) {
		super("#Remove: "+pattern+"#");
		this.setPattern(pattern);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public List<ProjectInfo> filterAndPersist() {
		this.files = fiDAO.findFileInfos("Ruby", pattern);
		for (FileInfo fileInfo : files) {
			System.out.println(fileInfo);
//			fiDAO.update(fileInfo);
		}
		return null;
	}
		
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}




}
