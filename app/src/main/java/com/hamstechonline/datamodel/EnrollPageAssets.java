package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollPageAssets {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("offer")
    @Expose
    private String offer;
    @SerializedName("pay_now")
    @Expose
    private String payNow;
    @SerializedName("installment")
    @Expose
    private String installment;
    @SerializedName("image")
    @Expose
    private String image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPayNow() {
        return payNow;
    }

    public void setPayNow(String payNow) {
        this.payNow = payNow;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String appname, apikey, lang;

    public EnrollPageAssets(String appname, String apikey, String lang){
        this.appname = appname;
        this.apikey = apikey;
        this.lang = lang;
    }

}
