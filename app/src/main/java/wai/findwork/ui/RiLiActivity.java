package wai.findwork.ui;

import android.os.Bundle;
import android.widget.ListView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.adapter.CommonAdapter;
import wai.findwork.adapter.CommonViewHolder;
import wai.findwork.method.Utils;
import wai.findwork.model.Config;
import wai.findwork.model.RiLi;
import wai.findwork.model.UserInfo;

/**
 * 记事本列表
 * Created by Finder丶畅畅 on 2017/2/4 22:48
 * QQ群481606175
 */

public class RiLiActivity extends BaseActivity {
    CommonAdapter<RiLi> commonAdapter;
    List<RiLi> list;
    @Bind(R.id.title_bar)
    TitleBar titleBar;
    @Bind(R.id.rili_lv)
    ListView riliLv;

    @Override
    public void initViews() {
        list = new ArrayList<>();
        commonAdapter = new CommonAdapter<RiLi>(this, list, R.layout.item_new) {
            @Override
            public void convert(CommonViewHolder holder, RiLi riLi, int position) {
                holder.setVisible(R.id.title_tv, false);
                holder.setText(R.id.content_tv, riLi.getContent());
            }
        };
        riliLv.setAdapter(commonAdapter);
    }

    @Override
    public void initEvents() {
        BmobQuery<RiLi> query = new BmobQuery<>();
        UserInfo buyer = new UserInfo();
        buyer.setObjectId(Utils.getCache(Config.KEY_ID));
        query.addWhereEqualTo("user", buyer);
        query.findObjects(new FindListener<RiLi>() {
            @Override
            public void done(List<RiLi> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    commonAdapter.refresh(list);
                }
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_ri_li;
    }
}
