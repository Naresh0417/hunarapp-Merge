package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveClass {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time_left")
    @Expose
    private String timeLeft;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("live_class_id")
    @Expose
    private Integer liveClassId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLiveClassId() {
        return liveClassId;
    }

    public void setLiveClassId(Integer liveClassId) {
        this.liveClassId = liveClassId;
    }
}
