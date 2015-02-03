package gaa.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Index;

@Entity
@Table(
name = "COMMITINFO",
uniqueConstraints = {@UniqueConstraint(columnNames = {"repositoryName", "sha"})}
)
public class CommitInfo extends AbstractEntity implements Comparable<CommitInfo>{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		protected Long id;
		private String sha;
		@Index(name="REPOSITORYNAMEINDEX")
		private String repositoryName;
		@Lob
		private String message;
		private String name;
		private String email;
		@Temporal(TemporalType.TIMESTAMP)
		private Date date;
		@OneToMany(cascade = { CascadeType.ALL })
		private List<CommitFileInfo> commitFiles;
		
		public CommitInfo() {
			// TODO Auto-generated constructor stub
		}

		public CommitInfo(String repositoryName, String sha, String message, String name,
				String email, Date date, List<CommitFileInfo> commitFiles) {
			super();
			this.repositoryName = repositoryName;
			this.sha = sha;
			this.message = message;
			this.name = name;
			this.email = email;
			this.date = date;
			this.commitFiles = commitFiles;
		}

		public String getSha() {
			return sha;
		}

		public void setSha(String sha) {
			this.sha = sha;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public List<CommitFileInfo> getCommitFiles() {
			return commitFiles;
		}

		public void setCommitFiles(List<CommitFileInfo> commitFiles) {
			this.commitFiles = commitFiles;
		}

		@Override
		public int compareTo(CommitInfo o) {
			return this.date.compareTo(o.date);
		}

		public String getRepositoryName() {
			return repositoryName;
		}

		public void setRepositoryName(String repositoryName) {
			this.repositoryName = repositoryName;
		}
		
		
}
