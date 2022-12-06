package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayinstallmentRequest {

    @SerializedName("razorpay_order_id")
    @Expose
    private String razorpayOrderId;
    @SerializedName("amount")
    @Expose
    private Integer amount;

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    String order_id, course_id;

    public PayinstallmentRequest(String order_id,String course_id){
        this.order_id = order_id;
        this.course_id = course_id;
    }
}
