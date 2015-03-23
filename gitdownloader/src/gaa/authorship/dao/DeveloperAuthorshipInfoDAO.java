package gaa.authorship.dao;

import gaa.authorship.model.DeveloperAuthorshipInfo;
import gaa.dao.GenericDAO;

import java.util.List;


public class DeveloperAuthorshipInfoDAO extends GenericDAO<DeveloperAuthorshipInfo> {
	
	
	@Override
	public void persist(DeveloperAuthorshipInfo o) {
		DeveloperAuthorshipInfo persistedRepository = this.em.find(DeveloperAuthorshipInfo.class, o.getId());
		if (persistedRepository == null)
			super.persist(o);
		
	}

	@Override
	public DeveloperAuthorshipInfo find(Object id) {
		return this.em.find(DeveloperAuthorshipInfo.class, id);
	}
	
	@Override
	public List<DeveloperAuthorshipInfo> findAll(Class clazz) {
		return super.findAll(DeveloperAuthorshipInfo.class);
	}
	
	
	@Override
	public void merge(DeveloperAuthorshipInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(DeveloperAuthorshipInfo entity) {
		return this.find(entity.getId())!=null;
	}
	
}
