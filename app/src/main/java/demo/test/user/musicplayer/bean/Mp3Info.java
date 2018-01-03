package demo.test.user.musicplayer.bean;

import android.graphics.Bitmap;

/**
 * Created by user on 2018-01-02.
 */
public class Mp3Info {
    /**
     * 数据库歌曲信息
     */
    private long _id;
    private String title;//歌名
    private String artist;//艺术家
    private String album;//专辑名称
    private long albumId;// 专辑封面 id
    private long duration;//时长
    private long size;//大小
    private String data;//路径
    private int isMusic;//是否为音乐
    /**
     * 标记字段
     */
    private long playTime;//最近播放时间
    private long mp3InfoId;//在收藏音乐时用于保存原始ID
    private int isLike; // 是否喜欢：1 喜欢  0 默认

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getMp3InfoId() {
        return mp3InfoId;
    }

    public void setMp3InfoId(long mp3InfoId) {
        this.mp3InfoId = mp3InfoId;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(int isMusic) {
        this.isMusic = isMusic;
    }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "_id=" + _id +
                ", mp3InfoId=" + mp3InfoId +
                ", playTime=" + playTime +
                ", isLike=" + isLike +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", isMusic=" + isMusic +
                '}';
    }
}
