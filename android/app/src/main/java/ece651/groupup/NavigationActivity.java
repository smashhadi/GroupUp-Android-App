package ece651.groupup;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class NavigationActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        final NavigationPagerAdapter adapter =  new NavigationPagerAdapter(getSupportFragmentManager());
        final ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_feed));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_groups_overview));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}

class NavigationPagerAdapter extends FragmentPagerAdapter {
    public NavigationPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab0 = new FeedFragment();
                return tab0;
            case 1:
                Fragment tab1 = new GroupsOverviewFragment();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
