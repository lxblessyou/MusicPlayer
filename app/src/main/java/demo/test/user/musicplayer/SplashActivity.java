package demo.test.user.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 定时器
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
              // 1秒后进入主界面
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        },1000);
    }
}
