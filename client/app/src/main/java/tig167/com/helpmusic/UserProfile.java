package tig167.com.helpmusic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfile extends Fragment {

    public UserProfile(){
        System.out.println("user profile constructor");
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

        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mButton = getActivity().findViewById(R.id.usrFragmentAddFriend);
        mTextView = getActivity().findViewById(R.id.profile_name);
        mBitMap = getActivity().findViewById(R.id.usrFragmentProfileImage);
        Log.d(LOG_TAG, ": Create activity");
    }
}
