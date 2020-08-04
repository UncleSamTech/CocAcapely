package com.unclesamtech.cocacapely;

public class AcapelyMediaModel {
    public String playlist_title;
    public String playlist_id;
    public String playlist_poster;
    public String playlist_icon;
    public String user_uid;
    public String playlistTime;

    public String playlistType;

    public String getPlaylistType() {
        return playlistType;
    }

    public AcapelyMediaModel(String playlist_title, String playlist_id, String playlist_poster, String playlist_icon, String user_uid, String playlistTime, String playlistType) {
        this.playlist_title = playlist_title;
        this.playlist_id = playlist_id;
        this.playlist_poster = playlist_poster;
        this.playlist_icon = playlist_icon;
        this.user_uid = user_uid;
        this.playlistTime = playlistTime;
        this.playlistType = playlistType;
    }

    public AcapelyMediaModel(String playlist_title, String playlist_id, String playlist_poster, String playlist_icon, String playlistTime) {
        this.playlist_title = playlist_title;
        this.playlist_id = playlist_id;
        this.playlist_poster = playlist_poster;
        this.playlist_icon = playlist_icon;
        this.playlistTime = playlistTime;
    }

    public void setPlaylistType(String playlistType) {
        this.playlistType = playlistType;
    }

    public String getPlaylistTime() {
        return playlistTime;
    }

    public void setPlaylistTime(String playlistTime) {
        this.playlistTime = playlistTime;
    }





    public String getPlaylist_title() {
        return playlist_title;
    }

    public void setPlaylist_title(String playlist_title) {
        this.playlist_title = playlist_title;
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    public String getPlaylist_poster() {
        return playlist_poster;
    }

    public void setPlaylist_church(String playlist_poster) {
        this.playlist_poster = playlist_poster;
    }

    public String getPlaylist_icon() {
        return playlist_icon;
    }

    public void setPlaylist_icon(String playlist_icon) {
        this.playlist_icon = playlist_icon;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }
}
