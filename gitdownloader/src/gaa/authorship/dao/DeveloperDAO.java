package gaa.authorship.dao;

import gaa.authorship.model.Developer;
import gaa.dao.GenericDAO;
import gaa.dao.PersistThread;

import java.util.List;


public class DeveloperDAO extends GenericDAO<Developer> {
	
	
	@Override
	public void persist(Developer o) {
		if (o.getId()!=null){
			Developer persistedDeveloper = this.em.find(Developer.class, o.getId());
			if (persistedDeveloper != null)
				return;
		}
		super.persist(o);
	}

	@Override
	public Developer find(Object id) {
		return this.em.find(Developer.class, id);
	}
	
	@Override
	public List<Developer> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(Developer.class);
	}
	
	
	@Override
	public void merge(Developer o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(Developer entity) {
		return this.find(entity.getId())!=null;
	}
	PersistThread<Developer> thread = null;
	public void persistAll(List<Developer> developers){
		if (thread == null)
			thread = new PersistThread<Developer>(developers, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<Developer>(developers, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
}
