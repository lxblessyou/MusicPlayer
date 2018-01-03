package demo.test.user.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
public class MusicListAdapter extends BaseAdapter{
    private Context context;
    private List<Mp3Info> list;
    private LayoutInflater inflater;

    public MusicListAdapter(Context context, List<Mp3Info> list) {
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
//        String album = mp3Info.getAlbum();
//        Log.i("tag", "getView: album:"+album+" albumId:"+albumId);
        long _id = mp3Info.get_id();
        String title = mp3Info.getTitle();
        String artist = mp3Info.getArtist();
        long albumId = mp3Info.getAlbumId();
        long duration = mp3Info.getDuration();
        // 显示歌曲名
        holder.tv_title.setText(title);
        // 初始化艺术家名称
        initArtist(holder, artist);
        // 显示专辑插图,卡
//        showArtwork(holder, _id, albumId);
        // 显示时长
        holder.tv_duration.setText(MediaUtil.millToSecond(duration));
        return convertView;
    }

    private void showArtwork(Holder holder, long _id, long albumId) {
        try {
            Bitmap artwork = MediaUtil.getArtwork(context, _id, albumId, true, true);
            holder.iv_album.setImageBitmap(artwork);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("tag", e.toString());
        }
    }

    private void initArtist(Holder holder, String artist) {
        if ("<unknown>".equals(artist)) {
            holder.tv_artist.setText("未知");
        } else {
            holder.tv_artist.setText(artist);
        }
    }

    class Holder {
        ImageView iv_album;
        TextView tv_title;
        TextView tv_artist;
        TextView tv_duration;
    }
}
