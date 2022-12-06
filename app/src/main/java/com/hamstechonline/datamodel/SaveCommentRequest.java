package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class SaveCommentRequest {

    @SerializedName("status")
    private String status;

    String appname;
    String apikey;
    int postid;
    String page;
    String comment;
    String phone;

    public SaveCommentRequest (String appname, String apikey, int postid, String page, String comment, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.postid = postid;
        this.page = page;
        this.comment = comment;
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
