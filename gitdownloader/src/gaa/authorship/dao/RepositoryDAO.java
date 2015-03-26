package gaa.authorship.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import gaa.authorship.model.Repository;
import gaa.dao.GenericDAO;
import gaa.dao.PersistThread;


public class RepositoryDAO extends GenericDAO<Repository> {
	
	
	@Override
	public void persist(Repository o) {
		if (o.getId()!=null){
			Repository persistedRepository = this.em.find(Repository.class, o.getId());
			if (persistedRepository != null)
				return;
		}
		prePersist(o);
		super.persist(o);
	}

	private void prePersist(Repository repository) {
//		new DeveloperDAO().persistAll(repository.getDevelopers());
//		new FileDAO().persistAll(repository.getFiles());
//		new AuthorshipInfoDAO().persistAll(repository.getAuthorships());
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
	
	public Map<String, Set<String>> getFilesAuthor(String repositoryName){
		String hql = "SELECT d.username, fi.path FROM repository_file rf	"
				+ "JOIN repository r ON r.id = rf.repository_id "
				+ "JOIN file fi ON fi.id = rf.files_id "
				+ "JOIN authorshipinfo ai ON ai.id = fi.bestauthorshipinfo_id "
				+ "JOIN developer d ON ai.developer_id = d.id "
				+ "WHERE r.fullname = \'" +  repositoryName +"\' " 
				+ "ORDER BY d.username;";
		Query q = em.createNativeQuery(hql);
		List<Object[]> authorsFiles = q.getResultList();
		
		Map<String, Set<String>> maps = new HashMap<String, Set<String>>();
		String firstAuthor = null;
		for (Object[] objects : authorsFiles) {
			String developerName = (String)objects[0];
			String fileName = (String)objects[1];
			if (!maps.containsKey(developerName))
				maps.put(developerName, new HashSet<String>());
			maps.get(developerName).add(fileName);			
		}
		return maps;
	}
	
	public List<String> getAllRepositoryNames(){
		String hql = "SELECT fullname FROM repository;";
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
	}
}
