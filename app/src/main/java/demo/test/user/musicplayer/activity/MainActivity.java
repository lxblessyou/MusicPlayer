package demo.test.user.musicplayer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.fragment.LocalListFragment;
import demo.test.user.musicplayer.fragment.NetListFragment;

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
}
