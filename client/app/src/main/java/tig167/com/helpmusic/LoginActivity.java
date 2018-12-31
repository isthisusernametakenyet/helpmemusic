package tig167.com.helpmusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private static SessionObject session;
    private String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
        PasswordHashing ph = new PasswordHashing();
        EditText emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        identifier = emailField.getText().toString();
        session = SessionObject.getInstance();
        getAccess(new JsonParser().loginDataToJson(Action.LOGIN.value(), identifier, securePassword));
    }

    private void getAccess(JSONArray json) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                MainActivity.URL,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        String str = new JsonParser().jsonToLoginResponse(array);
                        if ("ok".equalsIgnoreCase(str)) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Welcome",
                                    Toast.LENGTH_SHORT
                            ).show();
                            getUserData();
                        }else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "You entered the wrong password or email address",
                                    Toast.LENGTH_SHORT
                            ).show();
                            EditText email = findViewById(R.id.loginEmail);
                            EditText password = findViewById(R.id.loginPassword);
                            email.setTextColor(Color.RED);
                            password.setTextColor(Color.RED);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(LOG_TAG, "getAccess " + error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void getUserData() {
        final String SERVER_REQUEST_USER_NAME = "?getUserName=";
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL + SERVER_REQUEST_USER_NAME + identifier,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        session.setUser(new User(
                                new JsonParser().jsonToUserName(array),
                                identifier,
                                new Image().decode(new JsonParser().parseImage(array))
                        ));
                        getFriends();
                        Log.d(LOG_TAG, array.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(LOG_TAG, "get user data " + error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void getFriends() {
        final String SERVER_REQUEST_FRIENDS = "?getFriends=";
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL + SERVER_REQUEST_FRIENDS + identifier,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray array) {
                        for (User friend : new JsonParser().jsonToUsers(array)) {
                            session.user().addFriend(friend);
                            Log.d(LOG_TAG, friend.name() + " added from server db");
                        }
                        DbHelper.getInstance(getApplicationContext()).saveSession(session.user());
                        Log.d(LOG_TAG, " friends: \n" + session.user().friends());
                        nextActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, " getFriends error " + error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(LOG_TAG, " intent: start main activity");
        startActivity(intent);
    }

}
