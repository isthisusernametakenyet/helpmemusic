package tig167.com.helpmusic;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = PagerAdapter.class.getSimpleName();
    private int count;

    public enum FragmentTab {
        NEWS_FEED("News"),
        FRIENDS("Friends"),
        USER_PROFILE("Profile"),
        MUSIC("Music");

        private final String title;

        FragmentTab(String title) { this.title = title; }

        public String title() { return title; }
    }

    private SparseArray<FragmentTab> tabs;

    public PagerAdapter(FragmentManager mgr) {
        super(mgr);
        setTabIndex();
    }

    public void setTabIndex() {
        tabs = new SparseArray<>();
        count = 0;
        for (FragmentTab tab : FragmentTab.values()) {
            tabs.put(tab.ordinal(), tab);
            count++;
        }
    }

    public static TabLayout createTabLayout(View view) {
        TabLayout tabLayout = (TabLayout) view;
        for (FragmentTab tab : FragmentTab.values()) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.title()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        return tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentTab tab = tabs.get(position);
        switch (tab) {
            case NEWS_FEED:
                return new NewsFeedFragment();
            case FRIENDS:
                return new FriendsFragment();
            case USER_PROFILE:
                return new UserProfileFragment();
            case MUSIC:
                return new MusicFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

}
