package demo.test.user.musicplayer.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.service.PlayerService;
import demo.test.user.musicplayer.utils.MediaUtil;

import static android.content.ContentValues.TAG;

public class PlayerActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_current_progress;
    private TextView tv_duration;
    private SeekBar sb_progress;
    private ImageView iv_prev;
    private ImageView iv_play;
    private ImageView iv_next;
    private ImageView iv_album;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // 1.初始化控件
        initView();
        // 2.更新进度
        publishCurrentPosition();
    }

    private void initView() {
        iv_album = (ImageView) findViewById(R.id.iv_album);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_prev = (ImageView) findViewById(R.id.iv_prev);
        sb_progress = (SeekBar) findViewById(R.id.sb_progress);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_progress = (TextView) findViewById(R.id.tv_current_progress);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        iv_prev.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    publishPlayTime(progress);
                    publishSeekBar(progress);
                    PlayerService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void initUI(int index) {
//        Log.i("tag", "initUI: " + playerService);
        if (playerService != null) {
            Mp3Info mp3Info = PlayerService.localList.get(index);
            String title = mp3Info.getTitle();
            long duration = mp3Info.getDuration();
            int currentProgress = playerService.getCurrentProgress();
            tv_title.setText(title);
            tv_current_progress.setText(MediaUtil.millToSecond(currentProgress));
            tv_duration.setText(MediaUtil.millToSecond(duration));
            sb_progress.setMax((int) duration);
//            if (PlayerService.mediaPlayer.isPlaying()) {
//                iv_play.setImageResource(R.mipmap.pause2);
//            }
            setBottomPlayBtnState(PlayerService.mediaPlayer.isPlaying());
        }
    }

    @Override
    public void updateUI(int index) {
        Log.i("tag", "updateUI: " + playerService);
        if (playerService != null) {
            Mp3Info mp3Info = PlayerService.localList.get(index);
            String title = mp3Info.getTitle();
            long duration = mp3Info.getDuration();
            int currentProgress = playerService.getCurrentProgress();
            tv_title.setText(title);
            tv_current_progress.setText(MediaUtil.millToSecond(currentProgress));
            tv_duration.setText(MediaUtil.millToSecond(duration));
            if (PlayerService.mediaPlayer.isPlaying()) {
                iv_play.setImageResource(R.mipmap.pause2);
            }
        }
    }

    @Override
    public void publishSeekBar(int progress) {
        sb_progress.setProgress(progress);
    }

    @Override
    public void publishPlayTime(final int progress) {
//        tv_current_progress.setText(MediaUtil.millToSecond(progress));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_current_progress.setText(MediaUtil.millToSecond(progress));
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.i("tag", "onClick: " + v.getId());
        switch (v.getId()) {
            case R.id.iv_prev:
                playerService.prev();
                break;
            case R.id.iv_play:
                setBottomPlayBtnState(!PlayerService.mediaPlayer.isPlaying());
                if (PlayerService.mediaPlayer.isPlaying()) {
                    playerService.pause();
                    // 停止线程
//                    stopExecutor();
                } else if (!playerService.getIsFirst()) {
                    playerService.continueToPlay();
                    // 更新播放时长和拖动条
//                    publishCurrentPosition();
                } else {
                    playerService.play(0);
                    // 更新播放时长和拖动条
//                    publishCurrentPosition();
                }
                break;
            case R.id.iv_next:
                playerService.next();
                break;
            default:
                break;
        }
        // 更新UI
        updateUI(playerService.getCurrentIndex());
    }

    private void stopExecutor() {
//        executorService.shutdown();
    }

    private void publishCurrentPosition() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.i(TAG, "run: publish");
                    if (playerService.mediaPlayer.isPlaying()) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        publishPlayTime(playerService.mediaPlayer.getCurrentPosition());
                        publishSeekBar(playerService.mediaPlayer.getCurrentPosition());
                    }
                }
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
////                    Log.i(TAG, "run: publish");
//                    if (playerService.mediaPlayer.isPlaying()) {
//                        try {
//                            TimeUnit.MILLISECONDS.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        publishPlayTime(playerService.mediaPlayer.getCurrentPosition());
//                        publishSeekBar(playerService.mediaPlayer.getCurrentPosition());
//                    }
//                }
//            }
//        }).start();
    }

    private void setBottomPlayBtnState(boolean isPlaying) {
        if (isPlaying) {
            iv_play.setImageResource(R.mipmap.pause2);
        } else {
            iv_play.setImageResource(R.mipmap.play2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (executorService != null && !executorService.isShutdown()) {
            Log.i(TAG, "onStop: ");
            try {
            // 1.先发出停止线程的通知
            executorService.shutdownNow();
                if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    // 2.如果在发出通知超出指定时间是则立刻停止线程。任务执行结束返回 true
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                executorService.shutdownNow();
            }
        }
    }
}
