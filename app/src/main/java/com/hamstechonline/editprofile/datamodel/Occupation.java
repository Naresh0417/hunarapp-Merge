package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Occupation {

    @SerializedName("occupationid")
    @Expose
    private String occupationid;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("delbit")
    @Expose
    private String delbit;
    @SerializedName("data_lang")
    @Expose
    private String dataLang;

    public String getOccupationid() {
        return occupationid;
    }

    public void setOccupationid(String occupationid) {
        this.occupationid = occupationid;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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
