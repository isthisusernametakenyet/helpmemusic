package com.tig167.helpmusic.main_app.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Not used.
 */

public class SearchProfileAdapter extends FragmentStatePagerAdapter {

    public SearchProfileAdapter(FragmentManager fm){
        super(fm);
    }

    public Fragment getItem(int pos){
        return new UserProfileFragment();
    }

    public int getCount(){
        return 1;
    }
}
