package wai.findwork.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wai.findwork.BaseActivity;
import wai.findwork.R;
import wai.findwork.method.HttpUtil;
import wai.findwork.method.IDCardUtils;
import wai.findwork.method.Utils;
import wai.findwork.model.CodeModel;
import wai.findwork.model.Config;
import wai.findwork.model.URL;
import wai.findwork.model.UserInfo;
import wai.findwork.view.GlideCircleTransform;
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
    @Bind(R.id.person_et_psw2)
    EditText person_et_psw2;
    @Bind(R.id.person_et_psw1)
    EditText person_et_psw1;
    @Bind(R.id.person_et_phone)
    EditText person_et_phone;
    @Bind(R.id.qqorwx_et)
    EditText qqorwx_et;
    UserInfo info;
    @Bind(R.id.gz_tv)
    TextView gzTv;
    @Bind(R.id.remark_tv)
    TextView remarkTv;
    private List<CodeModel> liststate;
    private SpinnerDialog spinnerDialog;
    //private List<CodeModel> listType;
    private String typeString = "1";
    FinalDb db;
    ProgressDialog progressDialog;
    private String path = "";
    @Bind(R.id.img_remark_tv)
    TextView img_remark_tv;
    @Bind(R.id.tv_nowcity)
    TextView tv_nowcity;

    @Override
    public void initViews() {
        titleBar.setLeftClick(() -> finish());
        db = FinalDb.create(this);
        //接收传过来的model
        info = (UserInfo) getIntent().getSerializableExtra("UserInfo");
        if (info != null) {
            //给界面赋值
            titleBar.setCenter_str("编辑个人信息");
            person_cardnum.setText(info.getCardnum());
            person_et_gongzi.setText(info.getGongzi());
            person_et_remark.setText(info.getRemark());
            person_real_name.setText(info.getRealname());
            person_et_phone.setText(Utils.getCache(Config.KEY_User_ID));
            qqorwx_et.setText(info.getQq_wx());
            person_et_type.setText(info.getTypeName());
            if (info.getIconurl().equals("")) {
                ivHeader.setImageResource(R.mipmap.myheader);
            } else {
                Glide.with(this)
                        .load(info.getIconurl()).transform(new GlideCircleTransform(this))
                        .into(ivHeader);
            }
            String psw = Utils.getCache(Config.KEY_PassWord);
            path = info.getIconurl();
            person_et_psw2.setText(psw);
            person_et_psw1.setText(psw);
            person_btn_save.setText("保存");
            CodeModel codeModel = new CodeModel();
            codeModel.setType(Utils.getCache(Config.KEY_TYPE_STATE));
            codeModel.setObjectId(Utils.getCache(Config.KEY_Type_ID));
            codeModel.setName(info.getTypeName());
            info.setType(codeModel);
            info.setObjectId(Utils.getCache(Config.KEY_ID));
            if (info.getSex() != null && info.getSex()) {
                person_rb_nv.setChecked(true);
                //person_rb_nan.setChecked(false);
            } else {
                //person_rb_nv.setChecked(false);
                person_rb_nan.setChecked(true);
            }
        } else {
            info = new UserInfo();
        }
        loadState();
        ivHeader.setOnClickListener(view -> PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE));
    }

    private void initView(String position) {
        switch (position) {
            case "1":
                gzTv.setText("日工或包工：");
                remarkTv.setText("施工简历：");
                person_et_remark.setHint("勿填写任何联系方式，否则禁用！");
                person_et_type.setHint("请选择你是什么工种");
                person_et_gongzi.setHint("请填写日工资或包工费");
                img_remark_tv.setText("请添加本人照片");
                break;
            case "2":
                gzTv.setText("班组分包价格：");
                remarkTv.setText("班组简介：");
                person_et_type.setHint("请选择你有什么班组");
                person_et_gongzi.setHint("请填写分包价格");
                person_et_remark.setHint("勿填写任何联系方式，否则禁用！");
                img_remark_tv.setText("请添加本人照片");
                break;
            default:
                gzTv.setText("所需班组：");
                remarkTv.setText("工程概况：");
                person_et_type.setHint("请选择你是什么项目");
                person_et_gongzi.setHint("请填写所需工种或班组");
                person_et_remark.setHint("请介绍工程规模及施工内容");
                img_remark_tv.setText("请添加本人照片或项目照片");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                path = photos.get(0);
                ivHeader.setImageBitmap(Utils.getBitmapByFile(photos.get(0)));
            }
        }
    }

    //加载选择列表框
    private void loadDialog(List<CodeModel> list, boolean isTrue) {
        if (list.size() > 0) {
            if (spinnerDialog == null || (!spinnerDialog.isShowing())) {
                spinnerDialog = new SpinnerDialog(RegPersonActivity.this);
                spinnerDialog.setListView(list);
                spinnerDialog.setCanceledOnTouchOutside(false);
                spinnerDialog.show();
                spinnerDialog.setOnItemClick((position, val) -> {
                    if (isTrue) {
                        //状态
                        person_et_state.setText(val.getName());
                        typeString = val.getType();
                    } else {
                        //类型
                        person_et_type.setText(val.getName());
                        val.setObjectId(val.getOId());
                        info.setType(val);
                    }
                    initView((position + 1) + "");
                    spinnerDialog = null;
                });
            }
        }
    }

    //加载当前状态
    private void loadState() {
        liststate = new ArrayList<>();
        CodeModel code = new CodeModel();
        code.setName("找工作施工工种");
        code.setType("1");
        liststate.add(code);
        code = null;
        CodeModel code1 = new CodeModel();
        code1.setName("我是施工班组");
        code1.setType("2");
        liststate.add(code1);
        code1 = null;
        CodeModel code2 = new CodeModel();
        code2.setName("我有施工项目");
        code2.setType("3");
        liststate.add(code2);
        person_et_state.setText(liststate.get(0).getName());
        initView("1");
        code2 = null;
    }

    //保存头像
    private void sc() {
        if (!path.equals("")) {
            BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        path = bmobFile.getFileUrl();
                        //头像上传成功以后保存用户信息
                        SaveInfo();
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ToastShort(getResources().getString(R.string.neibu));
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        }
    }

    //获取页面的值
    private void getViewValue() {
        info.setCardnum(person_cardnum.getText().toString().trim());
        info.setGongzi(person_et_gongzi.getText().toString().trim());
        info.setRealname(person_real_name.getText().toString().trim());
        info.setRemark(person_et_remark.getText().toString().trim());
        info.setUsername(person_et_phone.getText().toString().trim());
        info.setPassword(person_et_psw1.getText().toString().trim());
        info.setNowcity(Utils.getCache(Config.KEY_CITY));
        info.setQq_wx(qqorwx_et.getText().toString().trim());
        if (person_rb_nan.isChecked()) {
            info.setSex(false);
        } else {
            info.setSex(true);
        }
    }

    //验证页面的值
    private boolean YanView() {
        if (tv_nowcity.getText().toString().trim().equals("定位失败")) {
            ToastShort("定位信息失败，请检查网络");
            return false;
        } else if (person_real_name.getText().toString().trim().equals("")) {
            ToastShort("请填写您的真实姓名");
            return false;
        } else if (person_et_type.getText().toString().trim().equals("")) {
            ToastShort((String) person_et_type.getHint());
            return false;
        } else if (person_et_gongzi.getText().toString().trim().equals("")) {
            ToastShort((String) person_et_gongzi.getHint());
            return false;
        } else if (person_et_phone.getText().toString().trim().equals("")) {
            ToastShort("请输入您的联系方式");
            return false;
        } else if (!Utils.isMobileNo(person_et_phone.getText().toString().trim())) {
            ToastShort("请检查您的联系方式");
            return false;
        } else if (person_et_psw1.getText().toString().trim().equals("")) {
            ToastShort("请输入登录密码");
            return false;
        } else if (person_et_psw2.getText().toString().trim().equals("")) {
            ToastShort("请输入确认登录密码");
            return false;
        } else if (!(person_et_psw1.getText().toString().trim().equals(person_et_psw2.getText().toString().trim()))) {
            ToastShort("输入密码不一致");
            return false;
        }
        return true;
    }

    @Override
    public void initEvents() {
        person_et_state.setOnClickListener(v -> {
            loadDialog(liststate, true);
            person_et_type.setText("");
//            info.setType(null);
        });
        person_et_type.setOnClickListener(v -> searchType());
        person_btn_save.setOnClickListener(v -> {
            if (YanView()) {
                //读取页面上的值
                progressDialog = new ProgressDialog(RegPersonActivity.this);
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("注册中...");
                progressDialog.show();
                if (info.getIconurl() == null &&(!path.equals(""))) {
                    sc();
                }else if(info.getIconurl()!=null&&(!path.equals(info.getIconurl()))){
                    sc();
                } else {
                    SaveInfo();
                }
            }
        });
        HttpUtil.load(URL.ip_address)
                .getIpAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ipAddress -> {
                    Utils.putCache(Config.KEY_CITY, ipAddress.getCity());
                    tv_nowcity.setText("当前城市：" + ipAddress.getCity());
                }, throwable -> {
                    tv_nowcity.setText("定位失败");
                });
    }

    private void SaveInfo() {
        getViewValue();
        //修改或保存
        if (info.getObjectId()==null) {
            info.setIconurl(path);
            //保存
            info.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser s, BmobException e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    if (e == null) {
                        finish();
                    } else {
                        ToastShort(getResources().getString(R.string.no_wang));
                    }
                }
            });
        } else {
            if ((!path.equals(info.getIconurl()) && (!path.equals("")) && (!info.getIconurl().equals("")))) {
                //删除以前的头像
                BmobFile file = new BmobFile();
                file.setUrl(info.getIconurl());//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
                file.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //修改
                            info.setIconurl(path);
                            info.update(Utils.getCache(Config.KEY_ID), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                    if (e == null) {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put(Config.KEY_User_ID, info.getUsername());
                                        map.put(Config.KEY_PassWord, person_et_psw1.getText().toString().trim());
                                        map.put(Config.KEY_Type_ID, info.getType().getObjectId());
                                        map.put(Config.KEY_TYPE_STATE, info.getType().getType());
                                        db.deleteAll(UserInfo.class);
                                        map.put(Config.KEY_ID, info.getObjectId());
                                        Utils.putCache(map);
                                        ToastShort("保存成功");
                                        info.setTypeName(info.getType().getName());
                                        db.save(info);
                                        setResult(101);
                                        finish();
                                    } else {
                                        ToastShort("修改失败");
                                    }
                                }
                            });
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            ToastShort("保存失败");
                        }
                    }
                });
            } else {
                //修改
                info.setIconurl(path);
                info.update(Utils.getCache(Config.KEY_ID), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        if (e == null) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(Config.KEY_User_ID, info.getUsername());
                            map.put(Config.KEY_PassWord, person_et_psw1.getText().toString().trim());
                            map.put(Config.KEY_Type_ID, info.getType().getObjectId());
                            map.put(Config.KEY_TYPE_STATE, info.getType().getType());
                            db.deleteAll(UserInfo.class);
                            map.put(Config.KEY_ID, info.getObjectId());
                            Utils.putCache(map);
                            info.setTypeName(info.getType().getName());
                            db.save(info);
                            ToastShort("保存成功");
                            setResult(101);
                            finish();
                        } else {
                            ToastShort("修改失败");
                        }
                    }
                });
            }
        }
    }

    //查询类型
    private void searchType() {
        List<CodeModel> list = new ArrayList<>();
        list = db.findAllByWhere(CodeModel.class, "Type='" + typeString + "'");
        loadDialog(list, false);
//        BmobQuery<CodeModel> query = new BmobQuery<CodeModel>();
//        query.addWhereEqualTo("Type", typeString);
//        query.findObjects(new FindListener<CodeModel>() {
//            @Override
//            public void done(List<CodeModel> list, BmobException e) {
//                if (e == null) {
//                    //listType = list;
//                    loadDialog(list, false);
//                } else {
//                    ToastShort("字典类型加载失败");
//                }
//            }
//        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_reg_person;
    }
}
