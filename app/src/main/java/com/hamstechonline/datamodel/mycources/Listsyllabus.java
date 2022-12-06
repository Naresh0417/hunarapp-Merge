package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Listsyllabus {

    @SerializedName("syllabus")
    @Expose
    private String syllabus;

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
}
