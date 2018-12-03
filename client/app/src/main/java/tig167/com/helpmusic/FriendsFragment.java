package tig167.com.helpmusic;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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

    private static final String SERVER_REQUEST_FRIENDS = "?getFriends=";

    private List<User> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        Log.d("debug","FRIENDS_FRAGMENT[onCreateView]");
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("debug","FRIENDS_FRAGMENT[onActivityCreated]");

        //getFriends();
        friends = new ArrayList<>();
        friends.add(new User("Lasse Kongo", "usr1337@remote.webz"));
        friends.add(new User("Jan Banan", "janne@dark.net"));

        resetListView();

    }

    private void resetListView() {
        ListView listView = getListView();

        UserAdapter userAdapter = new UserAdapter(getActivity(), friends);
        listView.setAdapter(userAdapter);
    }

    private void getFriends() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL + SERVER_REQUEST_FRIENDS + SessionObject.getInstance().user().email(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        friends = new JsonParser().jsonToUsers(array);
                        resetListView();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error: ", error.getCause().getMessage());
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // show friend profile


        Log.d("debug","FRIENDS_FRAGMENT[onItemClick:" + position + "]");
        resetListView();
    }

}
