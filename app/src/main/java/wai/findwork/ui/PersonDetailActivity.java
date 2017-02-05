package wai.findwork.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.view.TitleBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.Utils;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.UserBuy;
import wai.findwork.model.UserInfo;
import wai.findwork.view.GlideCircleTransform;

/**
 * 用户详情
 * Created by Finder丶畅畅 on 2017/2/4 21:23
 * QQ群481606175
 */

public class PersonDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.user_iv)
    ImageView userIv;
    @Bind(R.id.user_name_tv)
    TextView userNameTv;
    @Bind(R.id.user_type_tv)
    TextView userTypeTv;
    @Bind(R.id.id_card_tv)
    TextView idCardTv;
    @Bind(R.id.tel_tv)
    TextView telTv;
    @Bind(R.id.gz_tv)
    TextView gzTv;
    @Bind(R.id.remark_tv)
    TextView remarkTv;
    @Bind(R.id.get_tel_btn)
    Button getTelBtn;
    UserInfo info;

    @Override
    public void initViews() {
        info = (UserInfo) getIntent().getSerializableExtra("user");
        toolbar.setLeftClick(() -> finish());
    }

    @Override
    public void initEvents() {
        if (Utils.getCache(Config.KEY_ID).equals(info.getObjectId())) {
            telTv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
            getTelBtn.setVisibility(View.GONE);
        } else {
            BmobQuery<UserBuy> query = new BmobQuery<>();
            UserInfo userInfo = new UserInfo();
            userInfo.setObjectId(info.getObjectId());
            query.addWhereEqualTo("user", userInfo);
            UserInfo buyer = new UserInfo();
            buyer.setObjectId(Utils.getCache(Config.KEY_ID));
            query.addWhereEqualTo("buyer", buyer);
            query.findObjects(new FindListener<UserBuy>() {
                @Override
                public void done(List<UserBuy> list, BmobException e) {
                    if (e == null && list.size() > 0) {
                        telTv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
                        getTelBtn.setText("拨打电话");
                    }
                }
            });
        }
        Glide.with(MainActivity.main)
                .load(info.getIconurl()).transform(new GlideCircleTransform(this))
                .into(userIv);
        userNameTv.setText(info.getRealname());
        idCardTv.setText(info.getCardnum());
        userTypeTv.setText(info.getTypeName());

        gzTv.setText("工资：" + info.getGongzi());
        remarkTv.setText("备注：" + info.getRemark());
    }

    @Override
    public int setLayout() {
        return R.layout.ac_person_detail;
    }
}
