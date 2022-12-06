package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentSuccessResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("messsage")
    @Expose
    private String messsage;

    String appname,apikey,phone,order_id,razorpay_payment_id;

    public PaymentSuccessResponse (String appname,String apikey,String phone,String order_id,String razorpay_payment_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.order_id = order_id;
        this.razorpay_payment_id = razorpay_payment_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }
}
