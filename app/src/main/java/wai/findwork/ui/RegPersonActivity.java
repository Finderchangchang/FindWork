package wai.findwork.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.model.UserInfo;

/**
 * Created by Administrator on 2017/1/17.
 */

public class RegPersonActivity extends BaseActivity {
    @Bind(R.id.rp_iv_header)
    ImageView ivHeader;
    @Bind(R.id.reg_person_toolbar)
    TitleBar titleBar;
    //真实姓名
    @Bind(R.id.person_real_name)
    EditText person_real_name;
    //身份证号
    @Bind(R.id.person_cardnum)
    EditText person_cardnum;
    @Bind(R.id.person_et_state)
    EditText person_et_state;
    @Bind(R.id.person_et_type)
    EditText person_et_type;
    @Bind(R.id.person_et_gongzi)
    EditText person_et_gongzi;
    @Bind(R.id.person_et_remark)
    EditText person_et_remark;
    @Bind(R.id.person_btn_save)
    Button person_btn_save;
    @Bind(R.id.person_rg_sex)
    RadioGroup person_rg_sex;
    @Bind(R.id.person_rb_nan)
    RadioButton person_rb_nan;
    @Bind(R.id.person_rb_nv)
    RadioButton person_rb_nv;
    UserInfo info;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        //接收传过来的model
        info = (UserInfo) getIntent().getSerializableExtra("user");
        if (info != null) {
            //给界面赋值
            person_cardnum.setText(info.getCardnum());
            person_et_gongzi.setText(info.getGongzi());
            person_et_remark.setText(info.getRemark());
            person_real_name.setText(info.getRealname());
            person_btn_save.setText("保存");
            if (info.isSex()) {
                person_rb_nv.setChecked(true);
                //person_rb_nan.setChecked(false);
            } else {
                //person_rb_nv.setChecked(false);
                person_rb_nan.setChecked(true);
            }
        }
    }
    //获取页面的值
private void getViewValue(){

}
    //验证页面的值
    private boolean YanView(){
        return true;
    }
    @Override
    public void initEvents() {
        person_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //读取页面上的值
                //修改或保存
                if (info.getObjectId() == null) {
                    //保存
                    info.save(new SaveListener() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {

                            } else {
                                ToastShort("保存失败");
                            }
                        }
                    });
                } else {
                    //修改
                    info.update(info.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {

                            } else {
                                ToastShort("修改失败");
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_reg_person;
    }
}
