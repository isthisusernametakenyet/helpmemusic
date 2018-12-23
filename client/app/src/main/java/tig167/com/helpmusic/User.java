package tig167.com.helpmusic;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
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

    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public String toString() { return name + " " + email; }

    public Bitmap getProfileImage() {return profileImage;}

    //TODO: how shuld we handel the list??
    public List<User> friends(){return friends;}

    public void setProfileImage(Bitmap profileImage){ this.profileImage = profileImage; }

}
