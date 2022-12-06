package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Whyhamstech {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("whyhamstech")
    @Expose
    private String whyhamstech;
    @SerializedName("delbit")
    @Expose
    private String delbit;
    @SerializedName("data_lang")
    @Expose
    private String dataLang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhyhamstech() {
        return whyhamstech;
    }

    public void setWhyhamstech(String whyhamstech) {
        this.whyhamstech = whyhamstech;
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
