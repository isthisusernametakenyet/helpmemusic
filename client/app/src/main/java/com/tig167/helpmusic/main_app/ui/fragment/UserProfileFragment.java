package com.tig167.helpmusic.main_app.ui.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.VolleyService;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.data.local.db.DbHelper;
import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.data.remote.ServerAction;
import com.tig167.helpmusic.main_app.ui.MainActivity;

import org.json.JSONArray;

public class UserProfileFragment extends Fragment {

    private static final String LOG_TAG = UserProfileFragment.class.getSimpleName();
    private static SessionObject session = SessionObject.getInstance();
    private static DbHelper storage;

    private VolleyService volleyService;
    private Bundle args;
    private User user;

    Button mButton;
    TextView mTextView;
    ImageView mBitMap;

    public static UserProfileFragment newInstance(Bundle args) {
        UserProfileFragment u = new UserProfileFragment();

        u.setArguments(args);

        return u;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        storage = DbHelper.getInstance(getContext());

        View view;
        args = getArguments();

        if (args != null) {
            user = new User(
                    args.getString("name"),
                    args.getString("email"),
                    (Bitmap) args.get("image")
            );
        }

        if (args == null || user.isUnfriendable(session.user())) {
            view = inflater.inflate(R.layout.user_profile_no_button, container, false);
        } else {
            onResult();
            view = inflater.inflate(R.layout.user_profile, container, false);
            mButton = view.findViewById(R.id.usrFragmentAddFriend);
            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    volleyService.postDataVolley(
                            "POST",
                            MainActivity.URL,
                            new JsonParser().friendshipToJson(
                                    ServerAction.ADD_FRIEND.value(),
                                    session.user().email(),
                                    user.email()
                            )
                    );
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, ": Create activity");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        float cornerRadius = 7f;

        RoundedBitmapDrawable drawable;
        Resources res = getResources();
        Log.d(LOG_TAG, ": onViewCreated");

        mTextView = view.findViewById(R.id.profile_name);
        mBitMap = view.findViewById(R.id.usrFragmentProfileImage);
        //Log.d(LOG_TAG, ": " + args.toString());
        if (args == null) {
            if (session.user().profileImage() != null) {
                drawable = RoundedBitmapDrawableFactory.create(res, session.user().profileImage());

                drawable.setCornerRadius(cornerRadius);

                mBitMap.setImageDrawable(drawable);
                //mBitMap.setImageBitmap(SessionObject.getInstance().user().getProfileImage());
                Log.d(LOG_TAG, ": bitmap " + session.user().profileImage().toString());
            }
            mTextView.setText(session.user().name());
        } else {
            mBitMap.setImageBitmap(session.user().profileImage());
            mTextView.setText(session.user().name());
            mTextView.setText(user.name());
            drawable = RoundedBitmapDrawableFactory.create(res, user.profileImage());
            drawable.setCornerRadius(cornerRadius);
            mBitMap.setImageDrawable(drawable);
        }
    }

    private void onResult() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray arr) {
                String str = new JsonParser().jsonToLoginResponse(arr);
                if ("ok".equalsIgnoreCase(str)) {
                    session.user().addFriend(user);
                    storage.save(user);
                    CharSequence okText = "Added friend " + mTextView.getText();
                    Toast.makeText(getContext(), okText, Toast.LENGTH_SHORT).show();
                } else {
                    CharSequence failedText = "Failed to add friend " + mTextView.getText();
                    Toast.makeText(getContext(), failedText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
            }
        }, getContext());
    }

}