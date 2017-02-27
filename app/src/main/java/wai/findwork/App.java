package wai.findwork;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import c.b.BP;
import cn.bmob.v3.Bmob;

/**
 * Created by Finder丶畅畅 on 2017/1/14 21:25
 * QQ群481606175
 */

public class App extends Application {
    private static Context context;
    private static String key = "15e70d22f52e3af14f325cbb8e66989e";
    //你的应用从官方网站申请到的合法appid
    private static String WeiXinKey = "";
    //第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, key);
        BP.init(key);
        CrashReport.initCrashReport(getApplicationContext(), "5d80bd2424", true);
        //通过工厂，获取实例
//        api = WXAPIFactory.createWXAPI(this, WeiXinKey, true);
//        //将应用的appid注册到微信
//        api.registerApp(WeiXinKey);
    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        App.context = context;
    }
}
