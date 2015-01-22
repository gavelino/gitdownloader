package gaa.prototype;

import gaa.gitdownloader.DownloaderUtil;
import gaa.model.CommitFile;
import gaa.model.ProjectInfo;
import gaa.model.Status;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import br.ufmg.aserg.topicviewer.control.distribution.DistributionMapCalculator;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMap;
import br.ufmg.aserg.topicviewer.gui.distribution.DistributionMapPanel;
import br.ufmg.aserg.topicviewer.util.UnsufficientNumberOfColorsException;

public class PrototypeMain {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//	static final String DB_URL = "jdbc:mysql://localhost:3306/gitresearch";
	static final String DB_URL = "jdbc:mysql://localhost:3306/";
	//	   static final String DB_URL = "jdbc:mysql://localhost:3306/gitelasticsearch";

	//  Database credentials
	static final String USER = "git";
	static final String PASS = "git";

	static Map<String, HashSet<UserInfoData>> usersInfo = new HashMap<String, HashSet<UserInfoData>>();
	public static void main(String[] args) throws Exception {
		List<ProjectInfo> projectsInfo =  DownloaderUtil.getProjects();
		for (ProjectInfo projectInfo : projectsInfo) {
			String projectName = projectInfo.getFullName();
			
			Rank rank = new Rank(getCommitFiles("gitdownloader", projectName), projectName);	

			distributionMap(getMap(rank), projectName, "Rank "+projectName);
		}
		printProjectUsersInfo();
		
		
	}
	
	private static void printProjectUsersInfo() {
	for (Entry<String, HashSet<UserInfoData>> entry : usersInfo.entrySet()) {
			for (UserInfoData userInfo : entry.getValue()) {
				System.out.format("%s,%s,%d,%.0f,%f\n", entry.getKey(), userInfo.getUserName(), userInfo.getnFiles(), userInfo.getSpread(), userInfo.getFocus());
//				System.out.println(entry.getKey()+","+userInfo.getUserName()+ ","+userInfo.getnFiles() + ","+userInfo.getSpread() + ","+userInfo.getFocus());
			}
		}
		System.out.println();
		
	}

	public static void oldMain(String[] args) throws Exception {
		Rank rank;
//		List<ProjectInfo> projectsInfo =  DownloaderUtil.getProjects();
//		Map<String, List<CommitFile>> map = DownloaderUtil.getCommitFiles(projectsInfo);
//		Entry<String,List<CommitFile>> entry =  map.entrySet().iterator().next();
//		
//		String projectName = entry.getKey();
//		System.out.println(projectName);

//		String projectName = "elasticsearch/elasticsearch";
		String projectName = "gavelino/gitresearch";
		
//		rank = new Rank(entry.getValue(), projectName);
		rank = new Rank(getCommitFiles("gitdownloader", projectName), projectName);	

		distributionMap(getMap(rank), projectName, "Rank "+projectName);

		//		System.out.println("\n\nJUnit");
		//		rank = new Rank(getCommitFiles("gitjunit"), "gitjunit");		
		//		distributionMap(getMap(rank), "Rank JUnit");

		//		System.out.println("\n\nElasticSearch");
		//		rank = new Rank(getCommitFiles("gitelasticsearch"), "gitelasticsearch");		
		//		distributionMap(getMap(rank), "Rank ElasticSearch");


//		System.out.println();
//		distributionMap(getAuthorMap(rank), "Author");
//
//		for (String packageName : rank.getJavaPackages()) {
//			System.out.println(packageName);
//		}
//		System.out.println("total = "+rank.getJavaPackages().size());
		
		//		for (UserFileRank userFile : rank.getCompleteRank()) {
		//			System.out.println(userFile);
		//		}
		//		System.out.println("\n------------------------------\n");
		//		for (String projectName : Util.getProjectNames()) {
		//			System.out.println("Projeto:  "+projectName);
		//			for (String user : Util.getAllUsers(projectName)) {
		////				System.out.println("user = " + user);
		//				printUserFiles(rank, user);
		//			}
		//		}
	}






	private static Map<String, Set<String>> getMap(Rank rank){
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		List<UserFileRank> userFiles = rank.getOnlyJavaFilesRank();
		for (UserFileRank userFile : userFiles) {
			if (!map.containsKey(userFile.getUser()))
				map.put(userFile.getUser(), new HashSet<String>());
			map.get(userFile.getUser()).add(userFile.getFilename());
		}
		return map;
	}
	private static Map<String, Set<String>> getAuthorMap(Rank rank){
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		List<UserFileRank> userFiles = rank.getOnlyJavaFilesRankAuthor();
		for (UserFileRank userFile : userFiles) {
			if (!map.containsKey(userFile.getUser()))
				map.put(userFile.getUser(), new HashSet<String>());
			map.get(userFile.getUser()).add(userFile.getFilename());
		}
		return map;
	}
	private static void printUserFiles(Rank rank, String user) {
		for (UserFileRank userFile : rank.getCompleteRank()) {
			String filename = userFile.getFilename();

			if (user.equals(userFile.getUser()) && filename.substring(filename.lastIndexOf('.')+1, filename.length()).equals("java")) 
				System.out.print(filename+",");
		}
		System.out.println();
	}

