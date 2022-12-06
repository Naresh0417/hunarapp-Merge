package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SavePostReport {

    String appname, apikey, report_type, phone;
    int post_id;

    public SavePostReport(String appname, String apikey, int post_id, String report_type, String phone) {
        this.appname = appname;
        this.apikey = apikey;
        this.post_id = post_id;
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
