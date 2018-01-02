package demo.test.user.musicplayer;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import demo.test.user.musicplayer.bean.Mp3Info;

/**
 * Created by user on 2018-01-02.
 */

public class MyApp extends Application {
    public static List<Mp3Info> localList = null;
    @Override
    public void onCreate() {
        super.onCreate();
        localList = new ArrayList<>();
    }
}
