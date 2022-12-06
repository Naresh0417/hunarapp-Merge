package com.hamstechonline.editprofile.datamodel;

import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("page")
    private String page;
    @SerializedName("phone")
    private String phone;
    @SerializedName("prospectid")
    private String prospectid;
    @SerializedName("prospectname")
    private String prospectname;
    @SerializedName("profilepic")
    private String profilepic;
    @SerializedName("email")
    private String email;
    @SerializedName("city")
    private String city;
    @SerializedName("occupation")
    private String occupation;
    @SerializedName("ageband")
    private String ageband;
    @SerializedName("whyhamstech")
    private String whyhamstech;
    @SerializedName("address")
    private String address;
    @SerializedName("state")
    private String state;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("country")
    private String country;
    @SerializedName("gcm_id")
    private String gcmId;
    @SerializedName("is_student")
    private String is_student;
    @SerializedName("lang")
    private String lang;
    private String apikey;

    public UserData(String phone, String page, String apikey){
        this.phone = phone;
        this.page = page;
        this.apikey = apikey;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProspectid() {
        return prospectid;
    }

    public void setProspectid(String prospectid) {
        this.prospectid = prospectid;
    }

    public String getProspectname() {
        return prospectname;
    }

    public void setProspectname(String prospectname) {
        this.prospectname = prospectname;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAgeband() {
        return ageband;
    }

    public void setAgeband(String ageband) {
        this.ageband = ageband;
    }

    public String getWhyhamstech() {
        return whyhamstech;
    }

    public void setWhyhamstech(String whyhamstech) {
        this.whyhamstech = whyhamstech;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getIs_student() {
        return is_student;
    }

    public void setIs_student(String is_student) {
        this.is_student = is_student;
    }
}
