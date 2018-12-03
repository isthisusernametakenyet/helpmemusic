package tig167.com.helpmusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context c;
    private List<User> friendList;

    UserAdapter(Context context, List<User> list) {
        super(context, 0, list);
        c = context;
        friendList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
        }
        User friend = friendList.get(position);

        // image view

        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(friend.name());
        return listItem;
    }

}