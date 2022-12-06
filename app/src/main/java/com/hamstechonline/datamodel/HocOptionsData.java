package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HocOptionsData {

    @SerializedName("term_id")
    private Integer termId;
    @SerializedName("name")
    private String name;

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
