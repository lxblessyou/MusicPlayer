package demo.test.user.musicplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.constants.ConstantsForSelf;
import demo.test.user.musicplayer.service.PlayerService;

public abstract class BaseActivity extends AppCompatActivity {
    protected PlayerService.MyBinder myBinder;
    protected PlayerService playerService;

    private boolean isBind = false;

    protected SharedPreferences mSP;

    private UpdateBroadcast updateUIReceiver = new UpdateBroadcast();;
    private android.content.IntentFilter filter = new IntentFilter(ConstantsForSelf.ACTION_UPDATE_UI_RECEIVER);

    public PlayerService getPlayerService() {
        return playerService;
    }

    public PlayerService.MyBinder getMyBinder() {
        return myBinder;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (PlayerService.MyBinder) service;
            myBinder.setActivity(BaseActivity.this);
            playerService = myBinder.getService();
            try {
                updateUI(playerService.getCurrentIndex());
            } catch (Exception e) {
                e.printStackTrace();
            }
            initSP();
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
        if (playerService!=null) {
            updateUI(playerService.getCurrentIndex());
        }
        // 注册更新UI广播
        registerReceiver(updateUIReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(updateUIReceiver);
    }

    private void initSP() {
        mSP = MyApp.getSp();
        playerService.setCurrentIndex(mSP.getInt(ConstantsForSelf.KEY_INDEX,0));
        playerService.setOrderMode(mSP.getInt(ConstantsForSelf.KEY_MODE,0));
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

    public abstract void updateUI(int index);

    class UpdateBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(playerService.getCurrentIndex());
        }
    }
}
