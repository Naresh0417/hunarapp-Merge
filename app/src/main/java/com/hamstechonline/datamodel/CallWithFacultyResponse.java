package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallWithFacultyResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("messsage")
    @Expose
    private String messsage;

    String appname,apikey,phone,course_id;

    public CallWithFacultyResponse (String appname,String apikey,String phone,String course_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.course_id = course_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

}
