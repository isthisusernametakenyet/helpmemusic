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

    public JSONArray imageDataToJson(
            String requestCode,
            String pictureHash,
            String image,
            String email) {
        JSONArray array = new JSONArray();
        try {
            JSONObject requestCodeObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            //array.put(requestCodeObj);
            //JSONObject dataObj = new JSONObject();
            requestCodeObj.put("imageFileName", pictureHash);
            requestCodeObj.put("image", image);
            requestCodeObj.put("email", email);
            array.put(requestCodeObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return array;
    }

    public JSONArray loginDataToJson(String requestCode, String email, String str) {
        JSONArray array = new JSONArray();
        try {
            JSONObject requestCodeObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            array.put(requestCodeObj);
            JSONObject dataObj = new JSONObject();
            dataObj.put("email", email);
            dataObj.put("password", str);
            array.put(dataObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return array;
    }

    public JSONArray signupDataToJson(String requestCode, String name, String email, String str) {
        JSONArray array = new JSONArray();
        try {
            JSONObject requestCodeObj = new JSONObject();
            JSONObject dataObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            array.put(requestCodeObj);
            dataObj.put("name", name);
            dataObj.put("email", email);
            dataObj.put("password", str);
            array.put(dataObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return array;
    }

    public JSONArray addFriend(String requestCode, String friendEmail,String thisName, String thisEmail){
        JSONArray arr = new JSONArray();

        try{
            JSONObject requestCodeObj = new JSONObject();
            JSONObject friendEmailObj = new JSONObject();
            JSONObject thisEmailObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            arr.put(requestCodeObj);
            friendEmailObj.put("name", "");
            friendEmailObj.put("email", friendEmail);
            friendEmailObj.put("password", "");
            arr.put(friendEmailObj);
            thisEmailObj.put("name", thisName);
            thisEmailObj.put("email", thisEmail);
            thisEmailObj.put("password", "");
            arr.put(thisEmailObj);
        }catch(JSONException e){
            e.getMessage();
        }
        return arr;
    }

    public String jsonToLoginResponse(JSONArray array) {
        String response = "";
        try {
            JSONObject obj = array.getJSONObject(0);
            response = obj.getString("response");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return response;
    }

    public String jsonToSignupResponse(JSONArray array) {
        String response = "";
        try {
            JSONObject obj = array.getJSONObject(0);
            response = obj.getString("response");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return response;
    }

    public List<User> jsonToUsers(JSONArray array){
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try{
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String email = obj.getString("email");
                Log.d(LOG_TAG, ": init bitmap " + obj.get("imageData"));
                Bitmap profileImage = new Image().decode(obj.getString("imageData"));
                //Log.d(LOG_TAG, ": decode image " + profileImage.toString());

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

    public String jsonToUserName(JSONArray array) {
        String name = "";
        try {
            JSONObject obj = array.getJSONObject(0);
            name = obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
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
        Log.d(LOG_TAG, ": Array length " + array.length());
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
