package demo.test.user.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.activity.MainActivity;
import demo.test.user.musicplayer.adapter.MusicListAdapter;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.service.PlayerService;
import demo.test.user.musicplayer.utils.MediaUtil;

/**
 * Created by user on 2018-01-02.
 */
public class LocalListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private Context context;
    private MainActivity activity;
    private PlayerService mPlayService;

    private ListView lv_local;
    private MusicListAdapter adapter;
    private List<Mp3Info> mList;

    /**
     * 获取实例
     *
     * @param position
     * @return
     */
    public static Fragment newInstance(int position) {
        LocalListFragment localListFragment = new LocalListFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        localListFragment.setArguments(args);
        return localListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mList = MediaUtil.getMp3Infos(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        // 1.初始化 ListView
        lv_local = view.findViewById(R.id.lv_local);
        adapter = new MusicListAdapter(context, mList);
        lv_local.setAdapter(adapter);
        lv_local.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 播放服务中的列表
        if (mPlayService == null) {
            mPlayService = activity.getPlayerService();
        }
        mPlayService.setmMusicList(mList);
        // 1.开始播放
        activity.getPlayerService().play(position);
        // 2.更新UI
        activity.updateUI(position);
        // 3.保存到数据库并指定播放时间
        Mp3Info mp3Info = mList.get(position);
        mp3Info.setPlayDate(System.currentTimeMillis());
        mp3Info.save();
//        Log.i("tag", "onItemClick: position:" + position + " size:" + PlayerService.mMusicList.size());
    }
}
