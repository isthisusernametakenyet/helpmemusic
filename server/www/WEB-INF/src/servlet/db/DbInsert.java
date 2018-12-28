package servlet.db;

import java.sql.*;
import java.util.List;

import servlet.model.User;

public class DbInsert {

    private static DbUtil database;
    private DbStatus status;
    
    public DbInsert() {
        database = DbUtil.getInstance();
    }

    public boolean insertImage(String imageName, String imageData, String email) {
        status = DbStatus.FAILURE;
        final String SQL_INSERT_IMG = "INSERT INTO image "
        + "(name, imagebitmap, owner) "
        + "VALUES (?, ?, ?) RETURNING \"id\"";
        int id = new DbSelection().readUserId(email);
        int imageId;
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_IMG)) {
            System.out.println(imageName);
            ps.setString(1, imageName);
            System.out.println(imageData);
            ps.setString(2, imageData);
            System.out.println(email + " | " + id);
            ps.setInt(3, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    imageId = rs.getInt("id");
                    System.out.println("returning id: " + imageId);
                    new DbUpdate().updateProfileImage(id, imageId);
                    status = DbStatus.SUCCESS;
                }
            }
        } catch (SQLException e) {
            System.err.println("unable to insert image data to database: " 
            + e.getMessage());
            status = DbStatus.FAILURE;
        }
        return status.code();
    }
    
    public boolean insertUser(User user) {
        status = DbStatus.FAILURE;
        final String SQL_INSERT_USER =
        "INSERT INTO usr "
        + "(name, email, password) "
        + "VALUES (?, ?, ?)";
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_USER)) {
            ps.setString(1, user.name());
            ps.setString(2, user.email());
            ps.setString(3, user.password());
            ps.executeUpdate();
            status = DbStatus.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to insert into database " 
                        + sqle.getMessage());
            status = DbStatus.FAILURE;
        } 
        return status.code();
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
        status = DbStatus.FAILURE;
        String tableName = DbUtil.createFriendTableName(user.email());
        final String SQL_INSERT_FRIENDS = "INSERT INTO " + tableName + " (friend_id, friend_email) "
        + "VALUES (?, ?)";
        try (Connection connection = database.connection();
            PreparedStatement pstmt = connection.prepareStatement(SQL_INSERT_FRIENDS)) {
            pstmt.setInt(1, friendId);
            pstmt.setString(2, friendEmail);
            pstmt.executeUpdate();
            status = DbStatus.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to close statement "
                        + sqle.getMessage());
            status = DbStatus.FAILURE;
        }
        return status.code();
    }
    
}
