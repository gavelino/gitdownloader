package aserg.gtf.extractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import aserg.gtf.dao.CommitFileDAO;
import aserg.gtf.dao.CommitInfoDAO;
import aserg.gtf.dao.GitRepositoryDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.gitdownloader.DownloaderUtil;
import aserg.gtf.gitdownloader.GitProjectFinder;
import aserg.gtf.model.CommitInfo;
import aserg.gtf.model.GitRepository;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.ProjectStatus;

public class AuthorExtractor {
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
