package tig167.com.helpmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signUpButton(View view) {
        PasswordHashing ph = new PasswordHashing();
        JSONObject jsonObject = new JSONObject();
        EditText nameField = findViewById(R.id.signUpName);
        EditText emailField = findViewById(R.id.signUpEmail);
        EditText passwordField = findViewById(R.id.signUpPassword);
        String securePassword = ph.getSHA256SecurePassword(passwordField.getText().toString());
        try {
            jsonObject.put("name", nameField.getText());
            jsonObject.put("email", emailField.getText());
            jsonObject.put("password", securePassword);
            new URLSender().execute(
                    "http://10.0.2.2:8080/users",
                    Action.ADD_USER.value(),
                    jsonObject.toString()
            );

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        SessionObject.getInstance().setUser(name, email);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}