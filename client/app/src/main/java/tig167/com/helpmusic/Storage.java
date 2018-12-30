package tig167.com.helpmusic;

public interface Storage<T> {

    void saveSession(T t);

    void save(T t);

    T loadSession();

}
