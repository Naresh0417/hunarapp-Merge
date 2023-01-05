package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveClassesResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("live_classes")
    @Expose
    private List<LiveClass> liveClasses = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LiveClass> getLiveClasses() {
        return liveClasses;
    }

    public void setLiveClasses(List<LiveClass> liveClasses) {
        this.liveClasses = liveClasses;
    }

    String appname, apikey, lang, phone, type;

    public LiveClassesResponse (String appname, String apikey, String lang, String phone, String type) {
        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
        this.phone = phone;
        this.type = type;
    }

}
