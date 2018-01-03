package demo.test.user.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;

import demo.test.user.musicplayer.activity.BaseActivity;
import demo.test.user.musicplayer.activity.MainActivity;
import demo.test.user.musicplayer.activity.PlayerActivity;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.utils.MediaUtil;

public class PlayerService extends Service {
    public static MediaPlayer mediaPlayer;
    public static List<Mp3Info> localList;
    private int currentIndex;

    private MainActivity mainActivity;
    private PlayerActivity playerActivity;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 MediaPlayer
        mediaPlayer = new MediaPlayer();
        // TODO: 2018-01-03 设为前台服务
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取本地列表
        localList = MediaUtil.getMp3Infos(this);
        // MediaPlayer 监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
                if (mainActivity != null) {
                    mainActivity.updateBottomUI(currentIndex);
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

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
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录当前列表索引
        currentIndex = index;
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void prev() {
        if (currentIndex == 0) {
            play(localList.size() - 1);
        } else {
            play(--currentIndex);
        }
    }

    public void next() {
        if (currentIndex == localList.size() - 1) {
            play(0);
        } else {
            play(++currentIndex);
        }
    }

    public void continueToPlay() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void seekToService(int position) {
        mediaPlayer.seekTo(position);
    }

    /**
     * IBinder 类
     */
    public class MyBinder extends Binder {
        public void setActivity(BaseActivity baseActivity) {
            if (baseActivity instanceof MainActivity) {
                mainActivity = (MainActivity) baseActivity;
            } else if (baseActivity instanceof  PlayerActivity){
                playerActivity= (PlayerActivity) baseActivity;
            }
        }

        public PlayerService getService() {
            return PlayerService.this;
        }
    }
}