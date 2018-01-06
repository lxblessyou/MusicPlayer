package demo.test.user.musicplayer.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.service.PlayerService;

public abstract class BaseActivity extends AppCompatActivity implements PlayerService.PlayerServiceCallback {
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
//            Log.i("tag", "onServiceConnected: "+playerService);
            myBinder.setActivity(BaseActivity.this);
//            playerService.setPlayerServiceCallback(BaseActivity.this);
            try {
                updateUI(playerService.getCurrentIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i("tag", "onCreate: ");
        // 1.绑定服务
        bindService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playerService != null) {
            updateUI(playerService.getCurrentIndex());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playerService!=null) {
            updateUI(playerService.getCurrentIndex());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBind) {
            unbindService(conn);
            isBind = false;
        }
    }

    private void bindService() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        isBind = true;
    }

    @Override
    public abstract void updateUI(int index);

    @Override
    public abstract void publishSeekBar(int progress);

    @Override
    public abstract void publishPlayTime(int progress) ;
}
