package tig167.com.helpmusic;

public interface Storage {

    void saveSession(User user);

    void saveFriend(User friend);

    User loadSession();

}
