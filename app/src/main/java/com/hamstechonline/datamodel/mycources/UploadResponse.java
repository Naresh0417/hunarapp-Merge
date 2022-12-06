package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    @SerializedName("status")
    @Expose
    private String status;

    String appname, apikey, phone, course_id, assignment;

    public UploadResponse (String appname,String apikey,String phone,String course_id,String assignment) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.course_id = course_id;
        this.assignment = assignment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
