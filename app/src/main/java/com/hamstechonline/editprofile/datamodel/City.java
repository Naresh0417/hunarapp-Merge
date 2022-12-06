package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("cityid")
    @Expose
    private String cityid;
    @SerializedName("cityname")
    @Expose
    private String cityname;
    @SerializedName("delbit")
    @Expose
    private String delbit;
    @SerializedName("data_lang")
    @Expose
    private String dataLang;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getDelbit() {
        return delbit;
    }

    public void setDelbit(String delbit) {
        this.delbit = delbit;
    }

    public String getDataLang() {
        return dataLang;
    }

    public void setDataLang(String dataLang) {
        this.dataLang = dataLang;
    }
}
