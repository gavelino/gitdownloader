package aserg.gtf.truckfactor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.authorship.dao.FileDAO;
import aserg.gtf.authorship.dao.RepositoryDAO;
import aserg.gtf.authorship.model.File;

public class TruckFactorProgram {
	public static void main(String[] args) {
		TruckFactor truckFactor = new GreedyTruckFactor();
		truckFactor.getTruckFactor("Homebrew/homebrew");
//		for (String repName : new RepositoryDAO().getAllRepositoryNames()) {
//			truckFactor.getTruckFactor(repName);			
//		}
//		
	}

	
	
}
