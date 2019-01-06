package servlet.integration;

import org.json.*;
import java.util.*;

import servlet.db.DbSelection;
import servlet.model.User;

public class JSONParser{
    
    private static final int POST_REQUEST_INDEX = 0;
    private static final int POST_DATA_INDEX = 1;

    public String jsonToRequestCode(String json) {
        String requestCode = "";
        try {
            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(POST_REQUEST_INDEX);
            requestCode = obj.getString("requestCode");
        } catch (JSONException je) {
            System.err.println("server_error: unable to parse request");
            je.printStackTrace();
        }
        return requestCode;
    }
    
    public JSONArray stringToJson(String answer) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("response", answer);
        array.put(obj);
        return array;
    }
    
    public JSONArray usersToJson(List<User> users) {
        JSONArray array = new JSONArray();
        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("name", user.name());
            obj.put("email", user.email());
            if(user.image() == null){
                obj.put("imageData", "");
            }else{
                obj.put("imageData", user.image());
            }
            array.put(obj);
        }
        return array;
    }
    
    public JSONArray userToJson(User user) {
        JSONArray array = new JSONArray();
        JSONObject obj = new JSONObject();
        obj.put("name", user.name());
        obj.put("email", user.email());
        if(user.image() == null){
            obj.put("imageData", "");
        }else{
            obj.put("imageData", user.image());
        }
        array.put(obj);
        return array;
    }
    
    public List<User> jsonToUsers(String json) {
        JSONArray jsonArray = null;
        List<User> users = new ArrayList<User>();
        for (int i = 1; i < jsonArray.length(); i++) {
            try {
                jsonArray = new JSONArray(json);
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                users.add(new User(
                jsonObj.getString("name"),
                jsonObj.getString("email"),
                jsonObj.getString("password")
                ));
            } catch (JSONException je) {
                System.err.println("unable to parse: " + je.getMessage());
            }
        }
        return users;
    }

    public String[] jsonToIdentifiers(String json) {
        String email = "";
        String fEmail = "";
        try {
            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(POST_DATA_INDEX);
            email = obj.getString("email");
            fEmail = obj.getString("fEmail");
        } catch (JSONException je) {
            System.err.println("unable to parse: " + je.getMessage());
        }
        String[] identifiers = new String[2];
        identifiers[0] = email;
        identifiers[1] = fEmail;
        return identifiers;
    }
    
    public User jsonToUser(String json) {
        User user = null;
        try {
            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(POST_DATA_INDEX);
            user = new User(
            obj.getString("name"),
            obj.getString("email"),
            obj.getString("password")
            );
        } catch (JSONException je) {
            System.err.println("unable to parse: " + je.getMessage());
        }
        return user;
    }
    
    public String[] jsonToLoginData(String json){
        String email = "";
        String password = "";
        try {
            JSONArray array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(POST_DATA_INDEX);
            email = obj.getString("email");
            password = obj.getString("password");
        }
        catch (JSONException e) {
            System.err.println(e.getMessage());   
        }
        String[] loginValue = new String[2];
        loginValue[0] = email;
        loginValue[1] = password;
        return loginValue;
    }
    
    public String[] parseImageData(String json) {
        String imageName = "";   
        String imageData = "";
        String email = "";
        JSONArray array = null;
        try {                  
            array = new JSONArray(json);
            JSONObject obj = array.getJSONObject(POST_DATA_INDEX);
            imageName = obj.getString("imageFileName");
            imageData = obj.getString("image");
            email = obj.getString("email");
        } catch(JSONException e){
            System.out.println("faild to parse image data: " + e.getMessage());
        }
        String[] imageValue = new String[3];
        imageValue[0] = imageName;
        imageValue[1] = imageData;
        imageValue[2] = email;
        return imageValue;
    }
    
    public JSONArray imageToJson(String email){
        JSONArray arr = new JSONArray();
        try{            
            JSONObject obj = new JSONObject();
            DbSelection selection = new DbSelection();
            obj.put("imageData", selection.readProfileImage(email));
            arr.put(obj);
        }
        catch(JSONException e){
            System.err.println("Unable to parse the image data to json " + e.getMessage());
        }
        return arr;
    }
    
}
