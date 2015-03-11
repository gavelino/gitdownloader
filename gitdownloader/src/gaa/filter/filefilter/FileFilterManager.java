package gaa.filter.filefilter;

import org.eclipse.jgit.api.CleanCommand;

public class FileFilterManager {
	public static void main(String[] args) {
		RemoveFileFilter removeFileFilter  = new RemoveFileFilter();
//		removeFileFilter.clean();
		removeFileFilter.filterAndPersist("all", "%.min.css");
		removeFileFilter.filterAndPersist("all", "docs/%");
		removeFileFilter.filterAndPersist("all", "%/docs/%");
		
	}
}
