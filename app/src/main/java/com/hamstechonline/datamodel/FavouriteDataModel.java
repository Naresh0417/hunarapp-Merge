package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouriteDataModel {

    String appname, apikey, page, lang;

    public FavouriteDataModel (String appname, String apikey, String page, String lang) {
        this.appname = appname;
        this.apikey = apikey;
        this.page = page;
        this.lang = lang;
    }


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("topicslist")
    @Expose
    private List<Topics> topicslist = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Topics> getTopicslist() {
        return topicslist;
    }

    public void setTopicslist(List<Topics> topicslist) {
        this.topicslist = topicslist;
    }

}
