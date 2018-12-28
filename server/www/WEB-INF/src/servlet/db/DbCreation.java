package servlet.db;

import java.sql.*;

import servlet.model.User;

public class DbCreation {

    public boolean createFriendTable(String email) {
        DbStatus status = DbStatus.FAILURE;
        String tableName = DbUtil.createFriendTableName(email);
        final String SQL_CREATE = "CREATE TABLE " + tableName + " ( "
                                + "id SERIAL PRIMARY KEY, "
                                + "friend_id INTEGER REFERENCES usr(id), "
                                + "friend_email TEXT UNIQUE "
                                + ")";
        try (Connection connection = DbUtil.getInstance().connection();
            Statement st = connection.createStatement()) {
            st.executeUpdate(SQL_CREATE);
            status = DbStatus.SUCCESS;
        } catch (SQLException sqle) {
            System.err.println("unable to create friend-table "
                    + sqle.getMessage());
            status = DbStatus.FAILURE;
        }
        return status.code();
    }

}
