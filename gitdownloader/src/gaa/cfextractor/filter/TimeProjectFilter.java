package gaa.cfextractor.filter;

import gaa.model.GitProject;
import gaa.model.ProjectInfo;

import java.util.List;

public class TimeProjectFilter extends ProjectFilter {
	
	private float timeThreshold;
	private float commitsThreshold;
	
	
	
	public TimeProjectFilter(List<GitProject> projects, float timeThreshold, float commitsThreshold) {
		super(projects);
		this.timeThreshold = timeThreshold;
		this.commitsThreshold = commitsThreshold;
	}

	@Override
	public List<GitProject> filter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistFilterInformations() {
		// TODO Auto-generated method stub

	}

	public float getTimeThreshold() {
		return timeThreshold;
	}

	public void setTimeThreshold(float timeThreshold) {
		this.timeThreshold = timeThreshold;
	}

	public float getCommitsThreshold() {
		return commitsThreshold;
	}

	public void setCommitsThreshold(float commitsThreshold) {
		this.commitsThreshold = commitsThreshold;
	}

}
