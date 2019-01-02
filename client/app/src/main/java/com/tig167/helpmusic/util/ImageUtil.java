package com.tig167.helpmusic.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    public static String encode(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap decode(String b64) {

        Bitmap bitmap = null;
        if(b64 == null){
            return null;
        }
        try {
            byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.NO_WRAP);
            bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        }
        catch(IllegalArgumentException e){
            Log.e("IllegalArgumentException: ", e.getMessage());
        }

        return bitmap;
    }
}
