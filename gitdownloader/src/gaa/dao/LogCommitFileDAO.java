package gaa.dao;

import java.util.List;

import gaa.model.CommitFileInfo;
import gaa.model.CommitInfo;
import gaa.model.LogCommitFileInfo;
import gaa.model.ProjectInfo;

public class LogCommitFileDAO extends GenericDAO<LogCommitFileInfo>{


	@Override
	public void persist(LogCommitFileInfo logCFiles) {
		super.persist(logCFiles);
	}

	@Override
	public LogCommitFileInfo find(Object id) {
		return this.em.find(LogCommitFileInfo.class, id);
	}
	
	@Override
	public List<LogCommitFileInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(LogCommitFileInfo.class);
	}
	
	@Override
	public void merge(LogCommitFileInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(LogCommitFileInfo logCFile) {
		return this.find(logCFile.getId())!=null;
	}
	
	PersistThread<LogCommitFileInfo> thread = null;
	public void persistAll(List<LogCommitFileInfo> logCFiles){
		if (thread == null)
			thread = new PersistThread<LogCommitFileInfo>(logCFiles, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<>(logCFiles, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread.start();
	}

	
}
