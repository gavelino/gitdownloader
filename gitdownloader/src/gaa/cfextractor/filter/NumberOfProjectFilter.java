package gaa.cfextractor.filter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberOfProjectFilter extends ProjectFilter {
	
	private int numberThreshold;
	
	
	
	public NumberOfProjectFilter(List<ProjectInfo> projects, int numberThreshold) {
		super(projects, "*NUMBERPROJECTS"+numberThreshold+"*");
		this.numberThreshold = numberThreshold;
	}

	@Override
	public List<ProjectInfo> filter() {
		List<ProjectInfo> newList = new ArrayList<ProjectInfo>();
		ProjectInfo projectArray[] = new ProjectInfo[projects.size()];
		int i=0;
		for (ProjectInfo projectInfo : projects) {
			projectArray[i++] = projectInfo;
		}		
		Arrays.sort(projectArray);
		int numRemove = projectArray.length - numberThreshold;
		for (i = 0; i < projectArray.length; i++) {
			if (i<numRemove) {
				projectArray[i].setFiltered(true);
				String filterInfo = projectArray[i].getFilterinfo();
				projectArray[i].setFilterinfo(filterInfo == null
						|| filterInfo.isEmpty() ? filterStamp : filterInfo
						+ filterStamp);
			} else
				newList.add(projectArray[i]);
		}
		projects.clear();
		for (ProjectInfo projectInfo : projectArray) {
			projects.add(projectInfo);
		}
		return newList;
	}
	

	public int getNumberThreshold() {
		return numberThreshold;
	}

	public void setNumberThreshold(int teamThreshold) {
		this.numberThreshold = teamThreshold;
	}


}
