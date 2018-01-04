package demo.test.user.musicplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.fragment.LocalListFragment;
import demo.test.user.musicplayer.fragment.NetListFragment;
import demo.test.user.musicplayer.service.PlayerService;
import demo.test.user.musicplayer.utils.MediaUtil;

import static android.content.ContentValues.TAG;

public class MainActivity extends BaseActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager vp_main;
    private MyPagerAdapter adapter;

    private int currentColor = 0xFF3F9FE0;

    private Fragment localFragment;
    private Fragment netFragment;

    private LinearLayout ll_bottom_click;
    private CircleImageView civ_album;
    private TextView tv_title;
    private TextView tv_artist;
    private ImageView iv_pre;
    private ImageView iv_play;
    private ImageView iv_next;

    @Override
    protected void initUI(int index) {
        if (playerService != null) {
            Mp3Info mp3Info = PlayerService.localList.get(index);
            String title = mp3Info.getTitle();
            String artist = mp3Info.getArtist();
            tv_title.setText(title);
            tv_artist.setText(artist);
            setBottomPlayBtnState(PlayerService.mediaPlayer.isPlaying());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1.初始化 ViewPager
        initViewPager();
        // 2.初始化 PagerSlidingTabStrip
        initPagerSlidingTabStrip();
        // 3.初始化底部播放控件
        initBottomView();
    }


    @Override
    public void publishSeekBar(int progress) {

    }

    @Override
    public void publishPlayTime(int progress) {

    }

    private void initViewPager() {
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_main.setAdapter(adapter);
    }

    private void initPagerSlidingTabStrip() {
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setShouldExpand(true); // 设置tab可扩展，可实现标签均分宽度。Java代码设置必须放在其他属性设置前，否则无效
        tabs.setViewPager(vp_main); // 绑定 ViewPager
        tabs.setIndicatorColor(currentColor);   // 设置tab下方指示器颜色
//        tabs.setDividerColor(currentColor); // 设置tab之间分割器颜色
    }

    private void initBottomView() {
        ll_bottom_click = (LinearLayout) findViewById(R.id.ll_bottom_click);
        civ_album = (CircleImageView) findViewById(R.id.civ_album);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_artist = (TextView) findViewById(R.id.tv_artist);
        iv_pre = (ImageView) findViewById(R.id.iv_prev);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_next = (ImageView) findViewById(R.id.iv_next);
        // 底部可点击区域
        ll_bottom_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
        // 播放按钮点击事件
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPlayBtnState(!PlayerService.mediaPlayer.isPlaying());
                if (PlayerService.mediaPlayer.isPlaying()) {
                    playerService.pause();
                } else if (!playerService.getIsFirst()){
                    playerService.continueToPlay();
                }else {
                    playerService.play(0);
                }
            }
        });
        // 上一曲
        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPlayBtnState(true);
                playerService.prev();
                updateUI(playerService.getCurrentIndex());
            }
        });
        // 下一曲
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBottomPlayBtnState(true);
                playerService.next();
                updateUI(playerService.getCurrentIndex());
            }
        });
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"本地音乐", "网络曲库"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (localFragment == null) {
                        localFragment = LocalListFragment.newInstance(position);
                    }
                    return localFragment;
                case 1:
                    if (netFragment == null) {
                        netFragment = NetListFragment.newInstance(position);
                    }
                    return netFragment;
                default:
                    break;
            }
            return null;
        }

    }

    @Override
    public void updateUI(int index) {
        Mp3Info currentMp3Info = PlayerService.localList.get(index);
        setBottomPlayBtnState(true);
        setBottomTitle(currentMp3Info.getTitle());
        setBottomArtist(currentMp3Info.getArtist());
    }
    /**
     * @param title
     */
    public void setBottomTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * @param artist
     */
    public void setBottomArtist(String artist) {
        tv_artist.setText(artist);
    }
    public void setBottomArtwork(long _id, long albumId) {
        try {
//            Log.i("tag", _id+"--"+albumId);
            Bitmap artwork = MediaUtil.getArtwork(this, _id, albumId, true, true);
            if (artwork!=null) {
                civ_album.setImageBitmap(artwork);
            } else {
                civ_album.setImageResource(R.mipmap.music_album);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param isPlaying
     */
    public void setBottomPlayBtnState(boolean isPlaying) {
        if (isPlaying) {
            iv_play.setImageResource(R.mipmap.pause);
        } else {
            iv_play.setImageResource(R.mipmap.play);
        }
    }
}
