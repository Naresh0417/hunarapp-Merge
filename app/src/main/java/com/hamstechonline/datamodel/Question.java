package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("faq_title")
    @Expose
    private String faqTitle;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFaqTitle() {
        return faqTitle;
    }

    public void setFaqTitle(String faqTitle) {
        this.faqTitle = faqTitle;
    }
}
