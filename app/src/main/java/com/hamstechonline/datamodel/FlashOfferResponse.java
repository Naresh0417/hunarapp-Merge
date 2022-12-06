package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class FlashOfferResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("english")
    private String english;

    @SerializedName("hindi")
    private String hindi;

    String appname;
    String apikey;

    public FlashOfferResponse (String appname, String apikey) {
        this.appname = appname;
        this.apikey = apikey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }
}
