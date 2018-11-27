package servlet.db;

import java.sql.*;

import servlet.model.User;

public class DbInsert {

    private enum Status {
        SUCCESS(true),
        FAILURE(false);

        private boolean value;

        Status (boolean b) {
            value = b;
        }

        public boolean value() {
            return value;
        }
    }

    public boolean insertImage(String imageName, String imageData, String email, String password){
        Status status = Status.FAILURE;
        final String SQL_INSERT = "INSERT INTO image "
           + "(\"image_name\", image_bitmap_data, image_owner) "
           + "VALUES (?, ?, ?)";
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        DbSelection selection = new DbSelection();

        User user = selection.readUser(email, password);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQL_INSERT);
            pstmt.setString(1, imageName);
            pstmt.setString(2, imageData);
            pstmt.setInt(3, 0);
            pstmt.executeUpdate();
            status = Status.SUCCESS;
        } catch(SQLException e){
            System.err.println("unable to insert image data to database: " 
                    + e.getMessage());
            System.exit(1);
        } finally {
            try {
                pstmt.close();
            } catch(SQLException e){
                System.err.println("unable to close statment: " 
                        + e.getMessage());
                System.exit(1);
            }
        }
        return status.value();
    }

    public boolean insertUser(User user) {

        Status status = Status.FAILURE;

        final String SQL_INSERT =
            "INSERT INTO usr "
            + "(name, email, password) "
            + "VALUES (?, ?, ?)";
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(SQL_INSERT);
            pstmt.setString(1, user.name());
            pstmt.setString(2, user.email());
            pstmt.setString(3, user.password());
            pstmt.executeUpdate();
            status = Status.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to insert into database " 
                    + sqle.getMessage());
            System.exit(1);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement "
                        + sqle.getMessage());
                System.exit(1);
            }
            database.disconnect();
        }
        return status.value();
    }

    public void insertFriendship(String uEmail, String fEmail) {
    
        final String SQL_INSERT_FRIENDSHIP = "INSERT INTO friendship "
            + "(\"usr_email\", \"friend_email\") "
            + "VALUES (?, ?)";
            
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(SQL_INSERT_FRIENDSHIP);
            pstmt.setString(1, uEmail);
            pstmt.setString(2, fEmail);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            System.err.println("unable to close statement "
                    + sqle.getMessage());
		System.exit(1);
        }
        try {
            pstmt = connection.prepareStatement(SQL_INSERT_FRIENDSHIP);    
            pstmt.setString(1, fEmail);
            pstmt.setString(2, uEmail);  
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            System.err.println("unable to close statement "
                    + sqle.getMessage());
		System.exit(1);
	    } finally {
                try {
                    pstmt.close();
                } catch (SQLException sqle) {
                    System.err.println("unable to close statement "
                            + sqle.getMessage());
                    System.exit(1);
            }
            database.disconnect();
        }
    }

}
