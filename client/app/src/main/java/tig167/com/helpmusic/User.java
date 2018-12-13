package tig167.com.helpmusic;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private Bitmap profileImage;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public String toString() { return name + " " + email; }

    public Bitmap getProfileImage() {return profileImage;}

    public void setProfileImage(Bitmap profileImage){ this.profileImage = profileImage; }
}
