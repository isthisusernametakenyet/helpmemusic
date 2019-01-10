package com.tig167.helpmusic.util;

import android.util.Log;

import java.time.LocalDateTime;

/**
 * Provides functionality to set hash to pictures, with name, email and date.
 */
public class PictureHash {
    private String hashStart;
    private static final String LOG_TAG = "PictureHash: ";

    public PictureHash(String name, String email) {
        hashStart = name + email;
        setHashStart(hashStart);
    }

    private void setHashStart(String hashStart) {
        LocalDateTime now = LocalDateTime.now();

        hashStart += DateUtil.format(now);

        Log.d(LOG_TAG, hashStart);
    }

    public String hash(){
        return hashStart;
    }
}
