package gaa.filter.filefilter;

import org.eclipse.jgit.api.CleanCommand;

public class FileFilterManager {
	public static void main(String[] args) {
		RemoveFileFilter removeFileFilter  = new RemoveFileFilter();
//		removeFileFilter.clean();
//		//F1
//		removeFileFilter.filterAndPersist("all", "%/docs/%");

//		//F2
//		removeFileFilter.filterAndPersist("all", "examples/%");
//		removeFileFilter.filterAndPersist("all", "%/examples/%");

//		//F3
//		removeFileFilter.filterAndPersist("Ruby", "lib/assets/javascripts/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");

//		//F9
//		removeFileFilter.filterAndPersist("all", "%/require.js");

//		//F11
		removeFileFilter.filterAndPersist("all", "benchmarks/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");

//		//F7
//		removeFileFilter.filterAndPersist("all", "build/%");
		
	}
}
