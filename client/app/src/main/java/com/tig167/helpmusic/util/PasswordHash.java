package com.tig167.helpmusic.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates hashed passwords.
 */
public class PasswordHash {

    public String getSHA256SecurePassword(String passwordToHash){
        String generatedPassword = null;

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
