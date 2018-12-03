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
        EditText name = findViewById(R.id.signUpName);
        EditText password = findViewById(R.id.signUpPassword);
        EditText email = findViewById(R.id.signUpEmail);
        String securePassword = ph.getSHA256SecurePassword(password.getText().toString());
        String identifier = "";
        try {
            jsonObject.put("name", name.getText());
            jsonObject.put("email", email.getText());
            jsonObject.put("password", securePassword);
            new URLSender().execute(
                    "http://10.0.2.2:8080/users",
                    Action.ADD_USER.value(),
                    jsonObject.toString()
            );
            identifier = email.getText().toString();
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
        // identifier --> sessionObject
    }

}