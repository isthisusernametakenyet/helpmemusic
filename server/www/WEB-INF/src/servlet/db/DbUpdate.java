package servlet.db;

import java.sql.*;

import servlet.model.User;

public class DbUpdate{

    private enum Status{
        SUCCESS(true),
        FAILURE(false);

        private boolean value;

        Status(boolean b){
            value = b;
        }

        public boolean value(){
            return value;
        }
    }

    public boolean updateProfileImage(User user){
        Status status = Status.FAILURE;
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        DbSelection selection = new DbSelection();
        int id = selection.readUserId(user.email());
	int imageId = selection.profileImage(id);
	final String SQL_UPDATE = "UPDATE usr SET user_profile_image = ? WHERE user_id = " + id;
        try{
            PreparedStatement pstm = conn.prepareStatement(SQL_UPDATE);
        
            pstm.setInt(1, id);

            status = Status.SUCCESS;
        }
        catch(SQLException e){
            System.err.println("unable to insert profile image data to database: " 
                    + e.getMessage());
        }
        finally{
            try{
                pstm.close();
            }
            catch(SQLException e){
                System.err.println("unable to insert profile image data to database: " 
                    + e.getMessage());
            }
        }

        return status.value();
    }
}
