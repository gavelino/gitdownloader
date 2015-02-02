package gaa.cfextractor;

import gaa.dao.CommitInfoDAO;
import gaa.dao.CommitFileDAO;
import gaa.dao.GitRepositoryDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.gitdownloader.DownloaderUtil;
import gaa.gitdownloader.GitProjectFinder;
import gaa.model.CommitInfo;
import gaa.model.GitRepository;
import gaa.model.ProjectInfo;
import gaa.model.ProjectStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AuthorsExtractor {
	public static void main(String[] args) throws Exception {
		System.out.println(new Date());

		CommitInfoDAO ciDAO = new CommitInfoDAO();
		ProjectInfoDAO piDAO = new ProjectInfoDAO();
		List<ProjectInfo> projects = piDAO.findAll(null);
		for (ProjectInfo projectInfo : projects) {
			if (projectInfo.getNumAuthors()==0) {
				projectInfo.setNumAuthors(ciDAO.getNumberAuthors(projectInfo
						.getFullName()));
				System.out.println(projectInfo.getFullName() + " = "
						+ projectInfo.getNumAuthors());
				piDAO.update(projectInfo);
			}
		}
		
		System.out.println(new Date());
	}		
}
