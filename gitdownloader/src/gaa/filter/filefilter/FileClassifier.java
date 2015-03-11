package gaa.filter.filefilter;

import gaa.dao.FileInfoDAO;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FileClassifier {
	public static void main(String[] args) {
		FileInfoDAO fiDAO = new FileInfoDAO();
		FileType fileTypes[] = FileType.values();
//		for (int i = 0; i < fileTypes.length; i++) {
//			FileType fileType = fileTypes[i];
//			if (fileType != FileType.NOTIDENTIFIED) {
//				String whereClauses = "AND (";
//				List<String> patterns = FileType.getPatterns(fileType);
//				if (patterns.size()>0) {
//					Iterator<String> iterator = patterns.iterator();
//					while (iterator.hasNext()) {
//						String pattern = iterator.next();
//						if (fileType == FileType.DOCUMENTATION || fileType == FileType.EXAMPLES || fileType == FileType.LIBRARIES) 
//							whereClauses += " LOWER (fi.path) LIKE \'" + pattern + "\' ";
//						else
//							whereClauses += " fi.path LIKE \'" + pattern + "\' ";
//						if (iterator.hasNext())
//							whereClauses += " OR ";
//					}
//					whereClauses += " ) ";
//					System.out.println("Finding " + fileType + " files ...");
//					fiDAO.classifierAndUpdateFilesInfo(whereClauses, fileType);
//				}
//			}
//		}
		
		for (int i = 0; i < fileTypes.length; i++) {
			FileType fileType = fileTypes[i];
			if (fileType != FileType.NOTIDENTIFIED) {
				String whereClauses = "AND (";
				List<String> patterns = FileType.getPosixPatterns(fileType);
				if (patterns.size()>0) {
					Iterator<String> iterator = patterns.iterator();
					while (iterator.hasNext()) {
						String pattern = iterator.next();
						whereClauses += " fi.path ~ \'" + pattern + "\' ";
						if (iterator.hasNext())
							whereClauses += " OR ";
					}
					whereClauses += " ) ";
					System.out.println("Finding " + fileType + " files ...");
					fiDAO.classifierAndUpdateFilesInfo(whereClauses, fileType);
				}
			}
		}
		
		System.out.println(new Date());
	}

}
