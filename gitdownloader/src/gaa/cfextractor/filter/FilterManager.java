package gaa.cfextractor.filter;

import gaa.dao.ProjectInfoDAO;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.List;

public class FilterManager {
	List<ProjectInfo> projects;
	List<ProjectFilter> filters;
	ProjectFilter mainFilter;
	
	public static void main(String[] args) throws Exception {
		FilterManager filterManager =  new FilterManager(new ProjectInfoDAO().findAll(null));
//		filterManager.addFilter(new NumberOfProjectFilter(filterManager.getProjects(), 500));
//		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjects(), 32));
//		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjects(), 334));
//		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjects(), 45));
//		filterManager.addFilter(new MigrationProjectFilter(filterManager.getProjects(), 2, 0.5f, 20));

		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("java"), 17));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("java"), 230));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("java"), 100));
		
		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("javascript"), 40));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("javascript"), 395));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("javascript"), 41));

		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("php"), 29));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("php"), 380));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("php"), 65));

		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("python"), 26));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("python"), 295));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("python"), 39));
		
		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("ruby"), 71));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("ruby"), 781));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("ruby"), 95));
		
		filterManager.addFilter(new TeamProjectFilter(filterManager.getProjectsByLanguage("c/c++"), 23));
		filterManager.addFilter(new HistoryProjectFilter(filterManager.getProjectsByLanguage("c/c++"), 446));
		filterManager.addFilter(new SizeProjectFilter(filterManager.getProjectsByLanguage("c/c++"), 130));

//		filterManager.setMainFilter(new NewMigrationProjectFilter(filterManager.getProjects(), 1, 0.5f, 20));
		filterManager.setMainFilter(new MigrationProjectFilterUsingLogFilesInfo(filterManager.getProjects(), 1, 0.5f, 20));
		filterManager.addFilter(filterManager.getMainFilter());
		
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
		this.mainFilter.persistFilterInformations();
	}

	public List<ProjectInfo> getProjects() {
		return projects;
	}
	
	private List<ProjectInfo> getProjectsByLanguage(String language) {
		List<ProjectInfo> newProjects = new ArrayList<>();
		for (ProjectInfo projectInfo : projects) {
			if (language.equalsIgnoreCase("c/c++")){
				if (projectInfo.getLanguage().equalsIgnoreCase("c")||projectInfo.getLanguage().equalsIgnoreCase("c++"))
					newProjects.add(projectInfo);
			}
			else if (projectInfo.getLanguage().equalsIgnoreCase(language))
				newProjects.add(projectInfo);
		}
		return newProjects;
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

	public ProjectFilter getMainFilter() {
		return mainFilter;
	}

	public void setMainFilter(ProjectFilter mainFilter) {
		this.mainFilter = mainFilter;
	}
	
}
