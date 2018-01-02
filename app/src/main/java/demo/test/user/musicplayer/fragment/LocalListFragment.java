package demo.test.user.musicplayer.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import demo.test.user.musicplayer.MyApp;
import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.adapter.LocalListAdapter;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.utils.MediaUtil;

/**
 * Created by user on 2018-01-02.
 */
public class LocalListFragment extends Fragment {
    private Context context;

    private ListView lv_local;
    private LocalListAdapter adapter;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        // 1.
        lv_local = view.findViewById(R.id.lv_local);
        adapter = new LocalListAdapter(context, MyApp.localList);
        lv_local.setAdapter(adapter);
        return view;
    }

}
