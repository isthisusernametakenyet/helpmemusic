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

    private static final String JSON_CONTENT = "application/json;charset=UTF-8";
    
    private enum RequestCode {
        LOGIN("login"),
        ADD_USER("addUser"),
        GET_USER_NAME("getUserName"),
        ADD_FRIEND("addFriend"),
        GET_FRIENDS("getFriends"),
        GET_PROFILE_IMG("getProfileImg"),
        ADD_PROFILE_IMG("addProfileImg"),
        GET_SEARCH_RESULT("getSearchResult");
        
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
        String[] queryStrings = query.split("=");
        final int REQUEST_CODE_INDEX = 0;
        final int DATA_INDEX = 1;
        RequestCode requestCode = RequestCode.get(queryStrings[REQUEST_CODE_INDEX]);
        response.setContentType(JSON_CONTENT);
        JSONParser parser = new JSONParser();
        DbSelection selection = new DbSelection();
        JSONArray array = null;
        User user = null;
        List<User> users = null;
        switch (requestCode) {
            case GET_PROFILE_IMG:
            array = parser.imageToJson(queryStrings[DATA_INDEX]);
            response.getWriter().println(array.toString());
            break;
            case GET_FRIENDS:
            user = selection.getUser(queryStrings[DATA_INDEX]);
            users = selection.readFriends(user);
            array = parser.usersToJson(users);
            response.getWriter().println(array.toString());
            break;
            case GET_USER_NAME:
            user = selection.getUser(queryStrings[DATA_INDEX]);
            array = parser.userToJson(user);
            response.getWriter().println(array.toString());
            break;
            case GET_SEARCH_RESULT:
            users = selection.readUsers(queryStrings[DATA_INDEX]);
            array = parser.usersToJson(users);
            response.getWriter().println(array.toString());
            break;
            default:
            System.err.println("illegal action");
            break;
        }
    }
    
    @Override        
    public void doPost(
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = new PrintWriter(
        new OutputStreamWriter(response.getOutputStream(), UTF_8), true);
        response.setContentType(JSON_CONTENT);
        BufferedReader reader = request.getReader();
        String json = reader.readLine();
        JSONParser parser = new JSONParser();
        String rc = parser.jsonToRequestCode(json);
        RequestCode requestCode = RequestCode.get(rc);     
        switch (requestCode) {
            case ADD_USER:
            User user = parser.jsonToUser(json);
            if (new DbInsert().insertUser(user)
            && new DbCreation().createFriendTable(user.email())) {
                response.getWriter().println(parser.stringToJson("ok"));
            } else {
                response.getWriter().println(parser.stringToJson("failed"));
            }
            break;
            case LOGIN:
            String[] val = parser.jsonToLoginData(json);
            if (new DbSelection().hasUser(val[0], val[1])) {
                response.getWriter().println(parser.stringToJson("ok"));
            } else {
                response.getWriter().println(parser.stringToJson("access denied"));
            }
            break;
            case ADD_FRIEND:
            List<User> users = parser.jsonToUsers(json);
            new DbInsert().insertFriendship(users);
            break;
            case ADD_PROFILE_IMG:
            String[] imageData = parser.parseImageData(json);
            new DbInsert().insertImage(imageData[0], imageData[1], imageData[2]);
            break;
            default:
            System.err.println("illegal action");
            response.setStatus(400);
            break;
        }           
    }
}
