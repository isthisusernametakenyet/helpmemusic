package com.tig167.helpmusic.main_app.ui;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.tig167.helpmusic.main_app.ui.fragment.PagerAdapter;

/**
 * Creates the tab layout with titled tabs used in main activity.
 */
class TabLayoutFactory {

    static TabLayout create(View view) {
        TabLayout tabLayout = (TabLayout) view;
        for (PagerAdapter.FragmentTab tab : PagerAdapter.FragmentTab.values()) {
            tabLayout.addTab(tabLayout.newTab().setText(tab.title()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        return tabLayout;
    }
}
