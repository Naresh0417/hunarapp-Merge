package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscussionsModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("list")
    @Expose
    private List<Discussions> list = null;

    String appname,apikey,course_id,lang,phone;

    public DiscussionsModel (String appname,String apikey,String course_id,String lang,String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.course_id = course_id;
        this.lang = lang;
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Discussions> getList() {
        return list;
    }

    public void setList(List<Discussions> list) {
        this.list = list;
    }
}
