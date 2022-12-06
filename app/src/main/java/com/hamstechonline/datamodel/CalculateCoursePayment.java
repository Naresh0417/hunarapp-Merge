package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalculateCoursePayment {

    String appname, apikey;
    private List<Integer> course_ids = null;

    public CalculateCoursePayment(String appname, String apikey,List<Integer> course_ids) {
        this.appname = appname;
        this.apikey = apikey;
        this.course_ids = course_ids;
    }

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("full_payment")
    @Expose
    private Integer fullPayment;
    @SerializedName("installment_amount")
    @Expose
    private Integer installmentAmount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFullPayment() {
        return fullPayment;
    }

    public void setFullPayment(Integer fullPayment) {
        this.fullPayment = fullPayment;
    }

    public Integer getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(Integer installmentAmount) {
        this.installmentAmount = installmentAmount;
    }
}
