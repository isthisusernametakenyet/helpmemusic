package com.tig167.helpmusic.main_app.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a user of this application.
 */
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

    /**
     * Accessor for friend data
     *
     * @return  an unmodifiable list of friends
     */
    public List<User> friends() {
        return Collections.unmodifiableList(friends);
    }

    /**
     * Add a friend only if possible
     *
     * @param user  the user to be added to this friend data
     */
    public void addFriend(User user) {
        if (!isUnfriendable(user)) {
            friends.add(user);
        }
    }

    public boolean hasFriend(User user) {
        return friends.contains(user);
    }

    /**
     * Ensure that friend to be added is not this user
     * and not already in the list of friends
     *
     * @param user  the user to be checked against this user
     * @return      the boolean value of garbage data check, true on ok data
     */
    public boolean isUnfriendable(User user) {
        return this.hasFriend(user) || this.equals(user);
    }

    /**
     * compare users by email
     *
     * @param other    the object to be compared with this user
     * @return         the boolean value of email comparison, true on same email
     */
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
