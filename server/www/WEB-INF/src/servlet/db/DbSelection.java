package servlet.db;

import java.util.*;
import java.sql.*;

import servlet.model.User;

public class DbSelection {
    
    private static DbUtil database;

    public DbSelection() {
        database = DbUtil.getInstance();
    }
    
    public int readUserId(String email) {
        final String SQL_READ_ID = "SELECT \"id\" FROM usr WHERE email = ?";
        int id = -1;
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_READ_ID)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException sqle) {
            System.err.println("unable to read user-id from database: " + sqle.getMessage());
        }
        return id;
    }

    public User getUser(String email) {
        final String SQL_SELECT = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap FROM usr " +
        "LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id " + 
        "WHERE usr.email ILIKE '" + email + "%';";
        User user = null;
        try (Connection connection = database.connection();
            Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(SQL_SELECT)) {
                if (rs.next()) {
                    user = new User(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("id"),
                            rs.getString("imagebitmap")
                    );
                }
            }
        } catch (SQLException sqle) {
            System.err.println("unable to read user from database: " + sqle.getMessage());
        }
        return user;
    }
    
    public User readUser(String email, String password) {
        final String SQL_SELECT_USER = "SELECT * "
        + "FROM usr "
        + "WHERE \"email\" = ? AND \"password\" = ?";
        User user = null;
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_USER)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException sqle) {
            System.err.println("unable to read user from database " + sqle.getMessage());
        }
        return user;
    }
    
    public List<User> readUsers(String name) {
        final String SQL_SELECT_ALL = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap FROM usr " +
        "LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id " + 
        "WHERE usr.name ILIKE '" + name + "%';";
        List<User> users = new ArrayList<User>();
        try (Connection connection = database.connection();
            Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(SQL_SELECT_ALL)) {
                while (rs.next()) {
                    users.add(new User(
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getInt("id"),
                                rs.getString("imagebitmap")
                    ));
                }
                
            }
        } catch (SQLException sqle) {
            System.err.println("unable to select users from database " 
            + sqle.getMessage());
        }
        return users;
    }
    
    public List<User> readFriends(User user) {
        String tableName = DbUtil.createFriendTableName(user.email());
        final String SQL_SELECT_FRIENDS = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap "
        + "FROM " + tableName + " "
        + "JOIN usr "
        + "ON friend_id = usr.id LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id";
        List<User> friends = new ArrayList<>();
        try (Connection connection = database.connection();
            Statement st = connection.createStatement()) {
            try (ResultSet rs = st.executeQuery(SQL_SELECT_FRIENDS)) {
                while (rs.next()) {
                    friends.add(new User(
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getInt("id"),
                                rs.getString("imagebitmap")
                    ));
                }
            }
        } catch (SQLException sqle) {
            System.out.println("unable to read friends from database " 
            + sqle.getMessage());
        }   
        System.out.println("FriendList: " + friends.toString());
        return friends;
    }
    
    public boolean hasUser(String email) {
        return getUser(email) != null;
    }

    public boolean hasUser(String email, String password) {
        return readUser(email, password) != null;
    }
    
    public String readProfileImage(String email){
        String imageData = "";
        final String SQL_SELECT_IMG = "SELECT imagebitmap from image INNER JOIN usr ON image.id = usr.profileimage WHERE owner = ?";
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_IMG)) {
            ps.setInt(1, readUserId(email));
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    imageData = rs.getString("imagebitmap");
                }
            }
        } catch(SQLException sqle) {
            System.err.println("unable to get image data: " + sqle.getMessage());
        }
        return imageData;
    }
    
    public int profileImageId(int owner){
        int imageId = -1;
        final String SQL_SELECT_IMG_ID = "SELECT \"id\" FROM image WHERE owner = ? AND profileimage = true;";
        try (Connection connection = database.connection();
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_IMG_ID)) {
            ps.setInt(1, owner);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    imageId = rs.getInt("\"id\"");
                }
            }
        } catch (SQLException sqle) {
            System.err.println("unable to get imageid: " + sqle.getMessage());
        }
        return imageId;
    }
}
