package servlets.db;

import java.sql.*;

import servlet.model.User;

public class DbPurge {

	public void deleteUser(String mail) {
		final String SQL_DELETE = "DELETE FROM usr "
					+ "WHERE email = '" 
					+ email 
					+ "'";
		DbUtil database = new DbUtil();
		Connection connection = database.connection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE);
		} catch (SQLException sqle) {
			System.err.println("unable to delete from database " 
					+ sqle.getMessage());
			System.exit(1);
		} finally {
			try {
				statement.close();
			} catch (SQLException sqle) {
				System.err.println("unable to close statement " 
						+ sqle.getMessage());
				System.exit(1);
			}
			database.closeConnection();
		}
	}

}
