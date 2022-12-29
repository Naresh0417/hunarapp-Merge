package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnBoardingRequest {

    String appname, apikey, lang;

    public OnBoardingRequest (String appname, String apikey, String lang) {
        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image")
    @Expose
    private String image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
