package demo.test.user.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.test.user.musicplayer.R;
import demo.test.user.musicplayer.bean.Mp3Info;
import demo.test.user.musicplayer.utils.MediaUtil;

/**
 * Created by user on 2018-01-02.
 */
public class LocalListAdapter extends BaseAdapter{
    private Context context;
    private List<Mp3Info> list;
    private LayoutInflater inflater;

    public LocalListAdapter(Context context, List<Mp3Info> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Mp3Info getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mp3Info mp3Info = getItem(position);
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_local_music, parent, false);
            holder.iv_album = convertView.findViewById(R.id.iv_album);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_artist = convertView.findViewById(R.id.tv_artist);
            holder.tv_duration = convertView.findViewById(R.id.tv_duration);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String title = mp3Info.getTitle();
        String artist = mp3Info.getArtist();
        long duration = mp3Info.getDuration();
        holder.tv_title.setText(title);
        if ("<unknown>".equals(artist)) {
            holder.tv_artist.setText("未知");
        } else {
            holder.tv_artist.setText(artist);
        }
        holder.tv_duration.setText(MediaUtil.longToDate(duration));
        return convertView;
    }

    class Holder {
        ImageView iv_album;
        TextView tv_title;
        TextView tv_artist;
        TextView tv_duration;
    }
}
