package demo.test.user.musicplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import demo.test.user.musicplayer.bean.Mp3Info;

/**
 * Created by user on 2018-01-02.
 */
public class MediaUtil {
    public static List<Mp3Info> getMp3List(Context context) {
        List<Mp3Info> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
//        MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        Cursor query = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        Mp3Info mp3Info = null;
        while (query.moveToNext()) {
            mp3Info = new Mp3Info();
            long _id = query.getLong(query.getColumnIndex(MediaStore.Audio.Media._ID));// _id
            String title = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE));// 标题
            String artist = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
            String album = query.getString(query.getColumnIndex(MediaStore.Audio.Media.ALBUM));// 专辑封面
            long albumId = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));// 专辑封面 id
            long duration = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
            long size = query.getLong(query.getColumnIndex(MediaStore.Audio.Media.SIZE));// 文件大小
            String data = query.getString(query.getColumnIndex(MediaStore.Audio.Media.DATA));// 文件路径
            int isMusic = query.getInt(query.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
            if (isMusic != 0 && duration >= 160000) {
                mp3Info.set_id(_id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setAlbum(album);
                mp3Info.setAlbumId(albumId);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setData(data);
                mp3Info.setIsMusic(isMusic);
                list.add(mp3Info);
            }
        }
        query.close();
        return list;
    }

    public static String longToDate(long mill) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:SS");
        return dateFormat.format(mill);
    }
}
