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
	
	public List<FileInfo> findFileInfos(String language, String pattern){
//		String custom = "";
//		
//		if (language != null && !language.isEmpty()){
//			custom += " AND pi.language = \'" + language + "\'";
//		}
//		
//		custom += " AND fi.path LIKE \'" + pattern + "\'";
//		
//		String hql = "SELECT fi.* FROM projectinfo_fileinfo pi_fi	"
//				+ "JOIN projectinfo pi ON pi_fi.projectinfo_fullname = pi.fullname    "
//				+ "JOIN fileinfo fi on pi_fi.files_id = fi.id    "
//				+ "WHERE  pi.filtered = \'FALSE\' "
//				+ custom
//				+ ";";
//				
//				
//		Query q = em.createNativeQuery(hql);
//		 SELECT f from Student f LEFT JOIN f.classTbls s WHERE s.ClassName = 'abc'
		
		String hql = "SELECT fi FROM FileInfo fi JOIN fi.projectInfo pi WHERE pi.name = \'ajaxorg-ace\'";
		Query q = em.createQuery(hql);
		return q.getResultList();
			
	}
	
	public void updateList(List<FileInfo> files){
		
		EntityTransaction tx = this.em.getTransaction();
		try {
			tx.begin();
			for (Object t : files) {
				this.em.persist(t);
			}
			tx.commit();
		} catch (RuntimeException e) {
			if(tx != null && tx.isActive()) 
				tx.rollback();
			throw e;
		} 
		finally{
			this.em.clear();
		}
	}

}
