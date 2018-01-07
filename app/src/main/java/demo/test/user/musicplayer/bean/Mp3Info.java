package demo.test.user.musicplayer.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by user on 2018-01-02.
 */
public class Mp3Info extends DataSupport {

    /**
     * 数据库歌曲信息
     */
    private long id;
    private long song_id;//歌曲ID
    private String title;//歌名
    private String artist;//艺术家
    private String album;//专辑名称
    private long albumId;// 专辑封面 id
    private long duration;//时长
    private long size;//大小
    private String data;//路径
    /**
     * 标记字段
     */
    private int isMusic;//是否为音乐
    private long playTime;//最近播放时间
    private int isLike; // 是否喜欢：1 喜欢  0 默认

    public Mp3Info() {
    }

    public long getSong_id() {
        return song_id;
    }

    public void setSong_id(long song_id) {
        this.song_id = song_id;
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
                "song_id=" + song_id +
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Mp3Info)) {
            return false;
        }
        Mp3Info mp3Info = (Mp3Info) obj;
        if (this.getSong_id() == mp3Info.getSong_id()) {
            return true;
        }
        return false;
    }
}
