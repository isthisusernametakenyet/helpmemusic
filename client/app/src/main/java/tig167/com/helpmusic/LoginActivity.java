package tig167.com.helpmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://10.0.2.2:8080/users";
    private static SessionObject session;
    private LoginActivity me;
    private String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        me = this;
        session = SessionObject.getInstance();
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void loginClick(View view){
        PasswordHashing ph = new PasswordHashing();
        EditText emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", emailField.getText());
            jsonObject.put("password", securePassword);
            new URLSender().execute(
                    SERVER_URL,
                    Action.LOGIN.value(),
                    jsonObject.toString()
            );
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        identifier = emailField.getText().toString();
        getUserName();
    }

    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getUserName() {
        final String SERVER_REQUEST_USER_NAME = "?getUserName=";
        RequestQueue queue = Volley.newRequestQueue(me);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL + SERVER_REQUEST_USER_NAME + identifier,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        session.setUser(new JsonParser().jsonToUserName(array), identifier);
                        nextActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error: getUserName ", error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }
}
