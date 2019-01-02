package com.tig167.helpmusic.main_app;

import com.tig167.helpmusic.main_app.model.User;

public class SessionObject {

    private static SessionObject instance;
    private User user;

    private SessionObject() {}

    public static SessionObject getInstance() {
        if (instance == null) {
            instance = new SessionObject();
        }
        return instance;
    }

    public User user() {
        return user;
    }

    public void setUser(String name, String email) {
        this.user = new User(name, email);
    }

    public void setUser(User user) {
        this.user = user;
    }

}