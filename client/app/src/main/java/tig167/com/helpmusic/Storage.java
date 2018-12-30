package tig167.com.helpmusic;

public interface Storage<T> {

    void saveSession(T t);

    void saveFriend(T t);

    T loadSession();

}
