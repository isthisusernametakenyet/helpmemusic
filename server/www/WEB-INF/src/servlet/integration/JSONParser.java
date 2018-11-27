package servlet.integration;;
         
import org.json.*;
import java.util.*;
         
import servlet.model.User;
         
public class JSONParser {

    public JSONArray usersToJson(List<User> users) {
        JSONArray array = new JSONArray();
        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("name", user.name());
            obj.put("email", user.email());
            array.put(obj);
        }
        return array;
    }
             
    public List<User> jsonToUsers(String json) {                            
        JSONArray jsonArray = null;
        try {                  
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONObject jsonObject = new JSONObject(jsonTokener);                 
            jsonArray = jsonObject.getJSONArray("users");                
        } catch (JSONException je) {                                       
            System.err.println("unable to init json array: "
                    + je.getMessage());
            System.exit(1);
        }
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                users.add(new User(
                            jsonObj.getString("name"),
                            jsonObj.getString("email"),
                            jsonObj.getString("password")
                ));            
            } catch (JSONException je) {
                System.err.println("unable to parse: " + je.getMessage());
                System.exit(1);
            }
        }
        return users;
    }

    public User jsonToUser(String json) {    // name not found at login
        User user = null;
        json = json.replace("PostData=", "");
        try {
            JSONTokener jsonTokener = new JSONTokener(json);
            JSONObject jo = new JSONObject(jsonTokener);
            user = new User(
                    jo.getString("name"),
                    jo.getString("email"),
                    jo.getString("password")
            );
        } catch (JSONException je) {
            System.err.println("unable to parse: " + je.getMessage());
            System.exit(1);
        }
        return user;
    }

    public String[] parseLogin(String json){
        String email = "";
        String password = "";
        try {
            JSONObject jsonObj = new JSONObject(json);
            email = jsonObj.getString("email");
            password = jsonObj.getString("password");
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
        try{
            JSONObject jsonObj = new JSONObject(json);
            imageName = jsonObj.getString("imageFileName");
            imageData = jsonObj.getString("imageBitMapEncoded");
        } catch(JSONException e){
            System.out.println("faild to parse image data: " + e.getMessage());
        }
        String[] imageValue = new String[2];
        imageValue[0] = imageName;
        imageValue[1] = imageData;
        return imageValue;
    }
}
