package demo.test.user.musicplayer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.List;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.adapter.MusicListAdapter;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.service.PlayerService;

public class LatelyActivity extends BaseActivity {
    private ListView lv_lately;
    private MusicListAdapter mAdapter;
    private List<Mp3Info> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lately);
        // 初始化列表
        lv_lately = (ListView) findViewById(R.id.lv_lately);
        mList = DataSupport.where("playDate > 0").order("playDate desc").limit(5).find(Mp3Info.class);
        if (mList.size() > 0) {
            mAdapter = new MusicListAdapter(this, mList);
            lv_lately.setAdapter(mAdapter);
        }
//        lv_lately.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 1.开始播放
//                playerService.play(position);
//                // 2.更新UI
////                updateUI(position);
//                // 3.保存到数据库并指定播放时间
//                Mp3Info mp3Info = playerService.getmMusicList().get(position);
//                mp3Info.setPlayDate(System.currentTimeMillis());
//                mp3Info.save();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateUI(int index) {
        // 播放列表数据集
//        playerService.setmLikeList(mList);
    }
}
