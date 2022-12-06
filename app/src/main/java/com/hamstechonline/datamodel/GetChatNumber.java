package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetChatNumber {

    String appname, apikey, phone;

    public GetChatNumber(String appname, String apikey, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("waba")
    @Expose
    private String waba;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaba() {
        return waba;
    }

    public void setWaba(String waba) {
        this.waba = waba;
    }
}
