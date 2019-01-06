package com.tig167.helpmusic.main_app.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.model.User;
import com.tig167.helpmusic.main_app.ui.fragment.ShowProfileFragment;
import com.tig167.helpmusic.main_app.ui.fragment.UserAdapter;

import org.json.JSONArray;

import java.util.List;

public class SearchBar extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = SearchBar.class.getSimpleName();

    SearchView mSearchBar;
    ListView mListView;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        mSearchBar = findViewById(R.id.searchBar);
        //mSearchBar.requestFocus();
        mListView = findViewById(R.id.searchResult);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    public void doMySearch(String query){
        RequestQueue queue = Volley.newRequestQueue(this);

        Log.d(LOG_TAG, ": query = " + query);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.URL+"?getSearchResult=" + query,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {

                        JsonParser parser = new JsonParser();
                        users = parser.jsonToUsers(array);
                        Log.d(LOG_TAG, ": parsed json " + users.toString());
                        resetListView();

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

    private void resetListView(){
        mListView.setOnItemClickListener(this);
        ListView listView = findViewById(R.id.searchResult);
        UserAdapter adapter = new UserAdapter(getApplicationContext(), users);
        //Log.d(LOG_TAG, ": create a new adapter with count " + adapter.getCount());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this, ShowProfileFragment.class);
        Log.d(LOG_TAG, ": SearchBar onItemClick");
        User user = users.get(position);
        Log.d(LOG_TAG, ": user name " + user.name());
        intent.putExtra("name", user.name());
        intent.putExtra("email", user.email());
        intent.putExtra("image", user.profileImage());
        startActivity(intent);
    }

    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        setIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
}
