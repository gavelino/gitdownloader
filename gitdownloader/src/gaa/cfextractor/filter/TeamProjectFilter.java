package gaa.cfextractor.filter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.List;

public class TeamProjectFilter extends ProjectFilter {
	
	private float teamThreshold;
	private float commitsThreshold;
	
	
	
	public TeamProjectFilter(List<ProjectInfo> projects, float teamThreshold, float commitsThreshold) {
		super(projects, "*TEAMFILTER*");
		this.teamThreshold = teamThreshold;
		this.commitsThreshold = commitsThreshold;
	}

	@Override
	public List<ProjectInfo> filter() {
		List<ProjectInfo> newList = new ArrayList<ProjectInfo>();
		for (ProjectInfo projectInfo : projects) {
			if (projectInfo.getNumAuthors()<teamThreshold){
				projectInfo.setFiltered(true);
				projectInfo.setFilterinfo(projectInfo.getFilterinfo() + filterStamp);
			}	
			else
				newList.add(projectInfo);
		}
		return newList;
	}
	

	

	public float getCommitsThreshold() {
		return commitsThreshold;
	}

	public void setCommitsThreshold(float commitsThreshold) {
		this.commitsThreshold = commitsThreshold;
	}

	public float getTeamThreshold() {
		return teamThreshold;
	}

	public void setTeamThreshold(float teamThreshold) {
		this.teamThreshold = teamThreshold;
	}


}
