package wai.findwork.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
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
    @Bind(R.id.riji_et_title)
    EditText riji_et_title;
    @Bind(R.id.riji_create_tiem)
    TextView riji_create_tiem;
    RiLi riLi;
    @Bind(R.id.riji_btn_delete)
    Button riji_btn_delete;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        riLi = (RiLi) getIntent().getSerializableExtra("RILIMODEL");
        if (riLi != null) {
            riji_et_title.setText(riLi.getTitle());
            contentEt.setText(riLi.getContent());
            riji_create_tiem.setVisibility(View.VISIBLE);
            riji_create_tiem.setText(riLi.getCreatedAt());
            riji_btn_delete.setVisibility(View.VISIBLE);
        } else {
            riji_create_tiem.setVisibility(View.GONE);
            riji_btn_delete.setVisibility(View.GONE);
        }
        titleBar.setRightClick(() -> {
            //执行保存操作
            if (riji_et_title.getText().toString().trim().equals("")) {
                ToastShort("请输入标题");
            } else if (TextUtils.isEmpty(contentEt.getText().toString().trim())) {
                ToastShort("请输入日记内容");
            } else {
                RiLi riLi = new RiLi();
                riLi.setContent(contentEt.getText().toString().trim());
                riLi.setTitle(riji_et_title.getText().toString().trim());
                UserInfo userInfo = new UserInfo();
                userInfo.setObjectId(Utils.getCache(Config.KEY_ID));
                riLi.setUser(userInfo);
                riLi.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ToastShort("保存成功");
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
        riji_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                riLi.setObjectId(riLi.getObjectId());
                riLi.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            setResult(99);
                            ToastShort("删除成功");
                            finish();
                        } else {
                            ToastShort("删除失败" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_add_rili;
    }
}
