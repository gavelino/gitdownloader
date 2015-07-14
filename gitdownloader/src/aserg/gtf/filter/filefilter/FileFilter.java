package aserg.gtf.filter.filefilter;

import java.util.List;

import aserg.gtf.dao.NewFileInfoDAO;
import aserg.gtf.model.FileInfo;

public abstract class FileFilter {
	List<FileInfo> files;
	String filterStamp;
	NewFileInfoDAO fiDAO;
	public FileFilter(String filterStamp) {
		this.filterStamp = filterStamp;
		fiDAO =  new NewFileInfoDAO();
	}
	
	public abstract int filterAndPersistByLanguage(String language, String pattern);
	
	public void clean(){
		fiDAO.cleanFilter();
	}
	
	
}
