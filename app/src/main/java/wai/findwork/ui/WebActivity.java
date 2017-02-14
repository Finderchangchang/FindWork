package wai.findwork.ui;

import android.os.Bundle;
import android.webkit.WebView;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import wai.findwork.BaseActivity;
import wai.findwork.R;

/**
 * Created by Finder丶畅畅 on 2017/2/13 22:45
 * QQ群481606175
 */

public class WebActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    TitleBar titleBar;
    @Bind(R.id.ac_web)
    WebView acWeb;
    String url;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        url = getIntent().getStringExtra("url");
    }

    @Override
    public void initEvents() {
        switch (url) {
            case "about_us":
                url = "www.baidu.com";
                break;
        }
        acWeb.loadUrl(url);
    }

    @Override
    public int setLayout() {
        return R.layout.ac_web;
    }
}
