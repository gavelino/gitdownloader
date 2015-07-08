package gaa.truckfactor;

import gaa.authorship.dao.FileDAO;
import gaa.authorship.dao.RepositoryDAO;
import gaa.authorship.model.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckFactor {
	public static void main(String[] args) {
		Map<Integer, Double> truckDist = getTruckFactor("git/git");
	}

	private static Map<Integer, Double> getTruckFactor(String repName) {
		Map<Integer, Double> truckDist =  new HashMap<Integer, Double>();
		RepositoryDAO repDAO = new RepositoryDAO();
		List<File> files =  repDAO.getFiles(repName);
		
		return null;
	}
	
	
}
