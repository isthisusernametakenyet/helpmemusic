package tig167.com.helpmusic;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.view.ViewPager;



public class ShowProfileFragment extends FragmentActivity {

    private static final String LOG_TAG = ShowProfileFragment.class.getSimpleName();

    ViewPager mViewPager;
    Button mButton;
    TextView mTextView;
    ImageView mBitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_fragment);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mViewPager = findViewById(R.id.showProfile);
        final SearchProfileAdapter adapter = new SearchProfileAdapter(fm);
        mViewPager.setAdapter(adapter);

        Log.d(LOG_TAG, ": onCreate");




        //UserProfile userProfile = new UserProfile();
        Log.d(LOG_TAG, ": Create UserProfile");
        ft.add(R.id.showProfile, adapter.getItem(1));
        Log.d(LOG_TAG, ": commit FragmentManager and FragmentTransaction");
        ft.commit();

        /*
        mButton = findViewById(R.id.usrFragmentAddFriend);
        mTextView = findViewById(R.id.profile_name);
        Log.d(LOG_TAG, ": textView " + mTextView.toString());
        mBitMap = findViewById(R.id.usrFragmentProfileImage);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            //TODO: do somthing if ther is no information;
            mTextView.setText(SessionObject.getInstance().user().name());
            mBitMap.setImageBitmap(SessionObject.getInstance().user().getProfileImage());
            return;
        }
        //String name = intent.getStringExtra("userName");
        Bitmap bitmap = intent.getParcelableExtra("image");


        mTextView.setText(name);

        mBitMap.setImageBitmap(bitmap);
        */


    }
}
