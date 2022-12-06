package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Testimonials {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("src")
    @Expose
    private String src;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}