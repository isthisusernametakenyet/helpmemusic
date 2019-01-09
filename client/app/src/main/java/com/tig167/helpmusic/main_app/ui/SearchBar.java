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

import com.android.volley.VolleyError;
import com.tig167.helpmusic.data.remote.JsonParser;
import com.tig167.helpmusic.R;
import com.tig167.helpmusic.data.remote.ServerAction;
import com.tig167.helpmusic.data.remote.VolleyResultCallback;
import com.tig167.helpmusic.data.remote.VolleyService;
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
    private VolleyService volleyService;

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
            setResultCallback();
            doMySearch(query);
        }
    }

    private void setResultCallback() {
        volleyService = new VolleyService(new VolleyResultCallback() {

            @Override
            public void notifySuccess(String requestType, JSONArray arr) {
                users = new JsonParser().jsonToUsers(arr);
                Log.d(LOG_TAG, "parsed json " + users.toString());
                resetListView();
            }

            @Override
            public void notifyError(String requestType, VolleyError ve) {
                Log.d(LOG_TAG, "Volley requester " + requestType);
                Log.d(LOG_TAG, "Error: " + ve.getCause().getMessage());
            }
        }, getApplicationContext());
    }

    private void doMySearch(String query) {
        final String URL = MainActivity.URL + ServerAction.GET_SEARCH_RESULT.value() + query;
        volleyService.getDataVolley("GET", URL);
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
