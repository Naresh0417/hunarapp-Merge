package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckStudent {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("student")
    @Expose
    private String student;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
    String appname, apikey, phone;

    public CheckStudent (String appname, String apikey, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
    }

}
