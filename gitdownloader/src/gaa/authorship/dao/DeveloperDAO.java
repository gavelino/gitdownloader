package gaa.authorship.dao;

import gaa.authorship.model.Developer;
import gaa.authorship.model.Repository;
import gaa.dao.GenericDAO;
import gaa.dao.PersistThread;

import java.util.List;

import javax.persistence.Query;


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
	
	public List<Developer> getAllDevelopers(String repositoryName){
		String hql = "SELECT d FROM Repository r "
				+ "JOIN r.developers d "
				
				+ "WHERE r.fullName = "+ "\'" + repositoryName +"\' AND d.removed = \'FALSE\'";
//		String hql = "SELECT d FROM developer d "
//				+ "JOIN repository_developer r_d ON r_d.developers_id = d.id "
//				+ "JOIN repository r ON r.id = r_d.repository_id "
//				+ "WHERE r.fullname = "+ "\"" + repositoryName +"\";";
		Query q = em.createQuery(hql);
		return q.getResultList();
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
	
	public void update(Developer o){
		Developer persistedDeveloper = this.em.find(Developer.class, o.getId());
		if (persistedDeveloper != null){
			persistedDeveloper.setAuthorshipInfos(o.getAuthorshipInfos());
			persistedDeveloper.setEmail(o.getEmail());
			persistedDeveloper.setName(o.getName());
			persistedDeveloper.setNewUserName(o.getNewUserName());
			if (o.isRemoved())
				persistedDeveloper.setAsRemoved();
			super.merge(persistedDeveloper);
		}
	}
}
