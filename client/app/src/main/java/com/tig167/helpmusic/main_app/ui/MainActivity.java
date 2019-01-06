package com.tig167.helpmusic.main_app.ui;

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
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.data.remote.ServerAction;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.util.ImageUtil;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.main_app.ui.fragment.PagerAdapter;
import com.tig167.helpmusic.util.PictureHash;
import com.tig167.helpmusic.data.local.db.DbHelper;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://10.0.2.2:8080/users";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static SessionObject session;
    private static DbHelper storage;

    private MainActivity mainActivity;

    private TabLayout tabLayout;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = SessionObject.getInstance();
        storage = DbHelper.getInstance(this);
        mainActivity = this;
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        tabLayout = PagerAdapter.createTabLayout(findViewById(R.id.tab_layout));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        addTabListeners(viewPager);
        mImageView = findViewById(R.id.imageView);
        profileImage();
        initSearch();
    }

    private void addTabListeners(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d(LOG_TAG, "Current tab: " + tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(LOG_TAG, "Previous tab: " + tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(LOG_TAG, "Revisit tab: " + tab.getText());
            }
        });
    }

    private void getSession() {
        if (null == session.user()) {
            session.setUser(storage.loadSession());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getSession();
        Log.d(LOG_TAG, "Start main");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSession();
        Log.d(LOG_TAG, "Resume main");
    }

    @Override
    protected void onPause() {
        super.onPause();
        storage.saveSession(session.user());
        Log.d(LOG_TAG, "Pause main");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage.saveSession(session.user());
        Log.d(LOG_TAG, "Destroy main");
    }

    @Override
    protected void onStop() {
        super.onStop();
        storage.saveSession(session.user());
        Log.d(LOG_TAG, "Stop main");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "Save instance state");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(LOG_TAG, "Restore instance state");
    }

    private void initSearch() {
        Log.d(LOG_TAG, "Init search");
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //searchView.setIconifiedByDefault(false);
    }

    @Override
        public boolean onSearchRequested() {
        Log.d(LOG_TAG, "Request search");
        return super.onSearchRequested();
    }

    public void cameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOG_TAG, "Click camera");
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(LOG_TAG, "data.getExtra() " + data.getExtras().toString());
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String imageString = ImageUtil.encode(imageBitmap);
            Log.d(LOG_TAG, "Base64 encode " + imageString);
            mImageView.setImageBitmap(ImageUtil.decode(imageString));
            session.user().setProfileImage(imageBitmap);
            Log.d(LOG_TAG, "Decode base64 to bitmap");
            PictureHash ph = new PictureHash(session.user().name(), session.user().email());
            addProfileImage(new JsonParser().imageDataToJson(
                    ServerAction.ADD_PROFILE_IMG.value(),
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
                        Log.d("Error: addProfileImage ", error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        PictureHash ph = new PictureHash(session.user().name(), session.user().email());
        String fileName = "JPEG_" + ph.hash();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String imageData;

    public void profileImage() {
        //Bitmap bitMap = null;
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(LOG_TAG, " " + queue.toString());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL + "?getProfileImg=" + session.user().email(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Log.d(LOG_TAG, " :" + array.toString());
                        imageData = new JsonParser().parseImage(array);
                        Log.d(LOG_TAG, ": before decoding " + imageData);
                        Bitmap bitMap = null;
                        bitMap = imageData != null ? ImageUtil.decode(imageData) : null;
                        Log.d(LOG_TAG, ": after decoding");
                        //Log.d(LOG_TAG, ": Setting the bitmap " + bitMap.toString());
                        mImageView.setImageBitmap(bitMap);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
                        if (SessionObject.getInstance().user().profileImage() != null) {
                            mImageView.setImageBitmap(SessionObject.getInstance().user().profileImage());
                        } else {
                            RequestQueue queue = Volley.newRequestQueue(mainActivity);
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
                                            bitMap = imageData != null ? ImageUtil.decode(imageData) : null;
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
        );

    }
}