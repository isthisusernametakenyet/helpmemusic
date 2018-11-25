package tig167.com.helpmusic;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

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

                User u = new User(name, email);

                userList.add(u);
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return userList;
    }
}
