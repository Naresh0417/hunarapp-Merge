package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrolNotification {
    @SerializedName("status")
    @Expose
    private String status;

    String appname,apikey,course_id,phone;

    public EnrolNotification(String appname,String apikey,String course_id,String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.course_id = course_id;
        this.phone = phone;
    }
}
