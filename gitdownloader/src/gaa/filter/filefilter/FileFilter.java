package gaa.filter.filefilter;

import gaa.dao.FileInfoDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.FileInfo;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;

import java.util.List;

public abstract class FileFilter {
	List<FileInfo> files;
	String filterStamp;
	FileInfoDAO fiDAO;
	public FileFilter(String filterStamp) {
		this.filterStamp = filterStamp;
		fiDAO =  new FileInfoDAO();
	}
	
	public abstract int filterAndPersist(String language, String pattern);
	
	public void clean(){
		fiDAO.cleanFilter();
	}
	
	
}
