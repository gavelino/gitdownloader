package gaa.dao;

import java.time.Period;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import gaa.model.FileInfo;
import gaa.model.ProjectInfo;

public class FileInfoDAO extends GenericDAO<FileInfo>{

	@Override
	public FileInfo find(Object o) {
		return this.em.find(FileInfo.class, o);
	}	

	@Override
	public boolean exist(FileInfo entity) {
		return this.find(entity.getId())!=null;
	}

	public void update(FileInfo o){
		FileInfo persistedProject = this.em.find(FileInfo.class, o.getId());
		if (persistedProject != null){
			persistedProject.setFiltered(o.getFiltered());
			persistedProject.setFilterInfo(o.getFilterInfo());
			persistedProject.setMode(o.getMode());
			persistedProject.setPath(o.getPath());
			persistedProject.setSha(o.getSha());
			persistedProject.setSize(o.getSize());
			persistedProject.setType(o.getType());
			super.merge(persistedProject);
		}
	}
	
	public int filterAndUpdateFilesInfo(String whereClauses, String filterStamp){
		String custom = "";
		
		
		
		     
		String hql = "UPDATE  fileinfo AS fi "
				+ "SET filtered = \'TRUE\', filterinfo = \'"+ filterStamp +"\'  "
				+ "FROM projectinfo_fileinfo AS pi_fi, projectinfo AS pi    "
				+ "WHERE pi.filtered = \'FALSE\' AND pi_fi.projectinfo_fullname = pi.fullname AND pi_fi.files_id = fi.id "
				+ whereClauses
				+ ";";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	
	public int cleanFilter(){
			     
		String hql = "UPDATE  fileinfo "
				+ "SET filtered = \'FALSE', filterinfo = \'\';";
				
				
		Query q = em.createNativeQuery(hql);
		int rows =0 ;
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			rows = q.executeUpdate();
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
		return rows;
			
	}
	

}
