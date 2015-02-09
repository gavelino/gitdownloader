package gaa.cfextractor.filter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.List;

public class SizeProjectFilter extends ProjectFilter {
	
	private int teamThreshold;
	
	
	
	public SizeProjectFilter(List<ProjectInfo> projects, int teamThreshold) {
		super(projects, "*SIZE*");
		this.teamThreshold = teamThreshold;
	}

	@Override
	public List<ProjectInfo> filter() {
		List<ProjectInfo> newList = new ArrayList<ProjectInfo>();
		for (ProjectInfo projectInfo : projects) {
			if (!projectInfo.isFiltered()) {
				if (projectInfo.getNumFiles() < teamThreshold) {
					projectInfo.setFiltered(true);
					String filterInfo = projectInfo.getFilterinfo();
					projectInfo.setFilterinfo(filterInfo == null
							|| filterInfo.isEmpty() ? filterStamp : filterInfo
							+ filterStamp);
				} else
					newList.add(projectInfo);
			}
		}
		return newList;
	}
	

	public int getTeamThreshold() {
		return teamThreshold;
	}

	public void setTeamThreshold(int teamThreshold) {
		this.teamThreshold = teamThreshold;
	}


}
