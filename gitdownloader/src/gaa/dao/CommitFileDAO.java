package gaa.dao;

import java.util.List;

import gaa.model.CommitFileInfo;
import gaa.model.ProjectInfo;


public class CommitFileDAO extends GenericDAO<CommitFileInfo> {
	
	
	@Override
	public void persist(CommitFileInfo project) {
		super.persist(project);
	}

	@Override
	public CommitFileInfo find(Object id) {
		return this.em.find(CommitFileInfo.class, id);
	}
	
	@Override
	public List<CommitFileInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(CommitFileInfo.class);
	}
	
	@Override
	public void merge(CommitFileInfo o) {
		super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}
}
