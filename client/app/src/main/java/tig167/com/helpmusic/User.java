package tig167.com.helpmusic;

public class User {
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String name(){
        return name;
    }

    public String email(){
        return email;
    }

    public String password(){ return password; }

    public String toString() { return name + " " + email; }
}
