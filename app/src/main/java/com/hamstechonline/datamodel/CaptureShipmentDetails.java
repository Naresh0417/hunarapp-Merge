package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaptureShipmentDetails {
    String appname, apikey, phone, country, state, city, pincode, address;

    public CaptureShipmentDetails (String appname, String apikey, String phone,
                                   String country,String state,String city, String pincode,
                                   String address) {

        this.appname = appname;
        this.apikey = apikey;
        this.phone = phone;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.address = address;

    }

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
