package com.tig167.helpmusic.data.remote;

import android.graphics.Bitmap;
import android.util.Log;

import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.main_app.model.UserFactory;
import com.tig167.helpmusic.util.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Put data in json arrays, or get data from json arrays.
 */
public class JsonParser {

    private static final String LOG_TAG = JsonParser.class.getSimpleName();

    /**
     * Put data in json objects when handling images.
     * The object containing the request code is object 0 in the json array returned.
     *
     * @param requestCode   the server request code
     * @param pictureHash   the image meta data
     * @param image         the primary image data
     * @param email         the app user identifier
     * @return              the json array of image data
     */
    public JSONArray imageDataToJson(
            String requestCode,
            String pictureHash,
            String image,
            String email) {
        JSONArray array = new JSONArray();
        try {
            JSONObject requestCodeObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            array.put(requestCodeObj);
            JSONObject dataObj = new JSONObject();
            dataObj.put("imageFileName", pictureHash);
            dataObj.put("image", image);
            dataObj.put("email", email);
            array.put(dataObj);
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return array;
    }

    /**
     * Put data in json objects at login.
     * The object containing the request code is object 0 in the json array returned.
     *
     * @param requestCode   the server request code
     * @param email         the app user identifier
     * @param str           the very secret password
     * @return              the json array with login data
     */
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

    /**
     * Put data in json objects at sign up.
     * The object containing the request code is object 0 in the json array returned.
     *
     * @param requestCode   the server request code
     * @param name          the user name
     * @param email         the user email
     * @param str           nothing appropriate
     */
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

    /**
     * Put data in json objects when friend is to be added.
     * The object containing the request code is object 0 in the json array returned.
     *
     * @param requestCode   the server request code
     * @param email         the user email
     * @param fEmail        the other user email
     * @return              a json array containing two email addresses
     */
    public JSONArray friendshipToJson(
            String requestCode,
            String email,
            String fEmail) {
        JSONArray arr = new JSONArray();
        try{
            JSONObject requestCodeObj = new JSONObject();
            JSONObject dataObj = new JSONObject();
            requestCodeObj.put("requestCode", requestCode);
            arr.put(requestCodeObj);
            dataObj.put("email", email);
            dataObj.put("fEmail", fEmail);
            arr.put(dataObj);
        } catch (JSONException e){
            e.getMessage();
        }
        return arr;
    }

    /**
     * Parse json to string.
     *
     * @param array     the json array to be parsed
     * @return          a text response
     */
    public String jsonToString(JSONArray array) {
        String response = "";
        try {
            JSONObject obj = array.getJSONObject(0);
            response = obj.getString("response");
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return response;
    }

    /**
     * Parse json array to list of users.
     *
     * @param array     the json array to be processed
     * @return          a list of user data
     */
    public List<User> jsonToUsers(JSONArray array){
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try{
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("name");
                String email = obj.getString("email");
                Log.d(LOG_TAG, ": init bitmap " + obj.get("imageData"));
                Bitmap profileImage = ImageUtil.decode(obj.getString("imageData"));
                //Log.d(LOG_TAG, ": decode image " + profileImage.toString());

                User u = UserFactory.create(name, email, profileImage);
                userList.add(u);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }

}
