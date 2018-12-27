package servlet.db;

import java.util.*;
import java.sql.*;

import servlet.model.User;

public class DbSelection {
    
    public int readUserId(String email) {
        final String SQL_READ_ID = "SELECT \"id\" FROM usr WHERE email = \'" 
        + email + "\'";
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement st = null;
        ResultSet rs = null;
        int id = -1;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(SQL_READ_ID);
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException sqle) {
            System.err.println("unable to read id from db "
            + sqle.getMessage());
        } finally {
            try {
                st.close();
                rs.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement or resultset "
                + sqle.getMessage());
            }
            database.disconnect();
        }
        return id;
    }
    
    public User getUser(String email) {
        final String SQL_SELECT = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap FROM usr " +
        "LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id " + 
        "WHERE usr.email ILIKE '" + email + "%';";
        User user = null;
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(SQL_SELECT);
            if (rs.next()) {
                user = new User(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("id"),
                rs.getString("imagebitmap")
                );
            }
        } catch (SQLException sqle){
            System.err.println("unable to select from database "
            + sqle.getMessage());
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException sqle) {
                System.err.println("unable to select from database " 
                + sqle.getMessage());
            }
            database.disconnect();
        }
        return user;
    }
    
    public User readUser(String email, String password) {
        final String SQL_SELECT = "SELECT * "
        + "FROM usr "
        + "WHERE \"email\" = '" 
        + email + "' AND \"password\" = '" 
        + password + "'";
        User user = null;
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement stmt  = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SQL_SELECT);
            if (rs.next()) {
                user = new User(
                rs.getString("name"),
				rs.getString("email"),
				rs.getString("password")
                );
            }
        } catch (SQLException sqle) {
            System.err.println("unable to select from database " + sqle.getMessage());
            System.exit(1);
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException sqle) {
                System.out.println("unable to close result set or statement " 
                + sqle.getMessage());
            }
            database.disconnect();
        }
        return user;
    }
    
    public List<User> readUsers(String name) {
        final String SQL_SELECT_ALL = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap FROM usr " +
        "LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id " + 
        "WHERE usr.name ILIKE '" + name + "%';";
        List<User> users = new ArrayList<User>();
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement stmt  = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL);
            while (rs.next()) {
                users.add(new User(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("id"),
                rs.getString("imagebitmap")
                ));
                
            }
        } catch (SQLException sqle) {
            System.err.println("unable to select from database " 
            + sqle.getMessage());
            
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close result set "
                + sqle.getMessage());
            }
            database.disconnect();
        }
        System.out.println(users.toString());
        return users;
    }
    
    public List<User> readFriends(User user) {
        String tableName = DbUtil.createFriendTableName(user.email());
        final String SQL_SELECT_FRIENDS = "SELECT usr.id, usr.name, usr.email, usr.password, image.imagebitmap "
        + "FROM " + tableName + " "
        + "JOIN usr "
        + "ON friend_id = usr.id LEFT OUTER JOIN image ON usr.id = image.owner AND usr.profileimage = image.id";
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement statement = null;
        ResultSet rs = null;
        List<User> friends = new ArrayList<>();
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(SQL_SELECT_FRIENDS);
            while (rs.next()) {
                friends.add(new User(
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("id"),
                rs.getString("imagebitmap")
                ));
            }
        } catch (SQLException sqle) {
            System.out.println("unable to select from database " 
            + sqle.getMessage());
            // handle exception
        } finally {
            try {
                statement.close();
                rs.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement or resultset "
                + sqle.getMessage());
                // handle exception
            }
            database.disconnect();
        }   
        System.out.println("FriendList: " + friends.toString());
        return friends;
    }
    
    public boolean hasUser(String email) {
        return getUser(email) != null;
    }
    public boolean hasUser(String email, String password){
        return readUser(email, password) != null;
    }
    
    public String readProfileImage(String email){
        String imageData = "";
        int id = readUserId(email);
        final String SQL_SELECT = "SELECT imagebitmap from image INNER JOIN usr ON image.id = usr.profileimage WHERE owner = " + id;
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        Statement statement = null;
        ResultSet rs = null;
        try{
            statement = conn.createStatement();
            rs = statement.executeQuery(SQL_SELECT);
            while(rs.next()){
                imageData = rs.getString("imagebitmap");
            }
        }
        catch(SQLException e){
            System.err.println("Failed to get image data " + e.getMessage());
        }
        finally{
            try{
                statement.close();
                rs.close();
            }
            catch(SQLException e){
                System.err.println("Failed to close the rasultset and statment " + e.getMessage());
            }
        }
        
        return imageData;
    }
    
    public int profileImageId(int owner){
        int imageId = -1;
        final String SQL = "SELECT \"id\" FROM image WHERE ower = " + owner + " AND profileimage = true;";
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        Statement st = null;
        ResultSet rs = null;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(SQL);
            
            while(rs.next()){
                imageId = rs.getInt("\"id\"");
            }
        }
        catch(SQLException e){
            System.err.println("faild to get imageid");
        }
        finally{
            try{
                st.close();
                rs.close();
            }
            catch(SQLException e){
                System.err.println("faild to close statment and resultset");
            }
        }
        return imageId;
    }
}
