package demo.test.user.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.activity.BaseActivity;
import demo.test.user.musicplayer.activity.MainActivity;
import demo.test.user.musicplayer.activity.PlayerActivity;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.constants.ConstantsForSelf;
import demo.test.user.musicplayer.utils.MediaUtil;

public class PlayerService extends Service {
    public static MediaPlayer mediaPlayer;
    private List<Mp3Info> mMusicList;
    private Random mRandom;
    private int currentIndex;
    private int currentProgress;
    private boolean isFirst = true;

    private MainActivity mainActivity;
    private PlayerActivity playerActivity;

    public static final int ORDER_MODE = 0;
    public static final int RANDOM_MODE = 1;
    public static final int SINGLE_MODE = 2;
    private int orderMode = ORDER_MODE;

    private SharedPreferences mSp;

    private Intent mIntent;

    public List<Mp3Info> getmMusicList() {
        return mMusicList;
    }

    public void setmMusicList(List<Mp3Info> mMusicList) {
        this.mMusicList = mMusicList;
    }

    public int getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(int orderMode) {
        this.orderMode = orderMode;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 1.初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        // 2.获取本地列表
        mMusicList = MediaUtil.getMp3Infos(this);
        // 3.随机对象
        mRandom = new Random();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1.设为前台服务
        setForeground();
        // 2.初始化SharedPreferences数据
        initPreferences();
        // 3.MediaPlayer 播放监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
                if (mainActivity != null) {
                    mainActivity.updateUI(currentIndex);
                }
                if (playerActivity != null) {
                    playerActivity.updateUI(currentIndex);
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 设为前台服务
     */
    private void setForeground() {
        RemoteViews remoteView = new RemoteViews("demo.test.user.musicplayer", R.layout.layout_remote);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_logo2)
                .setTicker("音乐播放器")
                .setCustomContentView(remoteView);
        startForeground(1, builder.build());
    }

    /**
     * 初始化SharedPreferences数据
     */
    private void initPreferences() {
        if (mSp == null) {
            mSp = MyApp.getSp();
        }
        currentIndex = mSp.getInt(ConstantsForSelf.KEY_INDEX, 0);
        orderMode = mSp.getInt(ConstantsForSelf.KEY_MODE, ORDER_MODE);
    }

    /**
     * 播放
     *
     * @param index 列表索引
     */
    public void play(final int index) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mMusicList.get(index).getData());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    if (mIntent == null) {
                        mIntent = new Intent(ConstantsForSelf.ACTION_UPDATE_UI_RECEIVER);
                    }
                    if (mainActivity != null || playerActivity != null) {
                        sendBroadcast(mIntent);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录当前列表索引
        currentIndex = index;
        // 改变是否首次播放的标记
        isFirst = false;
        if (mSp == null) {
            mSp = MyApp.getSp();
        }
        SharedPreferences.Editor edit = mSp.edit();
        edit.putInt(ConstantsForSelf.KEY_INDEX, currentIndex);
        edit.putInt(ConstantsForSelf.KEY_MODE, orderMode);
        edit.commit();
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        // 判断播放模式,给当前列表索引赋值
        switch (orderMode) {
            case ORDER_MODE:
                // 有序
                if (currentIndex == 0) {
                    currentIndex = mMusicList.size() - 1;
                } else {
                    currentIndex++;
                }
                break;
            case RANDOM_MODE:
                // 随机
                currentIndex = mRandom.nextInt(mMusicList.size());
                break;
            case SINGLE_MODE:
                // 单曲(继续播放当前列表索引)
                break;
            default:
                break;
        }
        // 2.开始下一首的播放
        play(currentIndex);
    }

    /**
     * 下一首
     */
    public void next() {
        // 判断播放模式,给当前列表索引赋值
        switch (orderMode) {
            case ORDER_MODE:
                // 有序
                if (currentIndex == mMusicList.size() - 1) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                break;
            case RANDOM_MODE:
                // 随机
                currentIndex = mRandom.nextInt(mMusicList.size());
                break;
            case SINGLE_MODE:
                // 单曲(继续播放当前列表索引)
                break;
            default:
                break;
        }
        // 2.开始下一首的播放
        play(currentIndex);
    }

    /**
     * 继续播放
     */
    public void continueToPlay() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 拖动播放进度
     *
     * @param position 目标进度
     */
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }


    /**
     * IBinder 类
     */
    public class MyBinder extends Binder {
        /**
         * 设置Activity对象
         *
         * @param baseActivity
         */
        public void setActivity(BaseActivity baseActivity) {
            if (baseActivity instanceof MainActivity) {
                mainActivity = (MainActivity) baseActivity;
            } else if (baseActivity instanceof PlayerActivity) {
                playerActivity = (PlayerActivity) baseActivity;
            }
        }

        /**
         * 获取服务对象
         *
         * @return
         */
        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}
