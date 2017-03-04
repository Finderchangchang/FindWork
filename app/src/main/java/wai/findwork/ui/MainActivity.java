package wai.findwork.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import wai.findwork.BaseActivity;
import wai.findwork.R;

public class MainActivity extends BaseActivity {
    private MainAdapter mAdapter;
    @Bind(R.id.tab_pager)
    ViewPager mPager;
    @Bind(R.id.bottom_tab)
    TabLayout bottom_tab;
    public static MainActivity main;

    @Override
    public void initViews() {
        mAdapter = new MainAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(5);
        bottom_tab.setupWithViewPager(mPager);
        main = this;
    }

    @Override
    public void initEvents() {

    }

    @Override
    public int setLayout() {
        return R.layout.ac_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MainActivity.main).onActivityResult(requestCode,resultCode,data);
    }
}
