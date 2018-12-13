package servlet.db;

import java.sql.*;
import java.util.List;

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
    
    public boolean insertImage(String imageName, String imageData, String email) {
        Status status = Status.FAILURE;
        final String SQL_INSERT = "INSERT INTO image "
        + "(\"name\", imagebitmap, owner) "
        + "VALUES (?, ?, ?) RETURNING \"id\"";
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        DbSelection selection = new DbSelection();
        DbUpdate update = new DbUpdate();
        int id = selection.readUserId(email);
        int imageId;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(SQL_INSERT);
            pstmt.setString(1, imageName);
            pstmt.setString(2, imageData);
            pstmt.setInt(3, id);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            //System.out.println("returning id: " + imageId);
            if(rs.next()){
                imageId = rs.getInt("id");
                System.out.println("returning id: " + imageId);
                update.updateProfileImage(id, imageId);
            }
            status = Status.SUCCESS;
        } catch(SQLException e){
            System.err.println("unable to insert image data to database: " 
            + e.getMessage());
        } finally {
            try {
                pstmt.close();
            } catch(SQLException e){
                System.err.println("unable to close statement: " 
                + e.getMessage());
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
        DbSelection select = new DbSelection();
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
            status = Status.FAILURE;
        } finally {
            try {
                pstmt.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement "
                        + sqle.getMessage());
                status = Status.FAILURE;
            }
            database.disconnect();
        } 
        return status.value();
    }
    
    public boolean insertFriendship(List<User> users) {
        DbSelection selection = new DbSelection();
        return insertFriend(
        users.get(0),
        selection.readUserId(users.get(1).email()),
        users.get(1).email())
        && insertFriend(
        users.get(1),
        selection.readUserId(users.get(0).email()),
        users.get(0).email());
    }
    
    private boolean insertFriend(User user, int friendId, String friendEmail) {
        String tableName = DbUtil.createFriendTableName(user.email());
        final String SQL_INSERT_FRIENDSHIP = "INSERT INTO ? (friend_id, friend_email) "
        + "VALUES (?, ?)";
        Status status = Status.FAILURE;
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(SQL_INSERT_FRIENDSHIP);
            pstmt.setString(1, tableName);
            pstmt.setInt(2, friendId);
            pstmt.setString(3, friendEmail);
            pstmt.executeUpdate();
            status = Status.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to close statement "
            + sqle.getMessage());
            status = Status.FAILURE;
        } finally {
            try {
                pstmt.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement "
                + sqle.getMessage());
                status = Status.FAILURE;
            }
            database.disconnect();
        }
        return status.value();
    }
    
}
