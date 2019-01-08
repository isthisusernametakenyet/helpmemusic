package com.tig167.helpmusic.data.local;

public interface Storage<T> {

    void saveSession(T t);

    void save(T t);

    T loadSession();

}
