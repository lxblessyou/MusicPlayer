package demo.test.user.musicplayer.constants;

import android.content.IntentFilter;
import android.os.Environment;

import demo.test.user.musicplayer.MyApp;

/**
 * Created by user on 18/1/6.
 */

public interface ConstantsForSelf {
    String TAG = "tag";
    /**
     * Mp3Info 字段名
     */
    String MP3_SONG_ID="song_id";
    /**
     * SharedPreferences
     */
    String SP_NAME = "music_sp";
    String KEY_MODE = "mode";
    String KEY_INDEX = "index";
    /**
     * database 数据库
     */
    String DB_NAME = "music.db";
    String TABLE_NAME = "mp3info";
    String LIKE_PATH = Environment.getExternalStorageDirectory().getPath();
    /**
     * Intent和IntentFilter
     */
    String ACTION_UPDATE_UI_RECEIVER = "demo.test.user.musicplayer.updateui";
}
