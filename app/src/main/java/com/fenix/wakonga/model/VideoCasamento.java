package com.fenix.wakonga.model;

public class VideoCasamento {
    private String title;
    private String video;


    public VideoCasamento() {

    }
    public VideoCasamento(String title, String video) {
        this.title = title;
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Videos";
    }
}
