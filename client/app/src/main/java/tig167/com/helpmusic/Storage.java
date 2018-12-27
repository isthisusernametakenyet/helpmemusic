package tig167.com.helpmusic;

public class Storage {
    private static final Storage ourInstance = new Storage();

    public static Storage getInstance() {
        return ourInstance;
    }

    private Storage() {
    }
}
