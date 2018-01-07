package demo.test.user.musicplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.adapter.MusicListAdapter;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.service.PlayerService;

public class LikeActivity extends BaseActivity {
private ListView lv_like;
    private MusicListAdapter mAdapter;
    private List<Mp3Info> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        //
        lv_like = (ListView) findViewById(R.id.lv_like);
        mList = DataSupport.findAll(Mp3Info.class);
        mAdapter = new MusicListAdapter(this,mList);
        lv_like.setAdapter(mAdapter);
        Toast.makeText(getApplicationContext(), "size:"+mList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateUI(int index) {
        // 播放列表数据集
//        playerService.setmLikeList(mList);
    }
}
