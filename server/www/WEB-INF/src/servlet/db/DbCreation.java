package servlet.db;

import java.sql.*;

import servlet.model.User;

public class DbCreation {

    public boolean createFriendTable(String email) {
        
        boolean status = false;
        String tableName = DbUtil.createFriendTableName(email);
        final String SQL_CREATE = "CREATE TABLE " + tableName + " ( "
                                + "id SERIAL PRIMARY KEY, "
                                + "friend_id INTEGER REFERENCES usr(id), "
                                + "friend_email TEXT UNIQUE "
                                + ")";
        DbUtil database = new DbUtil();
        Connection connection = database.connection();
        Statement st = null;
        try {
            st = connection.createStatement();
            st.execute(SQL_CREATE);
            status = true;
        } catch (SQLException sqle) {
            System.err.println("unable to create friend-table "
                    + sqle.getMessage());
            status = false;
        } finally {
            try {
                st.close();
            } catch (SQLException sqle) {
                System.err.println("unable to close statement "
                        + sqle.getMessage());
            }
            database.disconnect();
        }
        return status;
    }

}
