package gaa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AUTH;

import gaa.authorship.FileAuthors;
import gaa.authorship.dao.DeveloperDAO;
import gaa.authorship.dao.FileDAO;
import gaa.authorship.dao.RepositoryDAO;
import gaa.authorship.model.AuthorshipInfo;
import gaa.authorship.model.DevStatus;
import gaa.authorship.model.Developer;
import gaa.authorship.model.Repository;
import gaa.authorship.model.RepositoryStatus;

public class AliasesIdentifier {
	
	
	
	public static void main(String[] args) {
		RepositoryDAO repDAO = new RepositoryDAO();
		DeveloperDAO devDAO = new DeveloperDAO();
		//StringUtils.getLevenshteinDistance("", "");
		
		for (Repository rep : repDAO.findAll()) {
			if (rep.getStatus() != RepositoryStatus.REMOVED) {
				List<Developer> developers = devDAO.getAllDevelopers(rep
						.getFullName());
				Map<Developer, List<Developer>> aliases = findAliases(
						developers, 1, 3);
				List<Developer> devAliases = treatAliases(rep.getFullName(),
						aliases);
				//updateDeveloperAliases(devDAO, devAliases);
			}
		}
	}

	

	private static void updateDeveloperAliases(DeveloperDAO devDAO,
			List<Developer> aliases) {
		for (Developer alias : aliases) {
			devDAO.update(alias);
		}
	}

	

	private static List<Developer> treatAliases(String repName, Map<Developer, List<Developer>> aliases) {
		List<Developer> devAliases = new ArrayList<Developer>();
		for (Entry<Developer, List<Developer>> entry : aliases.entrySet()) {
			Developer dev1 = entry.getKey();
			for (Developer dev2 : entry.getValue()) {
				if (NotAlias.isAlias(repName, dev1.getName(), dev2.getName())){
 					System.out.println(repName + ";" + dev1.getName() + ";"+dev2.getName());
 					dev2.setNewUserName(dev1.getName());
 					Developer tempDev = mergeAliasesAuthorship(dev1, dev2);
 					devAliases.add(dev1);
 					devAliases.add(dev2);
 					devAliases.add(tempDev);
 					dev1  = tempDev;
 					
				}
				else{
					System.err.println(repName + ";" + dev1.getName() + ";"+dev2.getName());
					
				}
			}
			//System.out.println();
			
		}
		return devAliases;
	}

	private static Developer mergeAliasesAuthorship(Developer dev1, Developer dev2) {
		Developer tempDev =  new Developer(dev1.getName(), dev1.getEmail(), dev1.getUserName(), DevStatus.TEMPORARY);
		tempDev.setAuthorshipInfos(createNewAuthorshipList(dev1.getAuthorshipInfos(), tempDev));
		for (AuthorshipInfo authorshipInfo : dev2.getAuthorshipInfos()) {
			AuthorshipInfo mergeAuthorship =  cloneAuthorship( getAuthorship(authorshipInfo.getFile().getPath(), tempDev.getAuthorshipInfos()), tempDev);
			if (mergeAuthorship == null){
				mergeAuthorship = cloneAuthorship(authorshipInfo, tempDev);
			}
			else{
				if (authorshipInfo.isFirstAuthor())
					mergeAuthorship.setAsFirstAuthor();
				if (authorshipInfo.isSecondaryAuthor() && !mergeAuthorship.isFirstAuthor())
					mergeAuthorship.setAsSecondaryAuthor();
				mergeAuthorship.setnDeliveries(mergeAuthorship.getnDeliveries()+authorshipInfo.getnDeliveries());
				mergeAuthorship.setnAddDeliveries(mergeAuthorship.getnAddDeliveries()+authorshipInfo.getnAddDeliveries());
				mergeAuthorship.updateDOA();
			}
			tempDev.addAuthorshipInfo(mergeAuthorship);
		}
		dev1.setAsRemoved();
		dev2.setAsRemoved();
		return tempDev;
	}



	private static List<AuthorshipInfo> createNewAuthorshipList(
			List<AuthorshipInfo> authorshipInfos, Developer dev) {
		List<AuthorshipInfo> list = new ArrayList<AuthorshipInfo>();
		for (AuthorshipInfo authorshipInfo : authorshipInfos) {
			list.add(cloneAuthorship(authorshipInfo, dev));
			
		}
		return list;
	}



	private static AuthorshipInfo cloneAuthorship(AuthorshipInfo authorshipInfo, Developer dev) {
		if (authorshipInfo.getDeveloper().getStatus() == DevStatus.TEMPORARY)
			return authorshipInfo;
		if(authorshipInfo == null)
			return null;
		AuthorshipInfo newAuthorshipInfo = new AuthorshipInfo(authorshipInfo.getFile(), dev);
		if (authorshipInfo.isFirstAuthor())
			newAuthorshipInfo.setAsFirstAuthor();
		if (authorshipInfo.isSecondaryAuthor())
			newAuthorshipInfo.setAsSecondaryAuthor();
		newAuthorshipInfo.setnAddDeliveries(authorshipInfo.getnAddDeliveries());
		newAuthorshipInfo.setnDeliveries(authorshipInfo.getnDeliveries());
		newAuthorshipInfo.updateDOA();
		return newAuthorshipInfo;
	}



	private static AuthorshipInfo getAuthorship(String path,
			List<AuthorshipInfo> list) {
		for (AuthorshipInfo authorshipInfo : list) {
			if (authorshipInfo.getFile().getPath().equals(path))
				return authorshipInfo;
		}
		return null;
	}



	private static boolean namesAreDifferent(String name, List<Developer> value) {
		for (Developer developer : value) {
			if(!developer.getName().equals(name))
				return true;
		}
		return false;
	}

	private static Map<Developer, List<Developer>> findAliases(List<Developer> allDevelopers, int distance, int minSize) {
		int newDistance = distance;
		List<Developer> copyList =  new CopyOnWriteArrayList<Developer>(allDevelopers);
		Map<Developer, List<Developer>> aliases =  new HashMap<Developer, List<Developer>>();
		for (Developer developer1 : copyList) {
			copyList.remove (developer1);
			for (Developer developer2 : copyList) {
				if(developer1.getId()!=developer2.getId() && developer1.getName().length()>=minSize){
					int localDistance = StringUtils.getLevenshteinDistance(convertToUTFLower(developer1.getName()), convertToUTFLower(developer2.getName()));
					if (distance == -1){
						newDistance = developer1.getName().split(" ").length;
					}
					if (!developer1.getName().equals(developer2.getName()) && localDistance<=newDistance){
						if(!aliases.containsKey(developer1))
							aliases.put(developer1, new ArrayList<Developer>());
						aliases.get(developer1).add(developer2);
						copyList.remove(developer2);
					}
				}
			}
		}
		return aliases;
	}

	private static CharSequence convertToUTFLower(String str) {
		String ret = null;
		try {
			ret = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		}
		catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return ret.toLowerCase();
	}



	private static void insert(Directory mainDirectory, String[] names, String author) {
		Directory insertDirectory = mainDirectory;
		for(int i=0;i<(names.length-1); i++ ){
			if (!insertDirectory.getDirectoryMap().containsKey(names[i]))
				insertDirectory.getDirectoryMap().put(names[i], new Directory(names[i]));
			insertDirectory = insertDirectory.getDirectoryMap().get(names[i]);
		}
		String fileName = names[names.length-1];
		insertDirectory.getDirectoryMap().put(fileName, new FileDirectory(fileName, author));
		
	}
	
	 
	
}
