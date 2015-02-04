package gaa.dao;

import java.util.List;

import javax.persistence.Query;

import gaa.model.CommitFileInfo;
import gaa.model.CommitInfo;
import gaa.model.ProjectInfo;


public class CommitFileDAO extends GenericDAO<CommitFileInfo> {
	
	
	@Override
	public void persist(CommitFileInfo project) {
		super.persist(project);
	}

	@Override
	public CommitFileInfo find(Object id) {
		return this.em.find(CommitFileInfo.class, id);
	}
	
	@Override
	public List<CommitFileInfo> findAll(Class clazz) {
		// TODO Auto-generated method stub
		return super.findAll(CommitFileInfo.class);
	}
	
	@Override
	public void merge(CommitFileInfo o) {
		super.merge(o);
	}
	
	private void update(ProjectInfo o){
		
	}

	@Override
	public boolean exist(CommitFileInfo entity) {
		return this.find(entity.getId())!=null;
	}

	public List<Long> getAddsCommitFile(String repositoryName) {
		
		String hql = "SELECT COUNT(*) FROM COMMITINFO_COMMITFILEINFO ci_cfi	"
				+ "JOIN COMMITINFO ci ON ci_cfi.CommitInfo_ID = ci.ID    "
				+ "JOIN COMMITFILEINFO cfi ON ci_cfi.commitFiles_ID = cfi.ID    "
				+ "WHERE ci.REPOSITORYNAME = \"" +  repositoryName +"\"" + " AND cfi.STATUS = \"ADDED\"    "
						+ "GROUP BY ci.SHA    "
						+ "ORDER BY COUNT(*) DESC;";
		Query q = em.createNativeQuery(hql);
		
		return q.getResultList();
	}
}
