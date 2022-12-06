package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveCommentReport {

    String appname, apikey, report_type, phone;
    int comment_id;

    public SaveCommentReport(String appname, String apikey, int comment_id, String report_type, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.comment_id = comment_id;
        this.report_type = report_type;
        this.phone = phone;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("reports")
    @Expose
    private String reports;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

}
