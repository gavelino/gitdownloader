package aserg.gtf.truckfactor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aserg.gtf.authorship.dao.FileDAO;
import aserg.gtf.authorship.dao.RepositoryDAO;
import aserg.gtf.authorship.model.File;

public abstract class TruckFactor {
	RepositoryDAO repDAO;
	
	public TruckFactor() {
		repDAO = new RepositoryDAO();
	}
	
	public abstract Map<Integer, Float> getTruckFactor(String repName);
	
	
}
