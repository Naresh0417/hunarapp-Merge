package com.hamstechonline.datamodel.homepage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCourse {

    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("course_title")
    @Expose
    private String courseTitle;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("watched_percentage")
    @Expose
    private Integer watchedPercentage;
    @SerializedName("days_left")
    @Expose
    private Integer daysLeft;
    @SerializedName("next_installment_date")
    @Expose
    private String nextInstallmentDate;
    @SerializedName("download_certificate")
    @Expose
    private String downloadCertificate;
    @SerializedName("days_left_icon")
    @Expose
    private String daysLeftIcon;
    @SerializedName("next_installment_icon")
    @Expose
    private String nextInstallmentIcon;
    @SerializedName("fast_learner_icon")
    @Expose
    private String fastLearnerIcon;
    @SerializedName("show_installment")
    @Expose
    private String showInstallment;
    @SerializedName("installment_amount")
    @Expose
    private String installmentAmount;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("email")
    @Expose
    private String email;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Integer getWatchedPercentage() {
        return watchedPercentage;
    }

    public void setWatchedPercentage(Integer watchedPercentage) {
        this.watchedPercentage = watchedPercentage;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }

    public String getNextInstallmentDate() {
        return nextInstallmentDate;
    }

    public void setNextInstallmentDate(String nextInstallmentDate) {
        this.nextInstallmentDate = nextInstallmentDate;
    }

    public String getDownloadCertificate() {
        return downloadCertificate;
    }

    public void setDownloadCertificate(String downloadCertificate) {
        this.downloadCertificate = downloadCertificate;
    }

    public String getDaysLeftIcon() {
        return daysLeftIcon;
    }

    public void setDaysLeftIcon(String daysLeftIcon) {
        this.daysLeftIcon = daysLeftIcon;
    }

    public String getNextInstallmentIcon() {
        return nextInstallmentIcon;
    }

    public void setNextInstallmentIcon(String nextInstallmentIcon) {
        this.nextInstallmentIcon = nextInstallmentIcon;
    }

    public String getFastLearnerIcon() {
        return fastLearnerIcon;
    }

    public void setFastLearnerIcon(String fastLearnerIcon) {
        this.fastLearnerIcon = fastLearnerIcon;
    }

    public String getShowInstallment() {
        return showInstallment;
    }

    public void setShowInstallment(String showInstallment) {
        this.showInstallment = showInstallment;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
