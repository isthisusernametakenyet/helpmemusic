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

import java.util.ArrayList;
import java.util.List;

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
        session = SessionObject.getInstance();
        getFriends();
        resetListView();
        getListView().setOnItemClickListener(this);
    }

    private void resetListView() {
        ListView listView = getListView();
        UserAdapter userAdapter = new UserAdapter(getActivity(), session.user().friends());
        listView.setAdapter(userAdapter);
    }

    private void getFriends() {
        final String SERVER_REQUEST_FRIENDS = "?getFriends=";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL + SERVER_REQUEST_FRIENDS + session.user().email(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        session.user().setFriends(new JsonParser().jsonToUsers(array));
                        Log.d("FriendsFragment: ", "friendList " + session.user().friends());
                        resetListView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error: getFriends. ", error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), ShowProfileFragment.class);
        //Log.d(LOG_TAG, ": SearchBar onItemClick");
        //Log.d(LOG_TAG, ": user name " + friends.get(position).name());
        intent.putExtra("name", session.user().friends().get(position).name());
        intent.putExtra("image", session.user().friends().get(position).profileImage());
        intent.putExtra("email", session.user().friends().get(position).email());
        startActivity(intent);

        //resetListView();
    }

}
