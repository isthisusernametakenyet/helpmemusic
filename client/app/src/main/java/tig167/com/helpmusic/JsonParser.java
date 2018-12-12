package tig167.com.helpmusic;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    private static final String LOG_TAG = JsonParser.class.getSimpleName();

    public String userToJson(String name, String password, String email){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("{");
        sb.append("\"name\":" + name + ",");
        sb.append("\"email\":" + email);
        sb.append("\"password\":" + password + ",");
        sb.append("}");
        sb.append("}");

        return sb.toString();
    }

    public List<User> jsonToUsers(JSONArray array){
        List<User> userList = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            try{
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String email = obj.getString("email");
                Bitmap profileImage = new Image().decode(obj.getString("image"));


                User u = new User(name, email);
                u.setProfileImage(profileImage);
                userList.add(u);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

    public List<User> jsonToUsersImage(JSONArray array){
        List<User> users = new ArrayList<>();

        for ( int i = 0; i < array.length(); i++){
            try{
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String email = obj.getString("email");
                String imageString = obj.getString("profileimage");

                User  u = new User(name, email);
                u.setProfileImage(new Image().decode(imageString));

                users.add(u);
            }
            catch(JSONException e){
                e.getMessage();
            }
        }
        return users;
    }


    public String parseImage(JSONArray array){
        String imageData = null;
        Log.d(LOG_TAG, ": Array length" + array.length());
        for(int i = 0; i < array.length(); i++){
            try{
                JSONObject obj = array.getJSONObject(i);
                imageData = obj.getString("imageData");
                Log.d(LOG_TAG, ": ImageData = " + imageData);


            }
            catch(JSONException e){
                Log.e(LOG_TAG, ": " + e.getMessage());
            }
        }
        Log.d(LOG_TAG, ": List String = " + imageData);
        return imageData;
    }
}
