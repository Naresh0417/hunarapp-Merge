package com.hamstechonline.datamodel.favourite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouriteResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("categories")
    @Expose
    private List<FavouriteCourse> courses = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FavouriteCourse> getCourses() {
        return courses;
    }

    public void setCourses(List<FavouriteCourse> courses) {
        this.courses = courses;
    }

    String appname, apikey, phone, lang;
    public FavouriteResponse (String appname, String apikey, String phone, String lang) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.lang = lang;
    }
}
