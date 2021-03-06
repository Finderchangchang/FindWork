package wai.findwork.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    String url;
    @Bind(R.id.time_tv)
    TextView timeTv;

    @Override
    public void initViews() {

    }

    @Override
    public void initEvents() {
        titleBar.setLeftClick(() -> finish());
        url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            model = (ArticleModel) getIntent().getSerializableExtra(Config.KEY_NEW_ID);
            titleTv.setText(model.getTitle());
            contentTv.setText(model.getContent());
            timeTv.setText(model.getCreatedAt());
        } else {
            BmobQuery<ArticleModel> query = new BmobQuery<>();
            titleBar.setCentertv("关于我们");
            query.addWhereEqualTo("title", "关于我们");
            query.findObjects(new FindListener<ArticleModel>() {
                @Override
                public void done(List<ArticleModel> list, BmobException e) {
                    if (e == null) {
                        if (list.size() > 0) {
                            model = list.get(0);
                            titleTv.setText(model.getTitle());
                            contentTv.setText(model.getContent());
                            timeTv.setText(model.getCreatedAt());
                        }
                    }
                }
            });
        }
    }

    @Override
    public int setLayout() {
        return R.layout.ac_new_detail;
    }
}
