package wai.findwork;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * Created by Finder丶畅畅 on 2017/1/14 21:25
 * QQ群481606175
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bmob.initialize(this,"15e70d22f52e3af14f325cbb8e66989e");
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        App.context = context;
    }
}
