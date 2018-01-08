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

public class LikeActivity extends BaseActivity {
    private ListView lv_like;
    private MusicListAdapter mAdapter;
    private List<Mp3Info> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        // 初始化列表
        lv_like = (ListView) findViewById(R.id.lv_like);
        mList = DataSupport.where("isLike > " + 0).find(Mp3Info.class);
        if (mList.size() > 0) {
            mAdapter = new MusicListAdapter(this, mList);
            lv_like.setAdapter(mAdapter);
        }
        lv_like.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 改变播放服务列表
                playerService.setmMusicList(mList);
                // 1.开始播放
                playerService.play(position);
                // 2.更新UI
//                updateUI(position);
                // 3.保存到数据库并指定播放时间
                Mp3Info mp3Info = playerService.getmMusicList().get(position);
                mp3Info.setPlayDate(System.currentTimeMillis());
                mp3Info.save();
            }
        });
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
