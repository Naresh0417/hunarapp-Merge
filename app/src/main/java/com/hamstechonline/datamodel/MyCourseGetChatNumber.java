package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCourseGetChatNumber {

    String appname, apikey, phone, course_id;

    public MyCourseGetChatNumber(String appname, String apikey, String phone, String course_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.course_id = course_id;
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
