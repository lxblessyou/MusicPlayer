package demo.test.user.musicplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.litepal.LitePal;
import org.xutils.x;

import demo.test.user.musicplayer.constants.ConstantsForSelf;

/**
 * Created by user on 18/1/5.
 */

public class MyApp extends Application {
    private static Context context;
    private static SharedPreferences sp;

    public static Context getContext() {
        return context;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);

        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        context = getApplicationContext();
        sp = getSharedPreferences(ConstantsForSelf.SP_NAME, MODE_PRIVATE);
    }
}
