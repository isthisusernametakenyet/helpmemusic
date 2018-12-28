package tig167.com.helpmusic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "client_session";
    private static final int DATABASE_VERSION = 1;
    private static DbHelper instance;

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context);
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
    }

    private void createUserTable(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user ( "
                                      + "id INTEGER PRIMARY KEY, "
                                      + "name TEXT, "
                                      + "email TEXT UNIQUE, "
                                      + "profile_img TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
        createFriendTable(db);
    }

    public void createFriendTable(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS friend ( "
                                      + "id INTEGER PRIMARY KEY, "
                                      + "name TEXT, "
                                      + "email TEXT UNIQUE, "
                                      + "profile_img TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void deleteOldSessionData() {
        this.getWritableDatabase().execSQL("DELETE FROM user WHERE id > 0;");
        this.getWritableDatabase().execSQL("DELETE FROM friend WHERE id > 0;");
    }

    public void write(User user) {
        deleteOldSessionData();
        String img = "";
        if (user.profileImage() != null) {
            img = new Image().encode(user.profileImage());
        }
        final String SQL_INSERT = "INSERT INTO user (name, email, profile_img) "
                                + "VALUES ('"
                                + user.name() + "' , '"
                                + user.email() + "' , '"
                                + img + "');";
        this.getWritableDatabase().execSQL(SQL_INSERT);
        writeFriends(user);
    }

    private void writeFriends(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (User friend : user.friends()) {
            String img = "";
            if (user.profileImage() != null) {
                img = new Image().encode(user.profileImage());
            }
            final String SQL_INSERT = "INSERT INTO friend (name, email, profile_img) "
                    + "VALUES( '"
                    + friend.name() + "', '"
                    + friend.email() + "', '"
                    + img + "');";
            db.execSQL(SQL_INSERT);
        }
        for (User f : user.friends()) {
            System.out.println("friend: " + f.name());
        }
    }

    public User read() {
        final String SQL_SELECT = "SELECT name, email, profile_img "
                                + "FROM user;";
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            if (cursor.moveToFirst()) {
                user = new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        new Image().decode(cursor.getString(2))
                );
            }
        }
        if (user != null) {
            user.setFriends(readFriends());
        }
        return user;
    }

    private List<User> readFriends() {
        final String SQL_SELECT = "SELECT name, email, profile_img "
                                + "FROM friend;";
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> friends = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            if (cursor.moveToFirst()) {
                friends.add(new User(
                        cursor.getString(0),
                        cursor.getString(1),
                        new Image().decode(cursor.getString(2))
                ));
            }
        }
        for (User f : friends) {
            System.out.println("friend: " + f.name());
        }
        return friends;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS friend;");
        db.execSQL("DROP TABLE IF EXISTS user;");
        onCreate(db);
    }
}