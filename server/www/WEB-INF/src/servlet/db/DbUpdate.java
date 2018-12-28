package servlet.db;

import java.sql.*;

import servlet.model.User;

public class DbUpdate {

    public boolean updateProfileImage(int id, int imageId) {
        final String SQL_UPDATE_IMG = "UPDATE usr SET profileimage = ? WHERE \"id\" = ?";
        DbStatus status = DbStatus.FAILURE;
        try (Connection connection = DbUtil.getInstance().connection();
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_IMG)) {
            ps.setInt(1, imageId);
            ps.setInt(2, id);
            ps.executeUpdate();
            status = DbStatus.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to insert profile image data to database: " 
                    + sqle.getMessage());
            status = DbStatus.FAILURE;
        }
        return status.code();
    }
    
}
