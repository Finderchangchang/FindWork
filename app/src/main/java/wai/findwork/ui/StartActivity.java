package wai.findwork.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import net.tsz.afinal.FinalDb;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import wai.findwork.R;
import wai.findwork.method.Utils;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.UserInfo;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends AppCompatActivity {
    private View mContentView;
    FinalDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_start);
        new Handler().postDelayed(() -> {
            if (TextUtils.isEmpty(Utils.getCache(Config.KEY_User_ID))) {
                Utils.IntentPost(LoginActivity.class);
            } else {
                UserInfo userInfo = new UserInfo();
                userInfo.setPassword(Utils.getCache(Config.KEY_PassWord));
                userInfo.setUsername(Utils.getCache(Config.KEY_User_ID));
                userInfo.login(new SaveListener<UserInfo>() {
                    @Override
                    public void done(UserInfo o, BmobException e) {
                        if (e == null) {
                            Utils.IntentPost(MainActivity.class);
                        } else {
                            Utils.IntentPost(LoginActivity.class);
                        }
                    }
                });
            }
            finish();
        }, 0);
        db = FinalDb.create(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (db.findAll(CodeModel.class).size() > 0) {
//            db.deleteAll(CodeModel.class);
        } else {
            BmobQuery<CodeModel> query = new BmobQuery<>();
            query.findObjects(new FindListener<CodeModel>() {
                @Override
                public void done(List<CodeModel> list, BmobException e) {
                    if (e == null) {
                        for (CodeModel model : list) {
                            model.setObjectid(model.getObjectId());
                            db.save(model);
                        }
                    }
                }
            });
        }
    }
}
