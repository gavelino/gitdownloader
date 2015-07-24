package aserg.gtf.gitdownloader;

import java.util.List;

import aserg.gtf.model.NewFileInfo;

class FileInfoAux{
	List<NewFileInfo> files;
	int numFiles;
	public FileInfoAux(List<NewFileInfo> files, int numFiles) {
		super();
		this.files = files;
		this.numFiles = numFiles;
	}
	
}