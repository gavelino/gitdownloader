package gaa.gitdownloader.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


abstract class GenericDAO<T> {
	protected final EntityManager em =  Persistence.createEntityManagerFactory("main").createEntityManager();

	public GenericDAO() {
		super();
	}
	
	public void persist(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.persist(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
	}
	
	public abstract T find(Object id);
		
	public void remove(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.remove(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
	}
	
	public void merge(T o) {
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			this.em.merge(o);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.em.close();
		super.finalize();
	}
	
	
	
}