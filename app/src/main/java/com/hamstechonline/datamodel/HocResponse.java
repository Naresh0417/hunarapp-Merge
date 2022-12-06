package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HocResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("tags")
    private List<HocOptionsData> hocOptionsData;

    String appname;
    String apikey;
    String lang;
    String post_type;

    public HocResponse(String appname,String apikey, String lang, String post_type) {
        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
        this.post_type = post_type;
    }

    public List<HocOptionsData> getHocOptionsData() {
        return hocOptionsData;
    }

    public void setHocOptionsData(List<HocOptionsData> hocOptionsData) {
        this.hocOptionsData = hocOptionsData;
    }
}
