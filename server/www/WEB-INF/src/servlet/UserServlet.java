package servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.io.*;
import java.io.OutputStreamWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.*;
import java.io.IOException;
import java.util.*;
import org.json.*;

import servlet.model.User;
import servlet.db.*;
import servlet.integration.JSONParser;

public class UserServlet extends HttpServlet {
    
    private enum RequestCode {
        LOGIN("login"),
        ADD_USER("addUser"),
        GET_USER_NAME("getUserName"),
        ADD_FRIEND("addFriend"),
        GET_FRIENDS("getFriends"),
        GET_PROFILE_IMG("getProfileImg"),
        ADD_PROFILE_IMG("addProfileImg");
        
        private String value;
        private static final Map<String, RequestCode> ENUM_MAP;
        
        RequestCode (String str) {
            value = str;
        }
        
        static {
            Map<String, RequestCode> tmp = new HashMap<String, RequestCode>();
            for (RequestCode instance : RequestCode.values()) {
                tmp.put(instance.value(), instance);
            }
            ENUM_MAP = Collections.unmodifiableMap(tmp);
        }
        
        String value() {
            return value;
        }
        
        static RequestCode get(String value) {
            return ENUM_MAP.get(value);
        }
    }
    
    @Override
    public void doGet(
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        String query = request.getQueryString();
        String[] data = query.split("=");
        final int KEY = 0;
        final int VAL = 1;
        if(data[KEY].equals("getProfileImg")) {
            String userEmail = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            JSONArray arr = new JSONParser().imageToJson(userEmail);
            System.out.println("Return image: " + arr.toString());
            response.getWriter().println(arr.toString());
        }
        if (data[KEY].equals("getFriends")) {
            String email = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            User user = new DbSelection().getUser(email);
            List<User> friends = new DbSelection().readFriends(user);
            JSONArray arr = new JSONParser().usersToJson(friends);
            response.getWriter().println(arr.toString());
        }
        if (data[KEY].equals("getUserName")) {
            String usrEmail = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            User usr = new DbSelection().getUser(usrEmail);
            System.out.println("new login: " + usr.name());
            JSONArray arr = new JSONParser().userToJson(usr);
            response.getWriter().println(arr.toString());
        }
        if (data[KEY].equals("getSearchResult")){
            String name = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            List<User> users = new DbSelection().readUsers(name);
            JSONArray arr = new JSONParser().usersToJson(users);
            System.out.println(arr.toString());
            response.getWriter().println(arr.toString());
        }
    }
    
    @Override        
    public void doPost(
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = new PrintWriter(
        new OutputStreamWriter(response.getOutputStream(), UTF_8), true);
        response.setContentType("application/json");
        BufferedReader reader = request.getReader();
        JSONParser parser = new JSONParser();
        
        String string = reader.readLine();
        int pos = string.indexOf('{');
        String jsonString = string.substring(pos);
        string = string.substring(0, pos);
        
        RequestCode requestCode = RequestCode.get(string);
        
        switch (requestCode) {
            case ADD_USER:
            User user = parser.jsonToUser(jsonString);
            if (new DbInsert().insertUser(user)
            && new DbCreation().createFriendTable(user.email())) {
                response.setStatus(200);
                response.getWriter().println("ok");
            } else {
                response.setStatus(210);
                response.getWriter().println("failed");
            }
            break;
            case LOGIN:
            String[] val = parser.parseLogin(jsonString);
            if (new DbSelection().hasUser(val[0], val[1])) {
                response.getWriter().println("ok");
            } else {
                response.getWriter().println("access denied");
            }
            response.setStatus(200);
            break;
            case ADD_FRIEND:
            List<User> friendship = parser.jsonToUsers(jsonString);
            new DbInsert().insertFriendship(friendship);
            break;
            case ADD_PROFILE_IMG:
            String[] imageData = parser.parseImageData(jsonString);
            new DbInsert().insertImage(imageData[0], imageData[1], imageData[2], imageData[3]);
            break;
            default: 
            System.err.println("illegal action");
            response.setStatus(400);
            break;
        }           
    }
}
