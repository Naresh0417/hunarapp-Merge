package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentsDataModel {
    @SerializedName("status")
    private String status;
    @SerializedName("comments")
    private List<CommentsData> commentsData;

    String appname;
    String apikey;
    int postid;
    String page;
    String phone;

    public CommentsDataModel(String appname, String apikey, int postid, String page, String phone){
        this.appname = appname;
        this.apikey = apikey;
        this.postid = postid;
        this.page = page;
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommentsData> getCommentsData() {
        return commentsData;
    }

    public void setCommentsData(List<CommentsData> commentsData) {
        this.commentsData = commentsData;
    }
}
