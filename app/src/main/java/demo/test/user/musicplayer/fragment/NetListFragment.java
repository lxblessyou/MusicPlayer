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
public class NetListFragment extends Fragment {
    public static Fragment newInstance(int position) {
        NetListFragment net  = new NetListFragment();
        Bundle args = new Bundle();
        args.putInt("position",position);
        net.setArguments(args);
        return net;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net,container,false);
        return view;
    }
}
