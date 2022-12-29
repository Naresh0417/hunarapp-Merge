package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimilarCourse {

    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
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
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("orderno")
    @Expose
    private String orderno;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("nsdc_status")
    @Expose
    private String nsdcStatus;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNsdcStatus() {
        return nsdcStatus;
    }

    public void setNsdcStatus(String nsdcStatus) {
        this.nsdcStatus = nsdcStatus;
    }


}
