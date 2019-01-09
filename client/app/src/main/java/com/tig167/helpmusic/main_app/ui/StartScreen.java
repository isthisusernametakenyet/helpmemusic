package com.tig167.helpmusic.main_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.data.remote.ServerAction;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.data.remote.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartScreen extends AppCompatActivity {

    private static final String LOG_TAG = StartScreen.class.getSimpleName();
    private TextView textView;
    private VolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Create start-screen");
        setContentView(R.layout.activity_startscreen);
        textView = findViewById(R.id.connect_text);
        initVolley();
    }

    /**
     * Say hello to the server in order to check the connection.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume, start-screen");
        final String URL = MainActivity.URL + ServerAction.CONNECT.value() + "hello";
        volleyService.getDataVolley("GET", URL);
    }


    /**
     * If the answer to "hello" is "world" we have a connection and are good to go.
     */
    private void initVolley() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                String str = new JsonParser().jsonToString(response);
                if ("world".equalsIgnoreCase(str)) {
                    String msg = "Connected to server";
                    textView.setText(msg);
                    Toast.makeText(getApplicationContext(),"Login", Toast.LENGTH_SHORT).show();
                    nextActivity();
                } else {
                    Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                Log.d(LOG_TAG, "serverConnection " + error.getCause().getMessage());
            }
        },this);
    }

    private void nextActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        Log.d(LOG_TAG, "Intent: start login activity");
        startActivity(intent);
    }
}
