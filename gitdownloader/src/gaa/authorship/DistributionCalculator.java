package gaa.authorship;

import gaa.authorship.dao.RepositoryDAO;
import gaa.authorship.model.DeveloperAuthorshipInfo;
import gaa.prototype.UserInfoData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapPanel;
import br.ufmg.aserg.topicviewer.util.UnsufficientNumberOfColorsException;

public class DistributionCalculator {

	public static void main(String[] args) {
		RepositoryDAO repoDAO = new RepositoryDAO();
		String repoName = "ThinkUpLLC/ThinkUp";
		distributionMap(repoDAO.getFilesAuthor(repoName), repoName, "Rank "
				+ repoName);
	}
	
	static Map<String, HashSet<DeveloperAuthorshipInfo>> usersInfo = new HashMap<String, HashSet<DeveloperAuthorshipInfo>>();
	static void distributionMap(Map<String, Set<String>> maps, String projectName, String mapName){
		HashSet<DeveloperAuthorshipInfo> projectUsersInfo;
		if (usersInfo.containsKey(projectName))
			projectUsersInfo = usersInfo.get(projectName);
		else{
			projectUsersInfo = new HashSet<DeveloperAuthorshipInfo>();
			usersInfo.put(projectName, projectUsersInfo);
		}
			
		
		HashSet<UserInfoData> userInfo = new HashSet<UserInfoData>();
		DistributionMap dm = new DistributionMap(mapName);
		String semanticTopics[][] = new String[maps.size()][];
		int index = 0;
		int stCount = 0;
		Queue<Entry<String, Set<String>>> queuedMap = getOrderedMap(maps);
		while (!queuedMap.isEmpty()){
			Entry<String, Set<String>> entry = queuedMap.poll();
			//		for (Entry<String, Set<String>> entry : getOrderedMap(maps).entrySet()) {
			String sTopics[] = new String[entry.getValue().size()];
			int i =0;
			for (String name : entry.getValue()) {
//				name = name.replace("/", ".");
				String className = getClassName(name);
				className = className.replace("/", ".");
				sTopics[i++] = className;
				dm.put(getPackageName(name).replace("/", "."), className, index, 0.0);
			}
			index++;
			projectUsersInfo.add(new DeveloperAuthorshipInfo(entry.getKey(), stCount, entry.getValue()));
			semanticTopics[stCount++] = sTopics;

		}
		try {
			dm = DistributionMapCalculator.addSemanticClustersMetrics(dm, maps.size());
			DistributionMapPanel dmPanel = new DistributionMapPanel(dm,semanticTopics);
			calcDMValues(dm, projectUsersInfo);
//			JFrame frame = new JFrame("DistributionMap - "+mapName);
//			JScrollPane scrollPane = new JScrollPane(dmPanel);  
//			frame.setContentPane(scrollPane);
//			frame.setVisible(true);
		} catch (UnsufficientNumberOfColorsException e1) {
			e1.printStackTrace();
		}
	}
	
	private static Queue<Entry<String, Set<String>>> getOrderedMap(
			Map<String, Set<String>> maps) {
		Map<String, Set<String>> clonedMap =  new HashMap<String, Set<String>>();
		Queue<Entry<String, Set<String>>> newMap =  new LinkedList<Entry<String, Set<String>>>();
		for (Entry<String, Set<String>> entry : maps.entrySet()) {
			clonedMap.put(entry.getKey(), entry.getValue());

		}

		while (!clonedMap.isEmpty()) {
			int bigSize = 0;
			Entry<String, Set<String>> bigEntry =  null;
			for (Entry<String, Set<String>> entry : clonedMap.entrySet()) {
				if (entry.getValue().size() > bigSize) {
					bigSize = entry.getValue().size();
					bigEntry = entry;
				}

			}
			if (bigEntry!=null){
				newMap.add(bigEntry);
				clonedMap.remove(bigEntry.getKey());
			}

		}
		return newMap;
	}
	static String getClassName(String id) {
		// TODO Auto-generated method stub
		if (id.contains("/"))
			return id.substring(id.lastIndexOf('/')+1);
		else
			return id;
	}
	static String getPackageName(String id) {
		if (id.contains("/"))
			return id.substring(0, id.lastIndexOf('/'));
		else
			return "";
	}
	private static void calcDMValues(DistributionMap dm, HashSet<DeveloperAuthorshipInfo> projectUsersInfo) {
		for (DeveloperAuthorshipInfo userInfo : projectUsersInfo) {
			userInfo.setFocus(dm.getFocus(userInfo.getIndex()));
			userInfo.setSpread(dm.getSpread(userInfo.getIndex()).intValue());
			System.out.format("%S - files:%d, spread: %d , focus: %f, INDEX: %d\n", userInfo.userName, userInfo.getnFiles(), userInfo.getSpread(), userInfo.getFocus(), userInfo.getIndex());
		}
	}
}
