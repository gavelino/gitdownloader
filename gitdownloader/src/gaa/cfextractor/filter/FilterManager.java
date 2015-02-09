package gaa.cfextractor.filter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.List;

public class FilterManager {
	List<ProjectInfo> projects;
	List<ProjectFilter> filters;
	public static void main(String[] args) {
		FilterManager filterManager =  new FilterManager(new ProjectInfoDAO().findAll(null));
		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjects(), 22));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjects(), 241));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjects(), 44));
		filterManager.addFilter(new MigrationProjectFilter(filterManager.getProjects(), 0.5f, 20));
		filterManager.cleanAndFilter();
		filterManager.persistFiltredProjects();
	}
	
	public FilterManager(List<ProjectInfo> projects) {
		this.projects = projects;
		this.filters = new ArrayList<ProjectFilter>(); 
	}
	
	public void addFilter(ProjectFilter filter){
		this.filters.add(filter);
	}

	public List<ProjectInfo> cleanAndFilter(){
		List<ProjectInfo> filteredList = new ArrayList<>();
		for (ProjectInfo projectInfo : projects) {
			projectInfo.setFiltered(false);
			projectInfo.setFilterinfo("");
		}		
//		for (ProjectFilter projectFilter : filters) {
//			projectFilter.clean();
//		}
		for (ProjectFilter projectFilter : filters) {
			filteredList.addAll(projectFilter.filter());
		}
		return filteredList;
	}
	
	public void persistFiltredProjects(){
		filters.get(0).persistFilterInformations();
	}

	public List<ProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(List<ProjectInfo> projects) {
		this.projects = projects;
	}

	public List<ProjectFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<ProjectFilter> filters) {
		this.filters = filters;
	}
	
}
