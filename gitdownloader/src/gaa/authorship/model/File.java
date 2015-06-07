package gaa.authorship.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	private String path;
	private String oldFileName;
	private int nChanges=0;
	private int nExtraAdds=0;

	@OneToMany(cascade = { CascadeType.ALL })
	private List<AuthorshipInfo> authorshipInfos = new ArrayList<AuthorshipInfo>();

	@OneToOne
	private AuthorshipInfo bestAuthorshipInfo;
	@OneToOne
	private AuthorshipInfo bestAuthorshipInfoMult;
	@OneToOne
	private AuthorshipInfo bestAuthorshipAddDeliveries;

	private double bestDoa;
	private double bestDoaMult;
	private double bestDoaAddDeliveries;
	public File() {
	}

	public File(String path) {
		super();
		this.path = path;
	}
	
	public int getnChanges() {
		return nChanges;
	}
	
	public void addNewChange(){
		nChanges++;
	}
	
	public void incNChanges(int inc){
		nChanges+=inc;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public List<AuthorshipInfo> getAuthorshipInfos() {
		return authorshipInfos;
	}

	public void setAuthorshipInfos(List<AuthorshipInfo> authorshipInfos) {
		this.authorshipInfos = authorshipInfos;
	}

	public void addAuthorshipInfo(AuthorshipInfo authorshipInfo) {
		this.authorshipInfos.add(authorshipInfo);
		
	}

//	public void addRenamedHistory(File oldFile) {
//		for (AuthorshipInfo oldAuthorshipInfo : oldFile.getAuthorshipInfos()) {
//			boolean flag = true;
//			for (AuthorshipInfo newAuthorshipInfo : this.getAuthorshipInfos()) {
//				if (oldAuthorshipInfo.getDeveloper().getUserName().equalsIgnoreCase(newAuthorshipInfo.getDeveloper().getUserName())){
//					if (oldAuthorshipInfo.isFirstAuthor())
//						newAuthorshipInfo.setFirstAuthor(true);
//					newAuthorshipInfo.incNDeliveries(oldAuthorshipInfo.getnDeliveries());
//					this.incNChanges(oldFile.getnChanges());
//					flag = false;
//				}
//			}
//			if (flag)
//				this.authorshipInfos.add(oldAuthorshipInfo);
//		}
//		
//	}
	

	@Override
	public String toString() {
		return path;
	}

	public AuthorshipInfo getBestAuthorshipInfo() {
		return bestAuthorshipInfo;
	}

	public void setBestAuthorshipInfo(AuthorshipInfo bestAuthorshipInfo) {
		this.bestAuthorshipInfo = bestAuthorshipInfo;
		this.bestDoa = bestAuthorshipInfo.getDOA();
	}

	public AuthorshipInfo getBestAuthorshipInfoMult() {
		return bestAuthorshipInfoMult;
	}

	public void setBestAuthorshipInfoMult(AuthorshipInfo bestAuthorshipInfo) {
		this.bestAuthorshipInfoMult = bestAuthorshipInfo;
		this.bestDoaMult = bestAuthorshipInfo.getDoaMultAuthor();
	}

	public double getBestDoa() {
		return bestDoa;
	}

	protected void setBestDoa(double bestDoa) {
		this.bestDoa = bestDoa;
	}

	public double getBestDoaMult() {
		return bestDoaMult;
	}

	public void setBestDoaMult(double bestDoaMult) {
		this.bestDoaMult = bestDoaMult;
	}

	public int getnExtraAdds() {
		return nExtraAdds;
	}

	public void addExtraAdds() {
		this.nExtraAdds++;
	}

	public AuthorshipInfo getBestAuthorshipAddDeliveries() {
		return bestAuthorshipAddDeliveries;
	}

	public void setBestAuthorshipAddDeliveries(
			AuthorshipInfo bestAuthorshipAddDeliveries) {
		this.setBestDoaAddDeliveries(bestAuthorshipAddDeliveries.getDOA());
		this.bestAuthorshipAddDeliveries = bestAuthorshipAddDeliveries;
	}

	public double getBestDoaAddDeliveries() {
		return bestDoaAddDeliveries;
	}

	public void setBestDoaAddDeliveries(double bestDoaAddDeliveries) {
		this.bestDoaAddDeliveries = bestDoaAddDeliveries;
	}
	
}
