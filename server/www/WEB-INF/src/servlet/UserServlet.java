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

    private DbSelection dbSelection;

    @Override
    public void doGet( // method never executed, else @marcus remove this comment
            HttpServletRequest request,
            HttpServletResponse response)
                        throws ServletException, IOException {
        response.setContentType("application/json");
        List<User> users = new DbSelection().readUsers();
        JSONArray arr = new JSONParser().usersToJson(users);
        response.getWriter().println(arr.toString(2));
        System.out.println("never printed"); // else remove line
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
            if (new DbInsert().insertUser(user)) {
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
        case ADD_FRIEND: // TODO @erik make it work
            List<User> frnds = parser.jsonToUsers(jsonString);
            new DbInsert().insertFriendship(frnds.get(0).email(), frnds.get(1).email());
            frnds.get(0).addFriend(frnds.get(1));
            frnds.get(1).addFriend(frnds.get(0));
            // what a mess!
            break;
        case GET_FRIENDS: // TODO @erik make it work
            User u = parser.jsonToUser(jsonString);
            List<User> friends = new DbSelection().readFriends(u.email());
            JSONArray arr = parser.usersToJson(friends);
            response.getWriter().println(arr.toString());
            break;
        case ADD_PROFILE_IMG:
            String[] imageData = parser.parseImageData(jsonString);
            new DbInsert().insertImage(imageData[0], imageData[1]);
            break;
        default: 
            System.err.println("illegal action");
            response.setStatus(400);
            break;
        }           
    }

}		
