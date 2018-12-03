package tig167.com.helpmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://10.0.2.2:8080/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void loginClick(View view){

        PasswordHashing ph = new PasswordHashing();
        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        String securePassword = ph.getSHA256SecurePassword(password.getText().toString());
        String identifier = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email.getText());
            jsonObject.put("password", securePassword);
            new URLSender().execute(
                    SERVER_URL,
                    Action.LOGIN.value(),
                    jsonObject.toString()
            );
            identifier = email.getText().toString();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        // identifier-->sessionObject
    }
}
