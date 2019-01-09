package com.tig167.helpmusic.main_app.ui.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tig167.helpmusic.R;
import com.tig167.helpmusic.main_app.model.User;

import java.util.List;

/**
 * Logic to control each item in friends fragment list.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Context c;
    private List<User> friendList;

    public UserAdapter(Context context, List<User> list) {
        super(context, R.layout.fragment_friends, list);
        c = context;
        friendList = list;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(c).inflate(R.layout.friend_layout, parent, false);
        }
        User friend = friendList.get(position);

        ImageView imageView = listItem.findViewById(R.id.imageView);

        imageView.setImageBitmap(friend.profileImage());
        if (friend.profileImage() != null) {
            imageView.setImageBitmap(friend.profileImage());
        }
        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(friend.name());
        return listItem;
    }

}
