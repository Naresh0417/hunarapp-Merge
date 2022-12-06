package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionUpload {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    String apikey, phone;
    int apk_version;

    public VersionUpload (String apikey, String phone, int apk_version) {
        this.apikey = apikey;
        this.phone = phone;
        this.apk_version = apk_version;
    }

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
}
