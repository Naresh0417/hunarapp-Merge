package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownloadCertificate {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;

    String appname, apikey, phone, course_id;

    public DownloadCertificate (String appname,String apikey,String phone,String course_id) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
