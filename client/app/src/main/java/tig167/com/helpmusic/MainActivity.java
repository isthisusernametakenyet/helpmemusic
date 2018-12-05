package tig167.com.helpmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCas;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://10.0.2.2:8080/users";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private enum FragmentTab {
        NEWS("News"),
        FRIENDS("Friends"),
        PROFILE("Profile"),
        MUSIC("Music");

        private final String text;

        FragmentTab(String str) {
            text = str;
        }

        public String text() {
            return text;
        }
    }

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        for (FragmentTab tab : FragmentTab.values()) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.text()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mImageView = findViewById(R.id.imageView);
        profileImage();
        //PictureHash p = new PictureHash("Marcus", "marcus@gmail.com");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        Log.d(LOG_TAG, ": done encoding");
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.NO_WRAP);
        Log.d(LOG_TAG, ": decoding done");
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
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

    private String imageData;

    public void profileImage(){
        //Bitmap bitMap = null;
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(LOG_TAG, " " + queue.toString());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL+"?profileImage=" + SessionObject.getInstance().user().email(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(LOG_TAG, " :" + array.toString());
                        imageData = new JsonParser().parseImage(array);
                        Log.d(LOG_TAG, ": before decoding " + imageData);
                        Bitmap bitMap = null;
                        bitMap = imageData != null ? base64ToBitmap(imageData) : null;
                        Log.d(LOG_TAG, ": after decoding");
                        //Log.d(LOG_TAG, ": Setting the bitmap " + bitMap.toString());
                        mImageView.setImageBitmap(bitMap);

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
}