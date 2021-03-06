package com.tig167.helpmusic.main_app.model;

import android.graphics.Bitmap;

/**
 * Responsible for creation of user object.
 */
public class UserFactory {

    public static User create(String name, String email) {
        return new User(name, email);
    }

    public static User create(String name, String email, Bitmap profileImage) {
        return new User(name, email, profileImage);
    }

}
