package tig167.com.helpmusic;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://10.0.2.2:8080/users";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static SessionObject session;

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
        session = SessionObject.getInstance();
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
        initSearch();
        PictureHash p = new PictureHash("Marcus", "marcus@gmail.com");
    }

    private void initSearch(){
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search_bar);

        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(LOG_TAG, " data.getExtra() " + data.getExtras().toString());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String imageString = new Image().encode(imageBitmap);
            Log.d(LOG_TAG, " Base64 encode " + imageString);
            mImageView.setImageBitmap(new Image().decode(imageString));
            session.user().setProfileImage(imageBitmap);
            Log.d(LOG_TAG, " Decode base64 to bitmap");
            PictureHash ph = new PictureHash(session.user().name(), session.user().email());
            addProfileImage(new JsonParser().imageDataToJson(
                    Action.ADD_PROFILE_IMG.value(),
                    ph.hash(),
                    imageString,
                    session.user().email()
            ));
        }
    }

    private void addProfileImage(JSONArray json) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                URL,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error: addUser ", error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
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

    private String imageData;

    public void profileImage(){
        //Bitmap bitMap = null;
        if(SessionObject.getInstance().user().getProfileImage() != null){
            mImageView.setImageBitmap(SessionObject.getInstance().user().getProfileImage());
        }else {
            RequestQueue queue = Volley.newRequestQueue(this);
            Log.d(LOG_TAG, " " + queue.toString());
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL + "?getProfileImg=" + SessionObject.getInstance().user().email(),
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray array) {
                            Log.d(LOG_TAG, " :" + array.toString());
                            imageData = new JsonParser().parseImage(array);
                            Log.d(LOG_TAG, ": before decoding " + imageData);
                            Bitmap bitMap = null;
                            bitMap = imageData != null ? new Image().decode(imageData) : null;
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

}