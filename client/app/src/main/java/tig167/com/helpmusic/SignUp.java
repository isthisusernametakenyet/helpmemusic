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

public class SignUp extends AppCompatActivity {

    private static final String LOG_TAG = SignUp.class.getSimpleName();
    private static SessionObject session;
    private String name;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        session = SessionObject.getInstance();
    }

    public void signUpButton(View view) {
        PasswordHashing ph = new PasswordHashing();
        EditText nameField = findViewById(R.id.signUpName);
        EditText emailField = findViewById(R.id.signUpEmail);
        EditText passwordField = findViewById(R.id.signUpPassword);
        name = nameField.getText().toString();
        email = emailField.getText().toString();
        if(!email.contains("@")){
            emailField.setTextColor(Color.RED);
            Toast.makeText(getApplicationContext(), "Not a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        addUser(new JsonParser().signupDataToJson(Action.ADD_USER.value(), name, email, securePassword));
    }

    private void addUser(JSONArray json) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                MainActivity.URL,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        String str = new JsonParser().jsonToSignupResponse(array);
                        if ("ok".equalsIgnoreCase(str)) {
                            session.setUser(name, email);
                            DbHelper.getInstance(getApplicationContext())
                                    .saveSession(session.user());
                            nextActivity();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, " error addUser " + error.getCause().getMessage());
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