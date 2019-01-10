package com.tig167.helpmusic.main_app;

import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.main_app.model.UserFactory;
import com.tig167.helpmusic.util.DateUtil;

import java.time.LocalDateTime;
import java.time.Period;

/**
 * Represents current session.
 * Holds a reference to a user object, i.e. the owner of current session.
 */
public class SessionObject {

    private static SessionObject instance;
    private User user;
    private LocalDateTime start;

    private SessionObject() {}

    public static SessionObject getInstance() {
        if (instance == null) {
            instance = new SessionObject();
        }
        return instance;
    }

    public void setSessionStart() {
        start = LocalDateTime.now();
    }

    public LocalDateTime getSessionStart() {
        return start;
    }

    public String timeElapsed() {
        return DateUtil.getMinutesSince(start) + " minutes";
    }

    public User user() {
        return user;
    }

    public void setUser(String name, String email) {
        this.user = UserFactory.create(name, email);
    }

    public void setUser(User user) {
        this.user = user;
    }

}
