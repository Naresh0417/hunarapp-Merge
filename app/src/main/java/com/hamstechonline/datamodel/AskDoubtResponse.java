package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AskDoubtResponse {

    String appname, apikey, page,phone,doubt,lang;
    int course_id;
    public AskDoubtResponse(String appname, String apikey, String page, String phone,
                            String doubt,int course_id,String lang){
        this.appname = appname;
        this.apikey = apikey;
        this.page = page;
        this.phone = phone;
        this.doubt = doubt;
        this.course_id = course_id;
        this.lang = lang;
    }
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
