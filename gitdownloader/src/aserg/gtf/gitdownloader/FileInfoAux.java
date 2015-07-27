package aserg.gtf.gitdownloader;

import java.util.List;

import aserg.gtf.model.FileInfo;

class FileInfoAux{
	List<FileInfo> files;
	int numFiles;
	public FileInfoAux(List<FileInfo> files, int numFiles) {
		super();
		this.files = files;
		this.numFiles = numFiles;
	}
	
}