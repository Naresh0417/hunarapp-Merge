package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class DynamicMenuData {

    @SerializedName("icon")
    private String icon;
    @SerializedName("page_type")
    private String page_type;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPage_type() {
        return page_type;
    }

    public void setPage_type(String page_type) {
        this.page_type = page_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
