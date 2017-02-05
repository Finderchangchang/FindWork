package wai.findwork.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Finder丶畅畅 on 2017/1/17 21:15
 * QQ群481606175
 */

public class MainAdapter extends FragmentPagerAdapter {
    private String mTitles[] = {"首页", "工种", "班组", "项目", "资讯"};

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
