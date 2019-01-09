package com.tig167.helpmusic.main_app.ui.fragment;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.SessionObject;
import com.tig167.helpmusic.main_app.model.User;

/**
 * Fragment with logic to control list of friends, eg. show profile when clicked.
 */

public class FriendsFragment extends ListFragment implements OnItemClickListener {

    private static final String LOG_TAG = FriendsFragment.class.getSimpleName();
    private static SessionObject session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        Log.d(LOG_TAG,"Create view");
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG,"Activity created");
        session = SessionObject.getInstance();
        resetListView();
        getListView().setOnItemClickListener(this);
    }

    public void resetListView() {
        getListView().setAdapter(new UserAdapter(getActivity(), session.user().friends()));
        Log.d(LOG_TAG,"Reset list-view");
    }

    /**
     * Callback method to show profile when clicked in list.
     *
     * @param parent    the parent AdapterView where the click was detected
     * @param view      the clicked view within AdapterView provided by adapter
     * @param position  the view position in adapter
     * @param id        the clicked item row id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowProfileFragment.class);
        User user = session.user().friends().get(position);
        intent.putExtra("name", user.name());
        intent.putExtra("email", user.email());
        intent.putExtra("image", user.profileImage());
        Log.d(LOG_TAG,"Intent: show profile");
        startActivity(intent);
    }

}
