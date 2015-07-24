package aserg.gtf.filter.filefilter;

import java.util.List;

import aserg.gtf.dao.NewFileInfoDAO;
import aserg.gtf.model.NewFileInfo;

public abstract class FileFilter {
	List<NewFileInfo> files;
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
