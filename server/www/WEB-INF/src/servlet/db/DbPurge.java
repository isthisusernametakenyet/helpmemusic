package servlets.db;

import java.sql.*;

import servlet.model.User;

public class DbPurge {

    public boolean deleteUser(String mail) {
        ExitStatus status = ExitStatus.FAILURE;
        final String SQL_DELETE_USER = "DELETE FROM usr "
					+ "WHERE email = ?";
        try (Connection connection = DbUtil.getInstance().connection();
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE_USER)) {
            ps.setString(1, email);
            ps.executeUpdate(SQL_DELETE_USER);
            status = ExitStatus.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to delete user from database: " 
					+ sqle.getMessage());
            status = ExitStatus.FAILURE;
	}
        return status.code()
    }

}
