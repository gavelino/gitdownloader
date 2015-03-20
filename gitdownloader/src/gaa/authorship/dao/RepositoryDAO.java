package gaa.authorship.dao;

import java.util.List;


import gaa.authorship.model.Repository;
import gaa.dao.GenericDAO;


public class RepositoryDAO extends GenericDAO<Repository> {
	
	
	@Override
	public void persist(Repository o) {
		Repository persistedRepository = this.em.find(Repository.class, o.getId());
		if (persistedRepository == null)
			super.persist(o);
		
	}

	@Override
	public Repository find(Object id) {
		return this.em.find(Repository.class, id);
	}
	
	@Override
	public List<Repository> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(Repository.class);
	}
	
	
	@Override
	public void merge(Repository o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(Repository entity) {
		return this.find(entity.getId())!=null;
	}
}
