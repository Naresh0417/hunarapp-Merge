package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseDetails {

    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("course_title")
    @Expose
    private String courseTitle;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("course_description")
    @Expose
    private String courseDescription;
    @SerializedName("course_language")
    @Expose
    private String courseLanguage;
    @SerializedName("order_id")
    @Expose
    private String order_id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("orderno")
    @Expose
    private String orderno;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage = courseLanguage;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
