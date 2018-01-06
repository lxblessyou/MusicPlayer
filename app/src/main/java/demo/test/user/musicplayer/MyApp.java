package demo.test.user.musicplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 18/1/5.
 */

public class MyApp extends Application {
    public static SharedPreferences sp;
    private final String SP_NAME = "music_sp";
    public static final String KEY_MODE = "mode";
    public static final String KEY_INDEX = "index";

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
    }
}
