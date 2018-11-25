package tig167.com.helpmusic;

public class User {
    private String name;
    private String email;

    public User(String name, String email){
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
}
