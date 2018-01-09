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
    /**
     * 网络
     */
    String URL_AUTHO = "http://music.baidu.com";;
    String URL_HOT_PATH = "/top/dayhot/?pst=shouyeTop";
    String URL_SEARCH_PATH = "/search?key=";
    String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";
}
