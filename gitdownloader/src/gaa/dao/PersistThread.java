package gaa.dao;

import java.util.List;

import javax.persistence.EntityTransaction;

public class PersistThread <T> extends Thread{
	List<T> list;
	GenericDAO<T> persistDAO;
	public PersistThread(List<T> list, GenericDAO<T> persistDAO) {
		this.list = list;
		this.persistDAO = persistDAO; 
	}
	@Override
	public void run() {
//		System.out.println("Thread iniciada = "+this.getName());
		EntityTransaction tx = persistDAO.em.getTransaction();
		try {
			tx.begin();
			for (T t : list) {
				persistDAO.em.persist(t);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		}		
//		System.out.println("Thread Finalizada = "+this.getName());		
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
