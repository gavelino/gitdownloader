package gaa.filter.filefilter;

import gaa.model.FileInfo;
import gaa.model.ProjectInfo;

import java.util.Date;
import java.util.List;

public class RemoveFileFilter extends FileFilter{
	
	
	private String pattern;
	public RemoveFileFilter() {
		super("#Remove");
		// TODO Auto-generated constructor stub
	}

	@Override
	public int filterAndPersistByLanguage(String language, String pattern) {
		String localFilterStamp = filterStamp+"("+language+"): "+pattern+"#";
		
		String whereClauses = "";
		if (language != null && !language.isEmpty()&&!language.equalsIgnoreCase("all")){
			whereClauses += " AND pi.language = \'" + language + "\'";
		}
		whereClauses += " AND fi.path LIKE \'" + pattern + "\'";
		
		int nRows = fiDAO.filterAndUpdateFilesInfo(whereClauses, localFilterStamp);
		System.out.println(new Date() + " - " + pattern +" - Updated "+ nRows);
		return nRows;
	}
	
	public int filterAndPersistByProject(String projectName, String pattern) {
		String localFilterStamp = filterStamp+"("+projectName+"): "+pattern+"#";
		
		String whereClauses = "";
		whereClauses += " AND pi.fullname = \'" + projectName + "\'";
		whereClauses += " AND fi.path LIKE \'" + pattern + "\'";
		
		int nRows = fiDAO.filterAndUpdateFilesInfo(whereClauses, localFilterStamp);
		System.out.println(new Date() + " - " + pattern +" - Updated "+ nRows);
		return nRows;
	}
	
		
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}


}
