package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveClassRegistrationResponse {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String appname, apikey, lang, phone;
    int live_class_id;

    public LiveClassRegistrationResponse (String appname, String apikey,String lang,String phone,int live_class_id) {

        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
        this.phone = phone;
        this.live_class_id = live_class_id;

    }

}
