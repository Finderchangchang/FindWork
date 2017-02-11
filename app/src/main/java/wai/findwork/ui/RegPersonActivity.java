package wai.findwork.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import net.tsz.afinal.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.IDCardUtils;
import wai.findwork.method.Utils;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.UserInfo;
import wai.findwork.view.SpinnerDialog;

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
    TextView person_et_state;
    @Bind(R.id.person_et_type)
    TextView person_et_type;
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
    @Bind(R.id.person_et_psw2)EditText person_et_psw2;
    @Bind(R.id.person_et_psw1)EditText person_et_psw1;
    @Bind(R.id.person_et_phone)EditText person_et_phone;
    UserInfo info;
    private List<CodeModel> liststate;
    private SpinnerDialog spinnerDialog;
    //private List<CodeModel> listType;
    private String typeString = "1";

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        //接收传过来的model
        info = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        //
        if (info != null) {
            //给界面赋值
            person_cardnum.setText(info.getCardnum());
            person_et_gongzi.setText(info.getGongzi());
            person_et_remark.setText(info.getRemark());
            person_real_name.setText(info.getRealname());
            person_et_phone.setText(Utils.getCache(Config.KEY_User_ID));
            person_et_type.setText(info.getTypeName());
            String psw=Utils.getCache(Config.KEY_PassWord);
            person_et_psw2.setText(psw);
            person_et_psw1.setText(psw);
            person_btn_save.setText("保存");
            if (info.isSex()) {
                person_rb_nv.setChecked(true);
                //person_rb_nan.setChecked(false);
            } else {
                //person_rb_nv.setChecked(false);
                person_rb_nan.setChecked(true);
            }
        }else{
            info=new UserInfo();
        }
        loadState();
    }

    //加载选择列表框
    private void loadDialog(List<CodeModel> list, boolean isTrue) {
        if (list.size() > 0) {
            spinnerDialog=new SpinnerDialog(RegPersonActivity.this);
            spinnerDialog.setListView(list);
            spinnerDialog.show();
            spinnerDialog.setOnItemClick(new SpinnerDialog.OnItemClick() {
                @Override
                public void onClick(int position, CodeModel val) {
                    if (isTrue) {
                        //状态
                        person_et_state.setText(list.get(position).getName());
                        typeString = list.get(position).getType();
                    } else {
                        //类型
                        person_et_type.setText(list.get(position).getName());
                        info.setType(list.get(position));
                    }
                }
            });
        }
    }

    //加载当前状态
    private void loadState() {
        liststate = new ArrayList<>();
        CodeModel code = new CodeModel();
        code.setName("找工作");
        code.setType("1");
        liststate.add(code);
        //code=null;
        CodeModel code1 = new CodeModel();
         code1.setName("有班组");
        code1.setType("2");
        liststate.add(code1);
        //code1=null;
        CodeModel code2 = new CodeModel();
        code2.setName("有项目");
        code2.setType("3");

        liststate.add(code2);
        person_et_state.setText(liststate.get(0).getName());

        //code2=null;
    }

    //获取页面的值
    private void getViewValue() {
        info.setCardnum(person_cardnum.getText().toString().trim());
        info.setGongzi(person_et_gongzi.getText().toString().trim());
        info.setRealname(person_real_name.getText().toString().trim());
        info.setRemark(person_et_remark.getText().toString().trim());
        info.setUsername(person_et_phone.getText().toString().trim());
        info.setPassword(person_et_psw1.getText().toString().trim());

        if (person_rb_nan.isChecked()) {
            info.setSex(false);
        } else {
            info.setSex(true);
        }
    }

    //验证页面的值
    private boolean YanView() {
        if (person_real_name.getText().toString().trim().equals("")) {
            ToastShort("请填写您的真实姓名");
            return false;
        } else if (person_cardnum.getText().toString().trim().equals("")) {
            ToastShort("请填写身份证号码");
            return false;
        }else if(!IDCardUtils.IDCardValidate(person_cardnum.getText().toString().trim())){
            ToastShort("请检查身份证号");
            return false;
        } else if(person_et_type.getText().toString().trim().equals("")){
            ToastShort("请选择工种类型");
            return false;
        }else if (person_et_gongzi.getText().toString().trim().equals("")) {
            ToastShort("请填写您现在的工资/每天");
            return false;
        }else if(person_et_phone.getText().toString().trim().equals("")){
            ToastShort("请输入您的联系方式");
            return false;
        }else if(person_et_psw1.getText().toString().trim().equals("")){
            ToastShort("请输入登录密码");
            return false;
        }else if(person_et_psw2.getText().toString().trim().equals("")){
            ToastShort("请输入确认登录密码");
            return false;
        }else if(!(person_et_psw1.getText().toString().trim().equals(person_et_psw2.getText().toString().trim()))){
            ToastShort("输入密码不一致");
            return false;
        }
        return true;
    }

    @Override
    public void initEvents() {
        person_et_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog(liststate, true);
            }
        });
        person_et_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要查询对应类型
                searchType();
            }
        });
        person_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YanView()) {
                    //读取页面上的值
                    getViewValue();
                    //修改或保存
                    if (Utils.getCache(Config.KEY_ID)==null) {
                        //保存
                        info.signUp(new SaveListener<BmobUser>() {
                            @Override
                            public void done(BmobUser s, BmobException e) {
                                if (e == null) {
                                    finish();
                                } else {
                                    ToastShort(getResources().getString(R.string.no_wang));
                                }
                            }
                        });
                    } else {





                        //修改
                        info.update(Utils.getCache(Config.KEY_ID), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    ToastShort("保存成功");
                                    finish();
                                } else {
                                    ToastShort("修改失败");
                                }
                            }
                        });
                    }
                } else {

                }
            }
        });
    }

    //查询类型
    private void searchType() {
        BmobQuery<CodeModel> query = new BmobQuery<CodeModel>();
        query.addWhereEqualTo("Type", typeString);
        query.findObjects(new FindListener<CodeModel>() {
            @Override
            public void done(List<CodeModel> list, BmobException e) {
                if (e == null) {
                    //listType = list;
                    loadDialog(list,false);
                } else {
                    ToastShort("字典类型加载失败");
                }
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_reg_person;
    }
}
