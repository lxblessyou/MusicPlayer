package demo.test.user.musicplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.service.PlayerService;
import demo.test.user.musicplayer.utils.MediaUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected void initUI(int index) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intent = new Intent(this, PlayerService.class);
            startService(intent);
            // 如果权限已注册则开启定时器任务
            startTimer();
        }

    }

    @Override
    public void updateUI(int index) {

    }

    @Override
    public void publishSeekBar(int progress) {

    }

    @Override
    public void publishPlayTime(int progress) {

    }

    private void startTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // 1秒后进入主界面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, PlayerService.class);
                    startService(intent);
                    // 如果权限已注册则开启定时器任务
                    startTimer();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
