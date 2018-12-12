package tig167.com.helpmusic;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class ShowProfileFragment extends AppCompatActivity {

    private static final String LOG_TAG = ShowProfileFragment.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_fragment);


        Log.d(LOG_TAG, ": onCreate");

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        UserProfile userProfile = new UserProfile();
        Log.d(LOG_TAG, ": Create UserProfile");
        ft.add(R.id.view_pager, userProfile);
        Log.d(LOG_TAG, ": commit FragmentManager and FragmentTransaction");
        ft.commit();



    }
}
