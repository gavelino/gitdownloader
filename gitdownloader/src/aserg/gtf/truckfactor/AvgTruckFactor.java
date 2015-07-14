package aserg.gtf.truckfactor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.authorship.dao.FileDAO;
import aserg.gtf.authorship.dao.RepositoryDAO;
import aserg.gtf.authorship.model.File;

public class AvgTruckFactor extends TruckFactor{
	
	public Map<Integer, Float> getTruckFactor(String repName) {
		Map<Integer, Double> truckDist =  new HashMap<Integer, Double>();
		
		List<File> files =  repDAO.getFiles(repName);
		int numDevelopers = repDAO.getNumDevelopers();
		return null;
	}
	
	
}
