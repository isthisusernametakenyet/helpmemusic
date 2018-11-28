package tig167.com.helpmusic;

public class SessionObject {

    private static SessionObject session = null;
    private static User user;
    private SessionObject(){

    }

    public static SessionObject getInstance(){
        if(session != null){
            return session;
        }
        else{
            session = new SessionObject();
            setUser();
            return session;
        }
    }

    //TODO: for testing. adding a hardcoded object of user to the session.
    private static void setUser (){
        PasswordHashing ph = new PasswordHashing();
        user = new User("marcus", "marcus@gmail.com", ph.getSHA256SecurePassword("hb3cgy5a"));
    }

    public User user(){
        return user;
    }

    public void setUser(User user){
        user = new User(user.name(), user.email(), user.password());
    }
}
