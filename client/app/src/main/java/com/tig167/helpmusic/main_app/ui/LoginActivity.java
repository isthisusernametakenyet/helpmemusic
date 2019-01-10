package com.tig167.helpmusic.main_app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.VolleyService;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.util.DateUtil;
import com.tig167.helpmusic.util.PasswordHash;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.data.local.db.DbHelper;
import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.data.remote.ServerAction;

import org.json.JSONArray;

import java.util.List;

/**
 * Handles user login or takes you to sign up.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private static SessionObject session;
    private VolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = SessionObject.getInstance();
        prepareLoginResponse();
    }

    /**
     * Button clicked indicates user does not exist, so start sign up activity instead.
     *
     * @param view      the button
     */
    public void signUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    /**
     * Send login data as json to server.
     *
     * @param identifier        the unique email address
     * @param securePassword    the encrypted password
     */
    private void sendServerPostRequest(String identifier, String securePassword) {
        volleyService.postDataVolley(
                "POST",
                MainActivity.URL,
                new JsonParser().loginDataToJson(
                        ServerAction.LOGIN.value(),
                        identifier,
                        securePassword
                )
        );
    }

    /**
     * Handle button click login. Get user data and send all.
     *
     * @param view      the button
     */
    public void loginClick(View view) {
        PasswordHash ph = new PasswordHash();
        EditText emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        String identifier = emailField.getText().toString();
        sendServerPostRequest(identifier, securePassword);
    }

    /**
     * Provide session object with new user data, and save in local storage.
     *
     * @param users     the current user with friends, current user is element at index 0
     */
    private void setSessionData(List<User> users) {
        User user = users.get(0); // get a ref to current user
        users.remove(0); // modify list to friend data by removing current user
        for (User f : users) {
            user.addFriend(f);
        }
        session.setUser(user);
        session.setSessionStart();
        String sessionStart = DateUtil.format(session.getSessionStart());
        Log.d(LOG_TAG, sessionStart + " " + session.user().email());
        DbHelper.getInstance(getApplicationContext())
                .saveSession(session.user());
    }

    /**
     * Init the volley service with callback interface
     * in order to get the login response.
     * Pass data to session object on success.
     */
    private void prepareLoginResponse() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray array) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                List<User> tmp = new JsonParser().jsonToUsers(array);
                if (tmp.size() == 0) {
                    Toast.makeText(
                            getApplicationContext(),
                            "You entered the wrong password or email address",
                            Toast.LENGTH_SHORT
                    ).show();
                    EditText email = findViewById(R.id.loginEmail);
                    EditText password = findViewById(R.id.loginPassword);
                    email.setTextColor(Color.RED);
                    password.setTextColor(Color.RED);
                } else {
                    setSessionData(tmp);
                    Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    nextActivity();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError ve) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                Log.d(LOG_TAG, "Error login " + ve.getCause().getMessage());
            }
        }, this);
    }

    /**
     * Login was successful, continue to main activity.
     */
    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(LOG_TAG, " intent: start main activity");
        startActivity(intent);
    }

}
