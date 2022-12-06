package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.SerializedName;

public class ProfileData {
    @SerializedName("data")
    private UserData data;

    String phone;
    String page;
    String apikey;

    public ProfileData(String phone, String page, String apikey){
        this.phone = phone;
        this.page = page;
        this.apikey = apikey;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
