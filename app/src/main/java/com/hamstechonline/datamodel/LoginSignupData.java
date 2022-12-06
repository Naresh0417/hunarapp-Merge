package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class LoginSignupData {
    @SerializedName("status")
    private String status;
    @SerializedName("image_1")
    private String image_1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    @SerializedName("image_2")
    private String image_2;

    String appname;
    String apikey;
    String lang;

    public LoginSignupData(String appname, String apikey, String lang){
        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
    }


}
