package demo.test.user.musicplayer.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.service.PlayerService;

public abstract class BaseActivity extends AppCompatActivity {
    protected PlayerService.MyBinder myBinder;
    protected PlayerService playerService;

    public PlayerService getPlayerService() {
        return playerService;
    }

    public PlayerService.MyBinder getMyBinder() {
        return myBinder;
    }

    private boolean isBind = false;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (PlayerService.MyBinder) service;
            playerService = myBinder.getService();
            myBinder.setActivity(BaseActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1.绑定服务
        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(conn);
        }
    }

    private void bindService() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        isBind = true;
    }
}
