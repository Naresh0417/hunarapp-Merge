package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDynamicData {

    @SerializedName("status")
    private String status;
    @SerializedName("custom_menu")
    private List<DynamicMenuData> dynamicMenuData;

    String appname;
    String apikey;
    String lang;

    public GetDynamicData(String appname, String apikey,String lang) {
        this.apikey = apikey;
        this.appname = appname;
        this.lang = lang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DynamicMenuData> getDynamicMenuData() {
        return dynamicMenuData;
    }

    public void setDynamicMenuData(List<DynamicMenuData> dynamicMenuData) {
        this.dynamicMenuData = dynamicMenuData;
    }
}
