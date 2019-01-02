package com.tig167.helpmusic.main_app.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private String name;
    private String email;
    private Bitmap profileImage;
    private List<User> friends;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        friends = new ArrayList<>();
    }

    public User(String name, String email, Bitmap profileImage) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        friends = new ArrayList<>();
    }

    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public Bitmap profileImage() {return profileImage;}

    public void setProfileImage(Bitmap profileImage){ this.profileImage = profileImage; }

    public List<User> friends() {
        return Collections.unmodifiableList(friends);
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public boolean hasFriend(User user) {
        return friends.contains(user);
    }

    public boolean isUnfriendable(User user) {
        return this.hasFriend(user) || this.equals(user);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof User)) {
            return false;
        }
        User user = (User) other;
        return email.equals(user.email());
    }

    @Override public String toString() { return name + " " + email; }

}
