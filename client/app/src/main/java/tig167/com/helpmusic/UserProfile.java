package tig167.com.helpmusic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

//import android.support.v4.app.FragmentActivity;

public class UserProfile extends Fragment {

    private static final String URL = "http://10.0.2.2:8080/users";
    private String email;
    private Bundle args;

    public UserProfile(){

    }

    private static final String LOG_TAG = UserProfile.class.getSimpleName();

    Button mButton;
    TextView mTextView;
    ImageView mBitMap;

    public static UserProfile newInstance(Bundle args){
        UserProfile u = new UserProfile();

        u.setArguments(args);

        return u;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        args = getArguments();


        View view;

        if(args == null || SessionObject.getInstance().user().friends().contains(args.getString("email")) || SessionObject.getInstance().user().email().equals(args.getString("email"))) {
            view = inflater.inflate(R.layout.user_profile_no_button, container, false);

        }else{
            view = inflater.inflate(R.layout.user_profile, container, false);
            mButton = view.findViewById(R.id.usrFragmentAddFriend);
            //adding a onClickListener since fragment dont have one
            mButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    addFriend(view);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        float cornerRadius = 7f;

        RoundedBitmapDrawable drawable;
        Resources res = getResources();
        Log.d(LOG_TAG, ": onViewCreated");

        mTextView = view.findViewById(R.id.profile_name);
        mBitMap = view.findViewById(R.id.usrFragmentProfileImage);
        //Log.d(LOG_TAG, ": " + args.toString());
        if(args == null) {
            if (SessionObject.getInstance().user().getProfileImage() != null) {
                drawable = RoundedBitmapDrawableFactory.create( res, SessionObject.getInstance().user().getProfileImage());

                drawable.setCornerRadius(cornerRadius);

                mBitMap.setImageDrawable(drawable);
                //mBitMap.setImageBitmap(SessionObject.getInstance().user().getProfileImage());
                Log.d(LOG_TAG, ": bitmap " + SessionObject.getInstance().user().getProfileImage().toString());
            }
            mTextView.setText(SessionObject.getInstance().user().name());
        }else{


            String name = args.getString("name");
            mTextView.setText(name);
            Bitmap image = (Bitmap) args.get("image");
            drawable = RoundedBitmapDrawableFactory.create( res, image);

            drawable.setCornerRadius(cornerRadius);

            mBitMap.setImageDrawable(drawable);
            email = args.getString("email");
        }
    }

    public void addFriend(View view){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JSONArray json = new JsonParser().addFriend(Action.ADD_FRIEND.value(), email , SessionObject.getInstance().user().name(), SessionObject.getInstance().user().email());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                URL,
                json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        Context context = getActivity();
                        String str = new JsonParser().jsonToLoginResponse(array);
                        if("ok".equalsIgnoreCase(str)){
                            CharSequence okText = "Added friend " + mTextView.getText();
                            Toast.makeText(context, okText, Toast.LENGTH_SHORT).show();
                        }else{
                            CharSequence failedText = "Failed to add friend " + mTextView.getText();
                            Toast.makeText(context, failedText, Toast.LENGTH_SHORT).show();
                        }
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
