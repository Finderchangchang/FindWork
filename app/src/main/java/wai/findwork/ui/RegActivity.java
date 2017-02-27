package wai.findwork.ui;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.Utils;
import wai.findwork.model.Config;
import wai.findwork.model.UserInfo;

public class
RegActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.cofirm_pwd_et)
    EditText cofirmPwdEt;
    @Bind(R.id.reg_btn)
    Button regBtn;

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        regBtn.setOnClickListener(view -> {
            String tel = telEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            String cofirm = cofirmPwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (!Utils.isMobileNo(tel)) {
                ToastShort("请输入正确手机号码");
            } else if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cofirm)) {
                ToastShort("前后密码不一致请重新输入");
            } else if (!pwd.equals(cofirm)) {
                ToastShort("前后密码不一致请重新输入");
            } else {
                BmobUser bu = new BmobUser();
                bu.setUsername(tel);
                bu.setPassword(pwd);
                bu.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser s, BmobException e) {
                        if (e == null) {
                            ToastShort("注册成功");
                            finish();
                        } else {
                            ToastShort(getResources().getString(R.string.no_wang));
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
        return R.layout.ac_reg;
    }
}
