package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadPostResponse {

    @SerializedName("status")
    @Expose
    private String status;

    String appname, apikey, post_title, post_content, phone, post_type, imagedata, lang;

    public UploadPostResponse(String appname, String apikey, String post_title, String post_content,
                              String phone, String post_type, String imagedata, String lang) {
        this.appname = appname;
        this.apikey = apikey;
        this.post_title = post_title;
        this.post_content = post_content;
        this.phone = phone;
        this.post_type = post_type;
        this.imagedata = imagedata;
        this.lang = lang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
