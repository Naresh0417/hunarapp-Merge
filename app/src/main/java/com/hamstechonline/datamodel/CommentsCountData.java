package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class CommentsCountData {
    @SerializedName("status")
    private String status;
    @SerializedName("comments")
    private String comments;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    String appname;
    String apikey;
    int postid;
    String page;

    public CommentsCountData(String appname, String apikey, int postid, String page) {
        this.appname = appname;
        this.apikey = apikey;
        this.postid = postid;
        this.page = page;
    }
}
