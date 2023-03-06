package com.example.groove.data;

public class Main_Item {
    private String songName;
    private String artistName;
    private int albumImg;
    private int viewType;
    private String mvimg;

    public Main_Item(String songName, int viewType, String mvimg) {
        this.songName = songName;
        this.viewType = viewType;
        this.mvimg = mvimg;
    }

    public Main_Item(String songName, String artistName, int albumImg) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumImg = albumImg;
    }

    public Main_Item(String songName, String artistName, int albumImg, int viewType) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumImg = albumImg;
        this.viewType = viewType;
    }

    public Main_Item(String songName, int albumImg, int viewType) {
        this.songName = songName;
        this.albumImg = albumImg;
        this.viewType = viewType;
    }

    public Main_Item(String songName) {
        this.songName = songName;
    }

    public String getMvimg() {
        return mvimg;
    }

    public void setMvimg(String mvimg) {
        this.mvimg = mvimg;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(int albumImg) {
        this.albumImg = albumImg;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
