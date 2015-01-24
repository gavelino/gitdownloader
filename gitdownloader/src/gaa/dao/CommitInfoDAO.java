package gaa.dao;

import java.util.List;

import gaa.model.CommitFileInfo;
import gaa.model.CommitInfo;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;


public class CommitInfoDAO extends GenericDAO<CommitInfo> {
	PersistThread<CommitInfo> thread = null;
	@Override
	public void persist(CommitInfo project) {
		super.persist(project);
	}

	@Override
	public CommitInfo find(Object id) {
		return this.em.find(CommitInfo.class, id);
	}
	
	@Override
	public List<CommitInfo> findAll(Class clazz) {
		return super.findAll(CommitInfo.class);
	}
	
	@Override
	public void merge(CommitInfo o) {
		CommitInfo commit = this.find(o.getSha());
		if (commit == null)
			super.merge(o);
	}
	
	public void persistAll(List<CommitInfo> commits){
		if (thread == null)
			thread = new PersistThread<CommitInfo>(commits, this);
		else {
			try {
				thread.join();
				thread.setList(commits);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread.run();
	}
	private void update(CommitInfo o){
		
	}
}
