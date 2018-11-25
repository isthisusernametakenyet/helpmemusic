package servlet.db;

public class DbAccessData {

	private String dbClass;
	private String dbUrl;
	private String dbUser;
	private String dbPasswd;

	public DbAccessData(String dbClass, String dbUrl, String dbUser, String dbPasswd) {
		this.dbClass = dbClass;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPasswd = dbPasswd;
	}

	public String dbClass() {
		return dbClass;
	}

	public String dbUrl() {
		return dbUrl;
	}

	public String dbUser() {
		return dbUser;
	}

	public String dbPasswd() {
		return dbPasswd;
	}

}
