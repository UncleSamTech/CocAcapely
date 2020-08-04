package com.unclesamtech.cocacapely;

public class RhemaHiveYoutubeVideosModel {
    public String videoTitle;
    public String videoDate;
    public String videoThumbnail;
    public String videoId;
    public String videoLink;
    private String viewsCount;

    public RhemaHiveYoutubeVideosModel(String videoTitle, String videoDate, String videoThumbnail, String videoId, String videoLink, String viewsCount) {
        this.videoTitle = videoTitle;
        this.videoDate = videoDate;
        this.videoThumbnail = videoThumbnail;
        this.videoId = videoId;
        this.videoLink = videoLink;
        this.viewsCount = viewsCount;
    }

    public RhemaHiveYoutubeVideosModel(String videoTitle, String videoDate, String videoThumbnail, String videoId,String viewsCount) {
        this.videoTitle = videoTitle;
        this.videoDate = videoDate;
        this.videoThumbnail = videoThumbnail;
        this.videoId = videoId;
        this.viewsCount = viewsCount;
    }

    public RhemaHiveYoutubeVideosModel(String videoTitle, String videoThumbnail, String videoId) {
        this.videoTitle = videoTitle;
        this.videoThumbnail = videoThumbnail;
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(String viewsCount) {
        this.viewsCount = viewsCount;
    }
}
