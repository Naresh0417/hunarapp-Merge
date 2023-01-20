package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HunarClubPostClick {

    String appname, apikey, page,phone;
    int postid;
    public HunarClubPostClick (String appname, String apikey,int postid,String phone,String page){
        this.appname = appname;
        this.apikey = apikey;
        this.postid = postid;
        this.phone = phone;
        this.page = page;
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
