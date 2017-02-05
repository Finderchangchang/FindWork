package wai.findwork.ui;

import android.os.Bundle;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.model.ArticleModel;
import wai.findwork.model.Config;

/**
 * 资讯详情
 * Created by Finder丶畅畅 on 2017/2/5 21:50
 * QQ群481606175
 */

public class NewDetailActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    TitleBar titleBar;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.read_url_tv)
    TextView readUrlTv;
    ArticleModel model;

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
        titleBar.setLeftClick(() -> finish());
        model = (ArticleModel) getIntent().getSerializableExtra(Config.KEY_NEW_ID);
        titleTv.setText(model.getTitle());
        contentTv.setText(model.getContent());
    }

    @Override
    public int setLayout() {
        return R.layout.ac_new_detail;
    }
}
