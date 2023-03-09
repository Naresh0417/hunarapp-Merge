package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferralCallbackRequest {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String appname, apikey, mobile,lang;

    public ReferralCallbackRequest (String appname, String apikey, String mobile,String lang) {
        this.appname = appname;
        this.apikey = apikey;
        this.mobile = mobile;
        this.lang = lang;
    }

}