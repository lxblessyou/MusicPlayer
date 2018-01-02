package demo.test.user.musicplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.fragment.LocalListFragment;
import demo.test.user.musicplayer.fragment.NetListFragment;
import demo.test.user.musicplayer.utils.MediaUtil;

public class MainActivity extends AppCompatActivity {
    private PagerSlidingTabStrip tabs;
    private ViewPager vp_main;
    private MyPagerAdapter adapter;

    private int currentColor = 0xFF3F9FE0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1.初始化 ViewPager
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_main.setAdapter(adapter);
        // 2.初始化 PagerSlidingTabStrip
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setShouldExpand(true); // 设置tab可扩展，可实现标签均分宽度。Java代码设置必须放在其他属性设置前，否则无效
        tabs.setViewPager(vp_main); // 绑定 ViewPager
        tabs.setIndicatorColor(currentColor);   // 设置tab下方指示器颜色
//        tabs.setDividerColor(currentColor); // 设置tab之间分割器颜色
        // 3.初始化数据集合
        initList();
    }

    private void initList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            // 获取 Mp3Info 数据
            MyApp.localList = MediaUtil.getMp3List(this);
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "本地音乐","网络曲库"};

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
                    return LocalListFragment.newInstance(position);
                case 1:
                    return NetListFragment.newInstance(position);
                default:
                    break;
            }
            return null;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取 Mp3Info 数据
                    MyApp.localList = MediaUtil.getMp3List(this);
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
