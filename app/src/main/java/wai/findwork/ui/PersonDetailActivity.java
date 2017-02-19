package wai.findwork.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.tsz.afinal.view.TitleBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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
    String index;
    int PLUGINVERSION = 7;

    @Override
    public void initViews() {
        info = (UserInfo) getIntent().getSerializableExtra("user");
        index = getIntent().getStringExtra("index");
        if (index == null) {
            index = info.getType().getType();
        }
        toolbar.setLeftClick(() -> finish());
    }

    UserInfo userInfo;
    UserInfo buyer;

    ProgressDialog dialog;

    @Override
    public void initEvents() {
        if (Utils.getCache(Config.KEY_ID).equals(info.getObjectId())) {
            telTv.setText("电话：" + Utils.getCache(Config.KEY_User_ID));
            getTelBtn.setVisibility(View.GONE);
        } else {
            BmobQuery<UserBuy> query = new BmobQuery<>();
            userInfo = new UserInfo();
            userInfo.setObjectId(info.getObjectId());
            query.addWhereEqualTo("user", userInfo);
            buyer = new UserInfo();
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
        Glide.with(this)
                .load(info.getIconurl()).transform(new GlideCircleTransform(this))
                .into(userIv);
        userNameTv.setText(info.getRealname());
        String num=info.getCardnum();
        if(TextUtils.isEmpty(num)) {
            idCardTv.setText(num.substring(0,num.length()-4)+"****");
        }
        if (("").equals(info.getTypeName()) || info.getTypeName() == null) {
            userTypeTv.setText(info.getType().getName());
        } else {
            userTypeTv.setText(info.getTypeName());
        }
        switch (index) {
            case "2":
                toolbar.setCenter_str("班组详情");
                gzTv.setText("日工资：" + info.getGongzi());
                remarkTv.setText("人员构成：" + info.getRemark());
                break;
            case "3":
                toolbar.setCenter_str("工程详情");
                gzTv.setText("所需班组：" + info.getGongzi());
                remarkTv.setText("工程概况：" + info.getRemark());
                break;
            default:
                gzTv.setText("工资：" + info.getGongzi());
                remarkTv.setText("备注：" + info.getRemark());
                break;
        }

        getTelBtn.setOnClickListener(view -> {
            if (("拨打电话").equals(getTelBtn.getText().toString())) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + info.getUsername());
                intent.setData(data);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请选择支付方式");
                builder.setItems(new String[]{"支付宝", "微信"}, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            pay(true);
                            break;
                        default:
                            pay(false);
                            break;
                    }

                });
                builder.show();
            }
        });
    }

    @Override
    public int setLayout() {
        return R.layout.ac_person_detail;
    }

    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return
     */
    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                startActivity(intent);
            } catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    startActivity(intent);
                } catch (Exception eee) {
                    Toast.makeText(PersonDetailActivity.this,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    private static final int REQUESTPERMISSION = 101;

    private void installApk(String s) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installBmobPayPlugin("bp.db");
                } else {
                    //提示没有权限，安装不了
                    Toast.makeText(PersonDetailActivity.this, "您拒绝了权限，这样无法安装支付插件", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 调用支付
     *
     * @param alipayOrWechatPay 支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {
        if (alipayOrWechatPay) {
            if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                    "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
                Toast.makeText(PersonDetailActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        } else {
            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
                // 有微信客户端，看看有无微信支付插件
                int pluginVersion = BP.getPluginVersion(this);
                if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件,
                    // 否则就是支付插件的版本低于官方最新版
                    Toast.makeText(
                            PersonDetailActivity.this,
                            pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                                    : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
                            Toast.LENGTH_SHORT).show();
//                    installBmobPayPlugin("bp.db");

                    installApk("bp.db");
                    return;
                }
            } else {// 没有安装微信
                Toast.makeText(PersonDetailActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        showDialog("正在获取订单...");
        final String name = getName();

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BP.pay(name, getBody(), getPrice(), alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(PersonDetailActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
//                tv.append(name + "'s pay status is unknow\n\n");
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(PersonDetailActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                UserBuy buy = new UserBuy();
                buy.setUser(userInfo);
                buy.setBuyer(buyer);
                buy.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            finish();
                        } else {
                            ToastShort("购买失败，请联系工作人员");
                        }
                    }
                });
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
//                order.setText(orderId);
//                tv.append(name + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            PersonDetailActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
//                    installBmobPayPlugin("bp.db");
                    installApk("bp.db");
                } else {
                    Toast.makeText(PersonDetailActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
//                tv.append(name + "'s pay status is fail, error code is \n"
//                        + code + " ,reason is " + reason + "\n\n");
                hideDialog();
            }
        });
    }

    // 执行订单查询
    void query() {
        showDialog("正在查询订单...");
        final String orderId = getOrder();

        BP.query(orderId, new QListener() {

            @Override
            public void succeed(String status) {
                Toast.makeText(PersonDetailActivity.this, "查询成功!该订单状态为 : " + status,
                        Toast.LENGTH_SHORT).show();
//                tv.append("pay status of" + orderId + " is " + status + "\n\n");
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PersonDetailActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
//                tv.append("query order fail, error code is " + code
//                        + " ,reason is \n" + reason + "\n\n");
                hideDialog();
            }
        });
    }

    // 默认为0.02
    double getPrice() {
        double price = 1;
        try {
//            price = Double.parseDouble(this.price.getText().toString());
        } catch (NumberFormatException e) {
        }
        return price;
    }

    // 商品详情(可不填)
    String getName() {
        return "";
    }

    // 商品详情(可不填)
    String getBody() {
        return "";
    }

    // 支付订单号(查询时必填)
    String getOrder() {
        return "";
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    /**
     * 安装assets里的apk文件
     *
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
