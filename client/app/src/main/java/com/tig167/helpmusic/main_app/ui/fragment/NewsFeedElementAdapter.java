package com.tig167.helpmusic.main_app.ui.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tig167.helpmusic.R;

import java.util.List;

/**
 * This class is under construction.
 */
public class NewsFeedElementAdapter extends ArrayAdapter<NewsFeedElement> {

    private Context c;
    private List<NewsFeedElement> feed;

    NewsFeedElementAdapter(Context context, List<NewsFeedElement> list) {
        super(context, 0, list);
        c = context;
        feed = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(c).inflate(R.layout.feed_item, parent, false);
        }
        NewsFeedElement element = feed.get(position);
        TextView msg = listItem.findViewById(R.id.textView_msg);
        msg.setText(element.message());
        return listItem;
    }
}