package com.tig167.helpmusic.main_app.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.android.volley.VolleyError;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.data.remote.ServerAction;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.VolleyService;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.util.ImageUtil;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.main_app.ui.fragment.PagerAdapter;
import com.tig167.helpmusic.util.PictureHash;
import com.tig167.helpmusic.data.local.db.DbHelper;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;

/**
 * The main screen consists of
 *  - a search bar
 *  - your profile image
 *  - a not yet implemented status update function
 *  - a button for camera access
 *  - a tabbed layout.
 * Tab 1: news feed placeholder
 * Tab 2: friends
 * Tab 3: user profile
 * Tab 4: music functionality placeholder
 */
public class MainActivity extends AppCompatActivity {

    public static final String URL = "http://10.0.2.2:8080/users";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static SessionObject session;
    private static DbHelper storage;

    private VolleyService volleyService;

    private TabLayout tabLayout;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = SessionObject.getInstance();
        storage = DbHelper.getInstance(this);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        tabLayout = TabLayoutFactory.create(findViewById(R.id.tab_layout));
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        addTabListeners(viewPager);
        mImageView = findViewById(R.id.imageView);

        float cornerRadius = 7f;
        Resources res = getResources();
        RoundedBitmapDrawable drawable;
        if (session.user().profileImage() != null) {
            drawable = RoundedBitmapDrawableFactory.create(res, session.user().profileImage());

            drawable.setCornerRadius(cornerRadius);

            mImageView.setImageDrawable(drawable);

            Log.d(LOG_TAG, ": bitmap " + session.user().profileImage().toString());
        }

        instantiateVolleyCallback();
        initSearch();
    }

    private void instantiateVolleyCallback() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray response) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                if ("ok".equals(new JsonParser().jsonToString(response))) {
                    Log.d(LOG_TAG, "Image added");
                } else {
                    Log.d(LOG_TAG, "Image was NOT added");
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d("Error: addProfileImage ", error.getCause().getMessage());
            }
        }, this);
    }

    private void initSearch() {
        Log.d(LOG_TAG, "Init search");
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        //searchView.setIconifiedByDefault(false);
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
            SessionObject.getInstance().setUser(DbHelper.getInstance(this).loadSession());
            session.setSessionStart();
        }
        Log.d(LOG_TAG, "Time since session start: " + session.timeElapsed());
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

    @Override
    public boolean onSearchRequested() {
        Log.d(LOG_TAG, "Request search");
        return super.onSearchRequested();
    }

    public void cameraClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOG_TAG, "Click camera");
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Process the newly taken picture returned from camera. Set the user's profile image (bitmap)
     * and send encoded and hashed image-string as json to server
     *
     * @param requestCode   the type of request sent to camera, eg. take a picture
     * @param resultCode    the status of the task performed by camera, eg. ok
     * @param data          the intent containing the image returned from camera activity
     */
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
            volleyService.postDataVolley(
                    "POST",
                    URL,
                    new JsonParser().imageDataToJson(
                            ServerAction.ADD_PROFILE_IMG.value(),
                            ph.hash(),
                            imageString,
                            session.user().email()
                    )
            );
        }
    }

    String mCurrentPhotoPath;

    //Will be used to create an image file on the phone in the completed application.
    //TODO: fix the method so it works.
    private File createImageFile() throws IOException {
        PictureHash ph = new PictureHash(session.user().name(), session.user().email());
        String fileName = "JPEG_" + ph.hash();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
