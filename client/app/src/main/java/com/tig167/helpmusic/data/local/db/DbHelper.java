package com.tig167.helpmusic.data.local.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tig167.helpmusic.data.local.Storage;
import com.tig167.helpmusic.main_app.model.UserFactory;
import com.tig167.helpmusic.util.ImageUtil;
import com.tig167.helpmusic.main_app.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Database helper: local storage.
 * Handles an SQLite database to save and load session data.
 */
public class DbHelper extends SQLiteOpenHelper implements Storage<User> {

    private static final String DATABASE_NAME = "session_user_db";
    private static final String LOG_TAG = DbHelper.class.getSimpleName();
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

    @Override
    public void saveSession(User user) {
        writeSession(user);
    }

    @Override
    public void save(User friend) {
        writeFriend(friend);
    }

    @Override
    public User loadSession() {
        return readUserData();
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

    public void writeSession(User user) {
        deleteOldSessionData();
        String img = "";
        if (user.profileImage() != null) {
            img = ImageUtil.encode(user.profileImage());
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
        this.getWritableDatabase().execSQL("DELETE FROM friend WHERE id > 0;");
        for (User friend : user.friends()) {
            writeFriend(friend);
        }
    }

    private void writeFriend(User friend) {
        SQLiteDatabase db = this.getReadableDatabase();
        String img = "";
        if (friend.profileImage() != null) {
            img = ImageUtil.encode(friend.profileImage());
        }
        final String SQL_INSERT = "INSERT INTO friend (name, email, profile_img) "
                + "VALUES( '"
                + friend.name() + "', '"
                + friend.email() + "', '"
                + img + "');";
        db.execSQL(SQL_INSERT);
    }

    private User readUserData() {
        final String SQL_SELECT = "SELECT name, email, profile_img "
                                + "FROM user;";
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        try (Cursor cursor = db.rawQuery(SQL_SELECT, null)) {
            if (cursor.moveToFirst()) {
                user = UserFactory.create(
                        cursor.getString(0),
                        cursor.getString(1),
                        ImageUtil.decode(cursor.getString(2))
                );
            }
        }
        Log.d(LOG_TAG," read user data\nload friends from storage:");
        if (user != null) {
            for (User friend : readFriends()) {
                user.addFriend(friend);
                Log.d(LOG_TAG, friend.name());
            }
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
                friends.add(UserFactory.create(
                        cursor.getString(0),
                        cursor.getString(1),
                        ImageUtil.decode(cursor.getString(2))
                ));
            }
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