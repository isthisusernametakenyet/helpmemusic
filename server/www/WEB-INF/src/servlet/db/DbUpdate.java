package servlet.db;

import java.sql.*;

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
        final String SQL_UPDATE = "UPDATE "
    }
}