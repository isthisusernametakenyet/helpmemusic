package com.tig167.helpmusic.util;

import android.util.Log;

import java.time.LocalDateTime;

/**
 * Provides functionality to set hash with name, email and date to pictures.
 */
public class PictureHash {
    private String hashStart;
    private static final String LOG_TAG = "PictureHash: ";

    public PictureHash(String name, String mail) {
        hashStart = name + mail;
        setHashStart(hashStart);
    }

    private void setHashStart(String hashStart) {
        //Time time = new Time();
        LocalDateTime now = LocalDateTime.now();

        hashStart += now.toString();


        Log.d(LOG_TAG, hashStart);
    }

    public String hash(){
        return hashStart;
    }
}
