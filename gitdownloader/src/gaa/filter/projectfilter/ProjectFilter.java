package gaa.filter.projectfilter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;

import java.util.List;

public abstract class ProjectFilter {
	List<ProjectInfo> projects;
	String filterStamp;
	public ProjectFilter(List<ProjectInfo> projects, String filterStamp) {
		this.projects = projects;
		this.filterStamp = filterStamp;
	}
	public abstract List<ProjectInfo> filter();
	
	public void persistFilterInformations() {
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		for (ProjectInfo projectInfo : projects) {
			piDAO.update(projectInfo);
		}

	}
	public void clean() {
		for (ProjectInfo projectInfo : projects) {
			if (projectInfo.isFiltered()){
				projectInfo.getFilterinfo().replace(filterStamp, "");
				if (projectInfo.getFilterinfo().isEmpty())
					projectInfo.setFiltered(false);
			}
		}
	}
}
