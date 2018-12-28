package servlet.db;

import java.sql.*;

public class DbUtil {
     
    private static DbUtil instance;
    private static final String PATH = "db_access_data";
    private static final DbAccessData DATA;
    private Connection connection;

    static {
        DATA = new DbAccessDataReader().readDbAccessData(PATH);
        try {
            Class.forName(DATA.dbClass());
        } catch (ClassNotFoundException cnfe) {
            System.err.println("no database found " + cnfe.getMessage());
        }
    }

    public static DbUtil getInstance() {
        if (instance == null) {
            instance = new DbUtil();
        }
        return instance;
    }

    public Connection connection() throws SQLException {
        return DriverManager.getConnection(
                    DATA.dbUrl(),
                    DATA.dbUser(),
                    DATA.dbPasswd());
    }

    public void disconnect() {
        try {
            connection.close();
	} catch (SQLException sqle) {
            System.err.println("connection not closed " + sqle.getMessage());
	}
    }

    public static String createFriendTableName(String email) {
        StringBuilder tableName = new StringBuilder(email);
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@' || email.charAt(i) == '.') {
                tableName.setCharAt(i, '_');
            }
        }
        return tableName.toString();
    }

}
