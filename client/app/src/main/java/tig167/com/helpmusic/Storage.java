package tig167.com.helpmusic;

import android.content.Context;

public class Storage {

    private static Storage instance;
    private static DbHelper database;
    private static SessionObject session;

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    private Storage(Context context) {
        database = DbHelper.getInstance(context);
        session = SessionObject.getInstance();
    }

    public void save() {
        database.write(session.user());
    }

    public void load() {
        session.setUser(database.read(session.user().email()));
    }

}
