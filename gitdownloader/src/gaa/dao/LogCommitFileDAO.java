package gaa.dao;

import java.util.List;

import javax.persistence.Query;

import gaa.model.CommitFileInfo;
import gaa.model.CommitInfo;
import gaa.model.LogCommitFileInfo;
import gaa.model.ProjectInfo;

public class LogCommitFileDAO extends GenericDAO<LogCommitFileInfo>{


	@Override
	public void persist(LogCommitFileInfo logCFiles) {
		super.persist(logCFiles);
	}

	@Override
	public LogCommitFileInfo find(Object id) {
		return this.em.find(LogCommitFileInfo.class, id);
	}
	
	@Override
	public List<LogCommitFileInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(LogCommitFileInfo.class);
	}
	
	@Override
	public void merge(LogCommitFileInfo o) {
		super.merge(o);
	}
	

	@Override
	public boolean exist(LogCommitFileInfo logCFile) {
		return this.find(logCFile.getId())!=null;
	}
	
	PersistThread<LogCommitFileInfo> thread = null;
	public void persistAll(List<LogCommitFileInfo> logCFiles){
		if (thread == null)
			thread = new PersistThread<LogCommitFileInfo>(logCFiles, this);
		else {
			try {
				if (thread.isAlive())
					thread.join();
				thread = new PersistThread<>(logCFiles, this);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		thread.start();
	}
	
	public List<Long> getAddsCommitFileOrderByNumberOfCFs(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_LOGCOMMITFILEINFO ci_lcfi	"
				+ "JOIN COMMITINFO ci ON ci_lcfi.commitinfo_id = ci.ID "
				+ "JOIN LOGCOMMITFILEINFO lcfi ON ci_lcfi.logcommitfiles_id = lcfi.ID "
				+ "WHERE ci.REPOSITORYNAME = \'" +  repositoryName +"\'" + " AND lcfi.STATUS = \'ADDED\'    "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY COUNT(*) DESC;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
	public List<Long> newGetAddsCommitFileOrderByNumberOfCFs(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_LOGCOMMITFILEINFO ci_lcfi	"
				+ "JOIN COMMITINFO ci ON ci_lcfi.commitinfo_id = ci.ID "
				+ "JOIN LOGCOMMITFILEINFO lcfi ON ci_lcfi.logcommitfiles_id = lcfi.ID "
				+ "JOIN projectInfo_fileinfo pi_fi on pi_fi.projectinfo_fullname = ci.repositoryname "
				+ "JOIN fileinfo fi ON pi_fi.files_ID = fi.ID"
				+ "WHERE lcfi.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND lcfi.STATUS = \"ADDED\" AND lcfi.NEWFILENAME = fi.PATH   "
						+ "GROUP BY lcfi.SHA    "
						+ "ORDER BY COUNT(*) DESC;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
	public List<Long> getAddsCommitFileOrderByDate(String repositoryName) {
		String hql = "SELECT COUNT(*) FROM COMMITINFO_LOGCOMMITFILEINFO ci_lcfi	"
				+ "JOIN COMMITINFO ci ON ci_lcfi.commitinfo_id = ci.ID "
				+ "JOIN LOGCOMMITFILEINFO lcfi ON ci_lcfi.logcommitfiles_id = lcfi.ID "
				+ "WHERE ci.REPOSITORYNAME = \'" +  repositoryName +"\'" + " AND lcfi.STATUS = \'ADDED\'    "
						+ "GROUP BY ci.SHA, ci.date    "
						+ "ORDER BY ci.DATE;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
		
	}
	
	public List<Long> newGetAddsCommitFileOrderByDate(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_LOGCOMMITFILEINFO ci_lcfi	"
				+ "JOIN COMMITINFO ci ON ci_lcfi.commitinfo_id = ci.ID    "
				+ "JOIN LOGCOMMITFILEINFO lcfi ON ci_lcfi.logcommitfileinfo_id = lcfi.ID    "
				+ "JOIN projectinfo_fileinfo pi_fi ON pi_fi.ProjectInfo_FULLNAME = ci.REPOSITORYNAME    "
				+ "JOIN fileinfo fi ON pi_fi.files_ID = fi.ID    "
				
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND lcfi.STATUS = \"ADDED\" AND lcfi.NEWFILENAME = fi.PATH   "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY ci.DATE;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
	
	public List<Object[]> getLogCommitFileInfoOrderByDate(String repositoryName, String path) {
		String hql = "SELECT ci.name, ci.email, lcfi.oldfilename, lcfi.newfilename, lcfi.status FROM commitinfo_logcommitfileinfo ci_lcfi	"
				+ "JOIN commitinfo ci ON ci_lcfi.commitinfo_id = ci.id "
				+ "JOIN logcommitfileinfo lcfi ON ci_lcfi.logcommitfiles_id = lcfi.id "
				+ "WHERE ci.REPOSITORYNAME = \'" +  repositoryName +"\'" + " AND lcfi.newfilename = \'" +  path +"\' "
						+ "ORDER BY ci.DATE;";
		Query q = em.createNativeQuery(hql);
		return q.getResultList();
		
	}
	
}
