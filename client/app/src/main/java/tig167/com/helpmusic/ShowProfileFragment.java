package tig167.com.helpmusic;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

//import android.support.v7.app.AppCompatActivity;



public class ShowProfileFragment extends FragmentActivity {

    private static final String LOG_TAG = ShowProfileFragment.class.getSimpleName();

    ScrollView mViewPager;
    Button mButton;
    TextView mTextView;
    ImageView mBitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_fragment);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mViewPager = findViewById(R.id.showprofilescrollview);
        //final SearchProfileAdapter adapter = new SearchProfileAdapter(fm);
        //mViewPager.setAdapter(adapter);

        Log.d(LOG_TAG, ": onCreate");

        Bundle args = getIntent().getExtras();

        UserProfileFragment userProfile = UserProfileFragment.newInstance(args);
        Log.d(LOG_TAG, ": Create UserProfileFragment");

        ft.add(mViewPager.getId(), userProfile);
        Log.d(LOG_TAG, ": commit FragmentManager and FragmentTransaction");

        ft.commit();
        Log.d(LOG_TAG, ft.toString());



    }
}
