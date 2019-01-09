package com.tig167.helpmusic.main_app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tig167.helpmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Feed placeholder.
 * This class is under construction.
 */
public class NewsFeedFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.newsfeed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Feed placeholder
        List<NewsFeedElement> feed = new ArrayList<>();
        feed.add(new NewsFeedElement("hello feed"));
        ListView listView = getListView();
        NewsFeedElementAdapter feedAdapter = new NewsFeedElementAdapter(getActivity(), feed);
        listView.setAdapter(feedAdapter);
    }

}
