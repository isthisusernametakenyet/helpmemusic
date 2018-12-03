package tig167.com.helpmusic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int count;

    PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        count = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return null;
            case 1:
                return new FriendsFragment();
            case 2:
            case 3:
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
