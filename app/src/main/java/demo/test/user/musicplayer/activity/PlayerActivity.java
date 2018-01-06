package demo.test.user.musicplayer.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.service.PlayerService;
import demo.test.user.musicplayer.utils.MediaUtil;

public class PlayerActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private TextView tv_current_progress;
    private TextView tv_duration;
    private SeekBar sb_progress;
    private ImageView iv_order;
    private ImageView iv_star;
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
        publishCurrentProgress();
    }

    private void initView() {
        iv_album = (ImageView) findViewById(R.id.iv_album);
        iv_order = (ImageView) findViewById(R.id.iv_order);
        iv_star = (ImageView) findViewById(R.id.iv_star);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_prev = (ImageView) findViewById(R.id.iv_prev);
        sb_progress = (SeekBar) findViewById(R.id.sb_progress);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_current_progress = (TextView) findViewById(R.id.rl_controller);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        iv_prev.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        iv_order.setOnClickListener(this);
        iv_star.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    publishPlayTime(progress);
                    publishSeekBar(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playerService.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void updateUI(int index) {
//        Log.i("tag", "updateUI: " + playerService);
        if (playerService != null) {
            Mp3Info mp3Info = PlayerService.localList.get(index);
            String title = mp3Info.getTitle();
            long id = mp3Info.get_id();
            long albumId = mp3Info.getAlbumId();
            long duration = mp3Info.getDuration();
            int currentProgress = playerService.getCurrentProgress();
            tv_title.setText(title);
            tv_current_progress.setText(MediaUtil.millToSecond(currentProgress));
            tv_duration.setText(MediaUtil.millToSecond(duration));
            sb_progress.setMax((int) mp3Info.getDuration());
            setBottomPlayBtnState();
            iv_album.setImageBitmap(MediaUtil.getArtwork(this,id,albumId,true,false));
            switch (playerService.getOrderMode()) {
                case PlayerService.ORDER_MODE:
                    iv_order.setImageResource(R.mipmap.order);
                    break;
                case PlayerService.RANDOM_MODE:
                    iv_order.setImageResource(R.mipmap.random);
                    break;
                case PlayerService.SINGLE_MODE:
                    iv_order.setImageResource(R.mipmap.single);
                    break;
                default:
                    break;
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
//        Log.i("tag", "onClick: " + v.getId());
        switch (v.getId()) {
            case R.id.iv_order:
                // 修改排序
                updateOrder();
                break;
            case R.id.iv_star:
                // TODO: 18/1/5
                break;
            case R.id.iv_prev:
                playerService.prev();
                playerService.setPlaying(true);
                break;
            case R.id.iv_play:
                if (PlayerService.mediaPlayer.isPlaying()) {
                    playerService.pause();
                    playerService.setPlaying(false);
                    // 停止线程
//                    stopExecutor();
                } else if (!playerService.getIsFirst()) {
                    playerService.continueToPlay();
                    playerService.setPlaying(true);
                    // 更新播放时长和拖动条
//                    publishCurrentProgress();
                } else {
                    playerService.play(playerService.getCurrentIndex());
                    playerService.setPlaying(true);
                    // 更新播放时长和拖动条
//                    publishCurrentProgress();
                }
//                setPlayBtnState(PlayerService.mediaPlayer.isPlaying());
                break;
            case R.id.iv_next:
                playerService.next();
                playerService.setPlaying(true);
                break;
            default:
                break;
        }
        // 更新UI
        updateUI(playerService.getCurrentIndex());
    }

    /**
     * 修改排序
     */
    private void updateOrder() {
        // 1.判断当前播放模式并重新赋值
        switch (playerService.getOrderMode()) {
            case PlayerService.ORDER_MODE:
                iv_order.setImageResource(R.mipmap.random);
                playerService.setOrderMode(PlayerService.RANDOM_MODE);
                break;
            case PlayerService.RANDOM_MODE:
                iv_order.setImageResource(R.mipmap.single);
                playerService.setOrderMode(PlayerService.SINGLE_MODE);
                break;
            case PlayerService.SINGLE_MODE:
                iv_order.setImageResource(R.mipmap.order);
                playerService.setOrderMode(PlayerService.ORDER_MODE);
                break;
            default:
                break;
        }
        // 2.更新 SharedPreference
        SharedPreferences.Editor edit = MyApp.sp.edit();
        edit.putInt(MyApp.KEY_MODE, playerService.getOrderMode());
        edit.commit();
    }

    private void stopExecutor() {
//        executorService.shutdown();
    }

    private void publishCurrentProgress() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
//                    Log.i(TAG, "run: publish");
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
    }

    private void setBottomPlayBtnState() {
        if (playerService.isPlaying()) {
            iv_play.setImageResource(R.mipmap.pause2);
        } else {
            iv_play.setImageResource(R.mipmap.play2);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
            Log.i("tag", "onStop: ");
        if (executorService != null && !executorService.isShutdown()) {
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
            }finally {
                executorService = null;
            }
        }
    }
}
