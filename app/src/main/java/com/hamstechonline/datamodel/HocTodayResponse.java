package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HocTodayResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("list")
    private List<HocTodayData> hocTodayData;

    String appname;
    String apikey;
    String phone;
    String lang;
    String post_type;
    String term_id;

    public HocTodayResponse (String appname, String apikey, String phone, String lang, String post_type, String term_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.lang = lang;
        this.post_type = post_type;
        this.term_id = term_id;
    }

    public List<HocTodayData> getHocTodayData() {
        return hocTodayData;
    }

    public void setHocTodayData(List<HocTodayData> hocTodayData) {
        this.hocTodayData = hocTodayData;
    }
}
