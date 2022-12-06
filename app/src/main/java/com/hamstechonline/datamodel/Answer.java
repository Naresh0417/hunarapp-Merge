package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Answer {

    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("faq_content")
    @Expose
    private String faqContent;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFaqContent() {
        return faqContent;
    }

    public void setFaqContent(String faqContent) {
        this.faqContent = faqContent;
    }
}
