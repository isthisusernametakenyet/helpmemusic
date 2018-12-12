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
        ADD_USER("add user"),
        ADD_FRIEND("add friend"),
        GET_FRIENDS("get friends"),
        ADD_PROFILE_IMG("add profile-img");
        
        private String value;
        private static final Map<String, RequestCode> ENUM_MAP;
        
        RequestCode (String str) {
            value = str;
        }
        
        static {
            // init a map in order to get eg. RequestCode.LOGIN from a string "login"
            // we can then switch values later on to perform the right task :-)
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
    public void doGet( // added getfriends
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        String query = request.getQueryString();
        System.out.println("query: " + query);
        /*
        if(query == null){
            response.setContentType("application/json;charset=UTF-8");
            List<User> users = new DbSelection().readUsers();
            JSONArray arr = new JSONParser().usersToJson(users);
            response.getWriter().println(arr.toString(2));
            System.out.println("never printed"); // else remove line
        }
        */
        String[] data = query.split("=");
        final int KEY = 0;
        final int VAL = 1;
        System.out.println("key:   " + data[KEY]);
        System.out.println("value: " + data[VAL]);
        if(data[KEY].equals("profileImage")) {
            String userEmail = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            JSONArray arr = new JSONParser().imageToJson(userEmail);
            response.getWriter().println(arr.toString());
        }
        if (data[KEY].equals("getFriends")) {
            String email = data[VAL];
            response.setContentType("application/json;charset=UTF-8");
            User user = new DbSelection().getUser(email);
            List<User> friends = new DbSelection().readFriends(user);
            System.out.println("user has " + friends.size() + " friends");
            JSONArray arr = new JSONParser().usersToJson(friends);
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
        
        /*
        *      here be dragons...
        */
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
