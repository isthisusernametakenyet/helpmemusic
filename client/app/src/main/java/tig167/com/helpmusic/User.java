package tig167.com.helpmusic;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable {

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

    public void addFriend(User user) {
        friends.add(user);
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

    public void setFriends(List<User> data) {
        friends.clear();
        friends.addAll(data);
    }

    @Override public String toString() { return name + " " + email; }





}
