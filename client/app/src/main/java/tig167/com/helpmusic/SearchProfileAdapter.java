package tig167.com.helpmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SearchProfileAdapter extends FragmentStatePagerAdapter {

    public SearchProfileAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getItem(int pos){
        return new UserProfile();
    }

    public int getCount(){
        return 1;
    }
}
