package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class LikesCountData {
    @SerializedName("status")
    private String status;
    @SerializedName("likes")
    private String likes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    String appname;
    String apikey;
    int postid;
    String page;

    public LikesCountData(String appname, String apikey, int postid, String page) {
        this.appname = appname;
        this.apikey = apikey;
        this.postid = postid;
        this.page = page;
    }
}
