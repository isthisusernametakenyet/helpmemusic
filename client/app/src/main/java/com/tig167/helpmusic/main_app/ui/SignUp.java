package com.tig167.helpmusic.main_app.ui;

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
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.VolleyService;
import com.tig167.helpmusic.util.PasswordHash;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.data.local.db.DbHelper;
import com.tig167.helpmusic.data.remote.ServerAction;

import org.json.JSONArray;

/**
 * Handles user sign up, and takes you to the main activity on success.
 */
public class SignUp extends AppCompatActivity {

    private static final String LOG_TAG = SignUp.class.getSimpleName();
    private VolleyService volleyService;
    private static SessionObject session;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        session = SessionObject.getInstance();
        initCallback();
    }

    /**
     * Send new user data to server.
     *
     * @param securePassword    the encrypted password
     */
    private void sendServerPostRequest(String securePassword) {
        volleyService.postDataVolley(
                "POST",
                MainActivity.URL,
                new JsonParser().signupDataToJson(
                        ServerAction.ADD_USER.value(),
                        name,
                        email,
                        securePassword
                )
        );
    }

    /**
     * Validate email, encrypt password and send a sign-up request
     * with the new user data as json to server
     *
     * @param view the button
     */
    public void signUpButton(View view) {
        PasswordHash ph = new PasswordHash();
        EditText nameField = findViewById(R.id.signUpName);
        EditText emailField = findViewById(R.id.signUpEmail);
        EditText passwordField = findViewById(R.id.signUpPassword);
        name = nameField.getText().toString();
        email = emailField.getText().toString();
        if(!email.contains("@")){
            emailField.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        sendServerPostRequest(securePassword);
    }

    /**
     * Instantiate volley result callback for the sign up request.
     * On success: set user to this session, save new session in local database
     * and continue to main activity
     */
    private void initCallback() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                String str = new JsonParser().jsonToString(response);
                if ("ok".equalsIgnoreCase(str)) {
                    session.setUser(name, email);
                    DbHelper.getInstance(getApplicationContext())
                            .saveSession(session.user());
                    nextActivity();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                Log.d(LOG_TAG, "Error addUser " + error.getCause().getMessage());
            }
        },this);
    }

    /**
     * Sign up was successful, continue to main activity.
     */
    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(LOG_TAG, "Intent: start main activity");
        startActivity(intent);
    }

}