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
import wai.findwork.model.UpdateManage;
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
        db = FinalDb.create(this);

        BmobQuery<UpdateManage> query = new BmobQuery<>();
        query.addWhereEqualTo("project", "1");
        query.findObjects(new FindListener<UpdateManage>() {
            @Override
            public void done(List<UpdateManage> list, BmobException e) {
                if (e == null && list != null) {
                    if (list.size() > 0) {
                        String time = Utils.getCache(Config.KEY_Code_Update_Time);
                        if (!TextUtils.isEmpty(time) || list.get(0).getUpdatedAt().equals(time)) {

                        } else {
                            if (db.findAll(CodeModel.class).size() > 0) {
                                db.deleteAll(CodeModel.class);
                            }
                            BmobQuery<CodeModel> query = new BmobQuery<>();
                            query.order("sorts");
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
            }
        });
        new Handler().postDelayed(() -> {
            Utils.IntentPost(LoginActivity.class);
            finish();
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
