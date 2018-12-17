package tig167.com.helpmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends Fragment {

    public UserProfile(){

    }

    private static final String LOG_TAG = UserProfile.class.getSimpleName();

    Button mButton;
    TextView mTextView;
    ImageView mBitMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.d(LOG_TAG, ": Create the userFragment");
        // profile logic
        Log.d(LOG_TAG, ": " + container.getId());

        View view = inflater.inflate(R.layout.user_profile, container, false);



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, ": Create activity");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(LOG_TAG, ": onViewCreated");
        mButton = view.findViewById(R.id.usrFragmentAddFriend);
        mTextView = view.findViewById(R.id.profile_name);
        mBitMap = view.findViewById(R.id.usrFragmentProfileImage);
    }
}
