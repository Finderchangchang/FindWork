package wai.findwork.ui;

import android.text.TextUtils;
import android.widget.EditText;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.Utils;
import wai.findwork.model.Config;
import wai.findwork.model.RiLi;
import wai.findwork.model.UserInfo;

/**
 * Created by Administrator on 2017/2/7.
 */

public class AddRiLiActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    TitleBar titleBar;
    @Bind(R.id.content_et)
    EditText contentEt;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        titleBar.setRightClick(() -> {
            //执行保存操作
            if (TextUtils.isEmpty(contentEt.getText().toString().trim())) {
                ToastShort("请输入一些内容");
            } else {
                RiLi riLi = new RiLi();
                UserInfo userInfo = new UserInfo();
                userInfo.setObjectId(Utils.getCache(Config.KEY_ID));
                riLi.setUser(userInfo);
                riLi.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ToastShort(s);
                            setResult(99);
                            finish();
                        } else {
                            ToastShort("保存失败，请稍后重试");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void initEvents() {

    }

    @Override
    public int setLayout() {
        return R.layout.ac_add_rili;
    }
}
