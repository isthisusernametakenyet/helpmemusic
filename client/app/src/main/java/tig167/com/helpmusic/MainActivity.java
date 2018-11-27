package tig167.com.helpmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO
    // this class/activity needs to know who logged in
    // in order to get friends of that user
    // and display them in a list-view on a tab

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String URL = "http://10.0.2.2:8080/users";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ArrayAdapter<User> adapter;
    private ListView listView;
    private List<User> users;
    private MainActivity me;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users = new ArrayList<>();  // remove this later!
                                    // we need to read friends of a specific user
                                    // NOT ALL USERS
                                    // correct me if I'm wrong

        listView = findViewById(R.id.activity_volley);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        mImageView = findViewById(R.id.imageView);
        listView.setAdapter(adapter);
        me = this;
        PictureHash p = new PictureHash("Marcus", "marcus@gmail.com");
    }

    private void resetListView(){
        listView = findViewById(R.id.activity_volley);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //getUsers(); // we don't want this
                      // because it will blurt all users out
                      // on top of the beautiful ui
    }

    private void getUsers() { // this method is never executed
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(LOG_TAG, " " + queue.toString());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(LOG_TAG, " :" + array.toString());
                        users = new JsonParser().jsonToUsers(array);
                        resetListView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    public void cameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOG_TAG, " Click camera");
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Log.d(LOG_TAG, " data.getExtra() " + data.getExtras().toString());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String imageString = bitmapToBase64(imageBitmap);
            Log.d(LOG_TAG, " Base64 encode " + imageString);
            mImageView.setImageBitmap(base64ToBitmap(imageString));
            Log.d(LOG_TAG, " Decode base64 to bitmap");

            PictureHash ph = new PictureHash("Marcus", "marcus@gmail.com");
            JSONObject obj = null;
            SessionObject sessionObject = SessionObject.getInstance();
            try {
                obj = new JSONObject();
                obj.put("imageFileName", ph.hash());
                obj.put("imageBitMapEncoded", imageString);
                obj.put("email", sessionObject.user().email());
                obj.put("password", sessionObject.user().password());
            }
            catch (JSONException e){
                Log.e(LOG_TAG, " JsonException: " + e.getMessage());
            }
            new URLSender().execute(URL, Action.ADD_PROFILE_IMG.value(), obj.toString());
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException{
        PictureHash ph = new PictureHash("Marcus", "marcus@gmail.com");
        String fileName = "JPEG_" + ph.hash();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private List<UserChangeListener> listeners;

    public interface UserChangeListener {
        void onUserChangeList(List<User> users);
    }

    public void addUserChangeListener(UserChangeListener l) {
        listeners.add(l);
    }
}