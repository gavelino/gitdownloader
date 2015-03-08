package gaa.dao;

import gaa.model.FileInfo;

public class FileInfoDAO extends GenericDAO<FileInfo>{

	@Override
	public FileInfo find(Object o) {
		return this.em.find(FileInfo.class, o);
	}

	@Override
	public boolean exist(FileInfo entity) {
		return this.find(entity.getId())!=null;
	}

}