	public static List<CommitFile> getCommitFiles(String database, String projectName) {
		Connection conn = null;
		Statement stmt = null;
		List<CommitFile> cFiles = new ArrayList<CommitFile>();

		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL+database,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
//			sql = "SELECT gcuser.DATE, gfile.FILENAME, gfile.STATUS, gCuser.EMAIL, gfile.ADDITIONS, gfile.DELETIONS, grc.SHA, grc.COMMIT_ID ,gcommit.MESSAGE FROM gitrepositorycommit_gitcommitfile gg "
//					+ "JOIN gitrepositorycommit grc ON (grc.SHA = gg.GitRepositoryCommit_SHA) "
//					+ "JOIN gitcommitfile gfile ON (gfile.ID = gg.files_ID) "
//					+ "JOIN gitcommit gcommit on grc.COMMIT_ID = gcommit.ID "
//					+ "JOIN gitcommituser gcuser on gcommit.AUTHOR_ID = gcuser.ID;";
			sql = "SELECT cf.* FROM  gitproject_commitfile git_commit "
					+ "JOIN commitfile cf on git_commit.commitFiles_ID = cf.ID "
					+ "JOIN gitproject gp on git_commit.GitProject_ID = gp.ID "
					+ "where gp.PROJECTINFO_FULLNAME = "
					+ "\""+ projectName + "\";"  ;
			ResultSet rs = stmt.executeQuery(sql);
			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				String oldFileName = rs.getString("oldfilename");
				String newFileName = rs.getString("newfilename");
				String status = rs.getString("status");
				//				String login = rs.getString("name");
				//				String login = rs.getString("email");
				String login = rs.getString("email").split("@")[0];
				int additions  = rs.getInt("additions");
				int deletions = rs.getInt("deletions");
				String sha = rs.getString("newfilesha");
				int commitId  = rs.getInt("commitid");
				String message = rs.getString("message");
				//				Date date = rs.getDate("date");
				Timestamp time = rs.getTimestamp("date");

				//				String simpleFileName = fileName.substring(fileName.lastIndexOf('/')+1,fileName.length());
				cFiles.add(new CommitFile(time, oldFileName, newFileName, Status.getStatus(status), 
						login, additions, deletions, sha, commitId, message));
			}
			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		return cFiles;
	}




	static void distributionMap(Map<String, Set<String>> maps, String projectName, String mapName){
		HashSet<UserInfoData> projectUsersInfo;
		if (usersInfo.containsKey(projectName))
			projectUsersInfo = usersInfo.get(projectName);
		else{
			projectUsersInfo = new HashSet<UserInfoData>();
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
				name = name.replace(".java", "");
				name = name.replace("/", ".");
				String className = getClassName(name);
				sTopics[i++] = className;
				dm.put(getPackageName(name), className, index, 0.0);
			}
			index++;
			projectUsersInfo.add(new UserInfoData(entry.getKey(), stCount, entry.getValue()));
			semanticTopics[stCount++] = sTopics;

		}
		try {
			dm = DistributionMapCalculator.addSemanticClustersMetrics(dm, maps.size());
			DistributionMapPanel dmPanel = new DistributionMapPanel(dm,semanticTopics);
			calcDMValues(dm, projectUsersInfo);
			JFrame frame = new JFrame("DistributionMap - "+mapName);
			JScrollPane scrollPane = new JScrollPane(dmPanel);  
			//			scrollPane.setBorder(BorderFactory.createTitledBorder("DistributionMap"));
			frame.setContentPane(scrollPane);
			frame.setVisible(true);
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



	private static void calcDMValues(DistributionMap dm, HashSet<UserInfoData> projectUsersInfo) {
		for (UserInfoData userInfo : projectUsersInfo) {
			userInfo.setFocus(dm.getFocus(userInfo.getIndex()));
			userInfo.setSpread(dm.getSpread(userInfo.getIndex()));
		}
	}



	static String getClassName(String id) {
		// TODO Auto-generated method stub
		if (id.contains("."))
			return id.substring(id.lastIndexOf('.')+1);
		else
			return id;
	}
	static String getPackageName(String id) {
		if (id.contains("."))
			return id.substring(0, id.lastIndexOf('.'));
		else
			return id;
	}
}
