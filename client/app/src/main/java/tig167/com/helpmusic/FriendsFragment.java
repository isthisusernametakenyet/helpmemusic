package tig167.com.helpmusic;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendsFragment extends ListFragment implements OnItemClickListener {

    private static final String LOG_TAG = FriendsFragment.class.getSimpleName();
    private static SessionObject session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        Log.d(LOG_TAG," create view");
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG," activity created");
        session = SessionObject.getInstance();
        resetListView();
        getListView().setOnItemClickListener(this);
    }

    public void resetListView() {
        getListView().setAdapter(new UserAdapter(getActivity(), session.user().friends()));
        Log.d(LOG_TAG," reset list-view");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowProfileFragment.class);
        User user = session.user().friends().get(position);
        intent.putExtra("name", user.name());
        intent.putExtra("email", user.email());
        intent.putExtra("image", user.profileImage());
        Log.d(LOG_TAG," Intent: show profile");
        startActivity(intent);
    }

}
