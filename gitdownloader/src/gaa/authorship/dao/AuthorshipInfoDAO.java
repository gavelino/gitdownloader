package gaa.authorship.dao;

import gaa.authorship.model.AuthorshipInfo;
import gaa.authorship.model.DeveloperAuthorshipInfo;
import gaa.dao.GenericDAO;
import gaa.dao.PersistThread;

import java.util.List;


public class AuthorshipInfoDAO extends GenericDAO<AuthorshipInfo> {
	
	
	@Override
	public void persist(AuthorshipInfo o) {
		if (o.getId()!=null){
			AuthorshipInfo authorshipInfo = this.em.find(AuthorshipInfo.class, o.getId());
			if (authorshipInfo != null)
				return;
		}
		super.persist(o);
	}

	@Override
	public AuthorshipInfo find(Object id) {
		return this.em.find(AuthorshipInfo.class, id);
	}
	
	@Override
	public List<AuthorshipInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(AuthorshipInfo.class);
	}
	
	
	@Override
	public void merge(AuthorshipInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(AuthorshipInfo entity) {
		return this.find(entity.getId())!=null;
	}

	PersistThread<AuthorshipInfo> thread = null;
	public void persistAll(List<AuthorshipInfo> authorshipsInfo){
		if (thread == null)
			thread = new PersistThread<AuthorshipInfo>(authorshipsInfo, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<AuthorshipInfo>(authorshipsInfo, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.start();
	}
}
