package tig167.com.helpmusic;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
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

import org.json.JSONArray;

import java.util.List;

public class SearchBar extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String LOG_TAG = SearchBar.class.getSimpleName();
    private static final String URL = "http://10.0.2.2:8080/users";

    SearchView mSearchBar;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);
        mSearchBar = findViewById(R.id.searchBar);
        //mSearchBar.requestFocus();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    public void doMySearch(String query){
        RequestQueue queue = Volley.newRequestQueue(this);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL+"?getSearchResult=" + query,
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
        ListView listView = findViewById(R.id.searchResult);
        UserAdapter adapter = new UserAdapter(getApplicationContext(), users);
        Log.d(LOG_TAG, ": create a new adapter whith count " + adapter.getCount());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // show friend profile

        resetListView();
    }

    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        setIntent(intent);
    }
}