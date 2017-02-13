package wai.findwork.ui;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import net.tsz.afinal.FinalDb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.Utils;
import wai.findwork.model.Config;
import wai.findwork.model.UserInfo;

/**
 * Created by Finder丶畅畅 on 2017/1/14 21:30
 * QQ群481606175
 */

public class LoginActivity extends BaseActivity {
    public static LoginActivity mInstail;
    @Bind(R.id.tel_et)
    EditText telEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.reg_btn)
    Button regBtn;
    FinalDb db;

    @Override
    public void initViews() {
        mInstail = this;
        db = FinalDb.create(this);
        regBtn.setOnClickListener(v -> Utils.IntentPost(RegPersonActivity.class));
        loginBtn.setOnClickListener(v -> {
            String tel = telEt.getText().toString().trim();
            String pwd = pwdEt.getText().toString().trim();
            if (TextUtils.isEmpty(tel)) {
                ToastShort("请输入手机号码");
            } else if (!Utils.isMobileNo(tel)) {
                ToastShort("请输入正确的手机号码");
            } else if (TextUtils.isEmpty(pwd)) {
                ToastShort("请输入密码");
            } else {

                UserInfo userInfo=new UserInfo();
                userInfo.setPassword(pwd);
                userInfo.setUsername(tel);

                userInfo.login(new SaveListener<UserInfo>() {
                    @Override
                    public void done(UserInfo o, BmobException e) {
                        if (e == null) {
                            db.deleteAll(UserInfo.class);

                            Map<String,String> map=new HashMap<String, String>();
                            map.put(Config.KEY_User_ID,tel);

                            map.put(Config.KEY_PassWord,pwd);
                            Utils.putCache(map);

                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                            query.addWhereEqualTo("username", tel);
                            query.addWhereEqualTo("password", pwd);
                            query.include("type");
                            query.findObjects(new FindListener<UserInfo>() {
                                @Override
                                public void done(List<UserInfo> list, BmobException e) {
                                    if(e==null) {
                                        UserInfo info = list.get(0);
                                        info.setTypeName(info.getType().getName());
                                        Map<String,String> map=new HashMap<String, String>();
                                        map.put(Config.KEY_Type_ID,info.getType().getObjectId());
                                        map.put(Config.KEY_TYPE_STATE,info.getType().getType());
                                        map.put(Config.KEY_ID, info.getObjectId());
                                        Utils.putCache(map);

                                        db.save(info);
                                        Utils.IntentPost(MainActivity.class);
                                        finish();
                                    }else{
                                        ToastShort("用户信息加载失败");
                                    }
                                }
                            });

                        } else {
                            ToastShort("用户名或密码错误请重新输入");
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
        return R.layout.ac_login;
    }
}
