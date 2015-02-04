package gaa.cfextractor.filter;

import gaa.dao.CommitFileDAO;
import gaa.dao.ProjectInfoDAO;
import gaa.model.CommitFileInfo;
import gaa.model.ProjectInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MigrationProjectFilter extends ProjectFilter {

	private float percFilesThreshold;
	private int nCommitsThreshold;
	
	
	public MigrationProjectFilter(List<ProjectInfo> projects, float percFilesThreshold, int nCommitsThreshold) {
		super(projects, "*MIGRATION*");
		this.percFilesThreshold = percFilesThreshold;
		this.nCommitsThreshold = nCommitsThreshold;
	}

	@Override
	public List<ProjectInfo> filter() {
		List<ProjectInfo> newList = new ArrayList<ProjectInfo>();
		CommitFileDAO cfiDAO = new CommitFileDAO();
		System.out.println(new Date());
		for (ProjectInfo projectInfo : projects) {
			System.out.println(projectInfo.getFullName());
			List<Long> listNumAddCommitFiles = cfiDAO
					.getAddsCommitFile(projectInfo.getFullName());
			int sum = 0;
			int count = 0;
			long totalCommitFiles = getNumCommitFiles(listNumAddCommitFiles);
			while (count < nCommitsThreshold
					&& count < listNumAddCommitFiles.size()) {
				sum += listNumAddCommitFiles.get(count++);
				if (sum > totalCommitFiles * percFilesThreshold)
					break;
			}
			if (sum > totalCommitFiles * percFilesThreshold) {
				System.out.format("%s %d de %d em %d\n",
						projectInfo.getFullName(), sum,
						totalCommitFiles, count);
				projectInfo.setFiltered(true);
				String filterInfo = projectInfo.getFilterinfo();
				projectInfo.setFilterinfo(filterInfo == null
						|| filterInfo.isEmpty() ? filterStamp : filterInfo
								+ filterStamp);
			} else
				newList.add(projectInfo);

		}
		System.out.println(new Date());
		return newList;
	}

	private Long getNumCommitFiles(List<Long> listNumAddCommitFiles) {
		long sum= 0;
		for (Long num : listNumAddCommitFiles) {
			sum+=num;
		}
		return sum;
	}

	public float getPercFilesThreshold() {
		return percFilesThreshold;
	}

	public void setPercFilesThreshold(float percFilesThreshold) {
		this.percFilesThreshold = percFilesThreshold;
	}

	public int getnCommitsThreshold() {
		return nCommitsThreshold;
	}

	public void setnCommitsThreshold(int nCommitsThreshold) {
		this.nCommitsThreshold = nCommitsThreshold;
	}
	
	

}
