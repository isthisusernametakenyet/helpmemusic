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
import com.tig167.helpmusic.util.PasswordHash;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.data.local.db.DbHelper;
import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.data.remote.ServerAction;

import org.json.JSONArray;

import java.util.List;

/**
 * Handle user login or go to sign up.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private static SessionObject session;
    private VolleyService volleyService;

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
        PasswordHash ph = new PasswordHash();
        EditText emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        String identifier = emailField.getText().toString();
        session = SessionObject.getInstance();
        prepareLoginResponse();
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

    private void prepareLoginResponse() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray array) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                List<User> users = new JsonParser().jsonToUsers(array);
                if (users.size() == 0) {
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
                    session.setUser(users.get(0)); // first element in list is THIS_USER
                    users.remove(0); // only friends left in list
                    for (User user : users) {
                        session.user().addFriend(user);
                    }
                    DbHelper.getInstance(getApplicationContext()).saveSession(session.user());
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

    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        Log.d(LOG_TAG, " intent: start main activity");
        startActivity(intent);
    }

}
