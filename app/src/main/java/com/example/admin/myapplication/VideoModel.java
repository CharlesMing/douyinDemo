package com.example.admin.myapplication;

public class VideoModel {
    private String mTitle;
    private String mVideoUrl;
    private String mVideoThumb;

    public VideoModel(String title, String videoUrl, String videoThumb) {
        mTitle = title;
        mVideoUrl = videoUrl;
        mVideoThumb = videoThumb;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getVideoThumb() {
        return mVideoThumb;
    }
}
