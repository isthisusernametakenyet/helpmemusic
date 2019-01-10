package com.tig167.helpmusic.data.local;

/**
 * Implemented by database helper class for local data handling, ie. save/load.
 *
 * @param <T> the object type handled by local data storage
 */
public interface Storage<T> {

    void saveSession(T t);

    void save(T t);

    T loadSession();

}
