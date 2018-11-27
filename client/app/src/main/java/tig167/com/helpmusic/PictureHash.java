package tig167.com.helpmusic;

import android.util.Log;

import java.time.LocalDateTime;

public class PictureHash {
    private String hashStart;
    private static final String LOG_TAG = "PictureHash: ";

    public PictureHash(String name, String mail){
        hashStart = name + mail;
        setHashStart(hashStart);
    }

    private void setHashStart(String hashStart){
        //Time time = new Time();
        LocalDateTime now = LocalDateTime.now();

        hashStart += now.toString();


        Log.d(LOG_TAG, hashStart);
    }

    public String hash(){
        return hashStart;
    }
}
