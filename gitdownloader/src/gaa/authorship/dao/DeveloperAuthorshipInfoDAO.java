package gaa.authorship.dao;

import gaa.authorship.model.Developer;
import gaa.authorship.model.DeveloperAuthorshipInfo;
import gaa.authorship.model.File;
import gaa.dao.GenericDAO;
import gaa.dao.PersistThread;

import java.util.List;


public class DeveloperAuthorshipInfoDAO extends GenericDAO<DeveloperAuthorshipInfo> {
	
	
	@Override
	public void persist(DeveloperAuthorshipInfo o) {
		if (o.getId()!=null){
			DeveloperAuthorshipInfo developerAuthorshipInfo = this.em.find(DeveloperAuthorshipInfo.class, o.getId());
			if (developerAuthorshipInfo != null)
				return;
		}
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

	PersistThread<DeveloperAuthorshipInfo> thread = null;
	public void persistAll(List<DeveloperAuthorshipInfo> developerAuthors){
		if (thread == null)
			thread = new PersistThread<DeveloperAuthorshipInfo>(developerAuthors, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<DeveloperAuthorshipInfo>(developerAuthors, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
	
}
