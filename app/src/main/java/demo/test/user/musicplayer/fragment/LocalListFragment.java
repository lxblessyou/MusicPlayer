package demo.test.user.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import demo.test.user.musicplayer.R;

/**
 * Created by user on 2018-01-02.
 */
public class LocalListFragment extends Fragment {
    /**
     * 获取实例
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        return view;
    }
}
