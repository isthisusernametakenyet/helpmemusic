package tig167.com.helpmusic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "client_users";
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
        final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS user( "
                                      + "id INTEGER PRIMARY KEY, "
                                      + "name TEXT, "
                                      + "email TEXT UNIQUE, "
                                      + "profile_img TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
        createFriendTable(db, SessionObject.getInstance().user().email());
    }

    public void createFriendTable(SQLiteDatabase db, String tableName) {
        final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName + " ( "
                                      + "friend_id INTEGER PRIMARY KEY, "
                                      + "user_id INTEGER, "
                                      + "friend_email TEXT UNIQUE, "
                                      + "FOREIGN KEY (user_id) REFERENCES user(id));";
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void write(User user) {
        final String SQL_INSERT = "INSERT INTO user (name, email, profile_img) "
                                + "VALUES ('"
                                + user.name() + "' , '"
                                + user.email() + "' , '"
                                + new Image().encode(user.profileImage()) + "');";
        this.getWritableDatabase().execSQL(SQL_INSERT);
        writeFriends(user);
    }

    private void writeFriends(User user) {
        int id = getUserId(user.email());
        SQLiteDatabase db = this.getReadableDatabase();
        for (User friend : user.friends()) {
            final String SQL_INSERT = "INSERT INTO " + user.email() + "( "
                    + "email, user_id )"
                    + "VALUES( '"
                    + friend.name() + "', '"
                    + friend.email() + "', '"
                    + new Image().encode(friend.profileImage()) + "', "
                    + id + ");";
            db.execSQL(SQL_INSERT);
        }
    }

    private int getUserId(String email) {
        final String SQL_SELECT = "SELECT id FROM user WHERE email = '" + email + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
        }
        return id;
    }

    public User read(String email) {
        final String SQL_SELECT = "SELECT name, email, profile_img "
                                + "FROM user "
                                + "WHERE email = '" + email + "';";
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
            user.setFriends(readFriends(user.email()));
        }
        return user;
    }

    private List<User> readFriends(String tableName) {
        final String SQL_SELECT = "SELECT name, email, profile_img "
                                + "FROM " + tableName + " "
                                + "JOIN user ON user_id = user(id);";
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> friends = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            friends.add(new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    new Image().decode(cursor.getString(2))
            ));
        }
        return friends;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<String> tables = new ArrayList<>();
        final String SQL_SELECT = "SELECT email FROM user;";
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }
        }
        for (String tableName : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
        }
        db.execSQL("DROP TABLE IF EXISTS user;");
        onCreate(db);
    }
}
