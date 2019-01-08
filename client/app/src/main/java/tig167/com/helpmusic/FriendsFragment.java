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
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class FriendsFragment extends ListFragment implements OnItemClickListener {

    private static SessionObject session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("on activity created: friendsFragment");
        session = SessionObject.getInstance();
        resetListView();
        getListView().setOnItemClickListener(this);
    }

    public void resetListView() {
        getListView().setAdapter(new UserAdapter(getActivity(), session.user().friends()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowProfileFragment.class);
        intent.putExtra("name", session.user().friends().get(position).name());
        intent.putExtra("email", session.user().friends().get(position).email());
        intent.putExtra("image", session.user().friends().get(position).profileImage());
        startActivity(intent);
    }

}
