package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {
    @SerializedName("lang_name")
    @Expose
    private String langName;

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

}
