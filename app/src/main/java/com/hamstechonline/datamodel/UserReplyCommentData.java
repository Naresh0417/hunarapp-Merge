package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserReplyCommentData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("replies")
    @Expose
    private List<Reply> replies = null;
    String appname, apikey, page, comment_id, phone;

    public UserReplyCommentData(String appname, String apikey, String page, String comment_id, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.page = page;
        this.comment_id = comment_id;
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
