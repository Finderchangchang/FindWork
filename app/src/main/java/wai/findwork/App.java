package wai.findwork;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import c.b.BP;
import cn.bmob.v3.Bmob;
import rx.internal.util.PlatformDependent;

/**
 * Created by Finder丶畅畅 on 2017/1/14 21:25
 * QQ群481606175
 */

public class App extends Application {
    private static Context context;
    private static String key = "15e70d22f52e3af14f325cbb8e66989e";
    //你的应用从官方网站申请到的合法appid
    private static String WeiXinKey = "wx7d110e835daabcba";
    //第三方app和微信通信的openapi接口


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this, key);
        BP.init(key);

        UMShareAPI.get(this);
        CrashReport.initCrashReport(getApplicationContext(), "5d80bd2424", true);
        PlatformConfig.setWeixin(WeiXinKey, "5bb696d9ccd75a38c8a0bfe0675559b3");
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("1105918363", "ckQvjCEjfHCFpafa");

    }


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        App.context = context;
    }
}
