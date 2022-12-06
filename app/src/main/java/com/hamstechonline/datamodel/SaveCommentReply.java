package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCommentReply {
    @SerializedName("status")
    @Expose
    private String status;

    String appname,apikey,page,comment_id,comment,phone;

    public SaveCommentReply(String appname, String apikey, String page, String comment_id, String comment,String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.page = page;
        this.comment_id = comment_id;
        this.comment = comment;
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
