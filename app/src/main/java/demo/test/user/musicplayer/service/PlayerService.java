package demo.test.user.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.activity.BaseActivity;
import demo.test.user.musicplayer.activity.MainActivity;
import demo.test.user.musicplayer.activity.PlayerActivity;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.utils.MediaUtil;

import static android.content.ContentValues.TAG;

public class PlayerService extends Service {
    public static MediaPlayer mediaPlayer;
    public static List<Mp3Info> localList;
    private Random mRandom;
    private int currentIndex;
    private int currentProgress;
    private boolean isFirst = true;

    private MainActivity mainActivity;
    private PlayerActivity playerActivity;

//    private PlayerServiceCallback playerServiceCallback;

    public static final int ORDER_MODE = 0;
    public static final int RANDOM_MODE = 1;
    public static final int SINGLE_MODE = 2;
    private int orderMode = ORDER_MODE;

    private boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
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

//    public void setPlayerServiceCallback(PlayerServiceCallback playerServiceCallback) {
//        this.playerServiceCallback = playerServiceCallback;
//    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public PlayerService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        // 随机对象
        mRandom = new Random();
//        Log.i(TAG, "onCreate: ");
        // TODO: 2018-01-03 设为前台服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 1.获取本地列表
        localList = MediaUtil.getMp3Infos(this);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化SharedPreferences数据
     */
    private void initPreferences() {
        currentIndex = MyApp.sp.getInt(MyApp.KEY_INDEX, 0);
        orderMode = MyApp.sp.getInt(MyApp.KEY_MODE, ORDER_MODE);
    }

    /**
     * 播放
     * @param index 列表索引
     */
    public void play(final int index) {
//        Log.i("tag", "play: data " + localList.get(index).getData());
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(localList.get(index).getData());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
//                    Log.i(TAG, "onPrepared: "+Thread.currentThread().getName());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录当前列表索引
        currentIndex = index;
        // 改变是否首次播放的标记
        isFirst = false;
        SharedPreferences.Editor edit = MyApp.sp.edit();
        edit.putInt(MyApp.KEY_INDEX, currentIndex);
        edit.putInt(MyApp.KEY_MODE, orderMode);
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
                    currentIndex = localList.size() - 1;
                } else {
                    currentIndex++;
                }
                break;
            case RANDOM_MODE:
                // 随机
                currentIndex = mRandom.nextInt(localList.size());
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
                if (currentIndex == localList.size() - 1) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
                break;
            case RANDOM_MODE:
                // 随机
                currentIndex = mRandom.nextInt(localList.size());
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
     * @param position  目标进度
     */
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }


    /**
     * IBinder 类
     */
    public class MyBinder extends Binder {
        /**
         * 设置Activity对象
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
         * @return
         */
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    /**
     * 服务回调接口
     */
    public interface PlayerServiceCallback {
        void updateUI(int index);

        void publishSeekBar(int progress);

        void publishPlayTime(int progress);
    }
}
