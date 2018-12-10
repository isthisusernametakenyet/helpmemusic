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

    public boolean updateProfileImage(int id, int imageId){
        Status status = Status.FAILURE;
        DbUtil database = new DbUtil();
        Connection conn = database.connection();
        //DbSelection selection = new DbSelection();
        //int id = selection.readUserId(email);
	    //int imageId = selection.profileImage(id);
	    final String SQL_UPDATE = "UPDATE usr SET profileimage = ? WHERE \"id\" = " + id;
        PreparedStatement pstm = null;
        try{
            pstm = conn.prepareStatement(SQL_UPDATE);
        
            pstm.setInt(1, imageId);
            pstm.executeUpdate();
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
