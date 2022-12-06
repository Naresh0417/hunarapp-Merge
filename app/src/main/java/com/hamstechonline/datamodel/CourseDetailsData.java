package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseDetailsData {
    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_description")
    @Expose
    private String categoryDescription;
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
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("nsdc_amount_6_months")
    @Expose
    private String nsdcAmount6Months;
    @SerializedName("nsdc_title")
    @Expose
    private String nsdcTitle;
    @SerializedName("nsdc_desc")
    @Expose
    private String nsdcDesc;
    @SerializedName("nsdc_image")
    @Expose
    private String nsdcImage;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("nsdc_status")
    @Expose
    private String nsdcStatus;
    @SerializedName("instalment_amount")
    @Expose
    private String instalmentAmount;
    @SerializedName("second_instalment")
    @Expose
    private Integer secondInstalment;
    @SerializedName("course_overview")
    @Expose
    private String courseOverview;
    @SerializedName("course_syllabus")
    @Expose
    private String courseSyllabus;
    @SerializedName("course_starterkit")
    @Expose
    private String courseStarterkit;
    @SerializedName("course_starter_kit_img")
    @Expose
    private String courseStarterKitImg;
    @SerializedName("course_certified_by")
    @Expose
    private String courseCertifiedBy;
    @SerializedName("course_certified_by_img")
    @Expose
    private String courseCertifiedByImg;
    @SerializedName("course_live_webinars")
    @Expose
    private String courseLiveWebinars;
    @SerializedName("course_councelling")
    @Expose
    private String courseCouncelling;
    @SerializedName("course_career_options")
    @Expose
    private String courseCareerOptions;
    @SerializedName("course_workshops")
    @Expose
    private String courseWorkshops;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNsdcAmount6Months() {
        return nsdcAmount6Months;
    }

    public void setNsdcAmount6Months(String nsdcAmount6Months) {
        this.nsdcAmount6Months = nsdcAmount6Months;
    }

    public String getNsdcTitle() {
        return nsdcTitle;
    }

    public void setNsdcTitle(String nsdcTitle) {
        this.nsdcTitle = nsdcTitle;
    }

    public String getNsdcDesc() {
        return nsdcDesc;
    }

    public void setNsdcDesc(String nsdcDesc) {
        this.nsdcDesc = nsdcDesc;
    }

    public String getNsdcImage() {
        return nsdcImage;
    }

    public void setNsdcImage(String nsdcImage) {
        this.nsdcImage = nsdcImage;
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

    public String getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(String instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public Integer getSecondInstalment() {
        return secondInstalment;
    }

    public void setSecondInstalment(Integer secondInstalment) {
        this.secondInstalment = secondInstalment;
    }

    public String getCourseOverview() {
        return courseOverview;
    }

    public void setCourseOverview(String courseOverview) {
        this.courseOverview = courseOverview;
    }

    public String getCourseSyllabus() {
        return courseSyllabus;
    }

    public void setCourseSyllabus(String courseSyllabus) {
        this.courseSyllabus = courseSyllabus;
    }

    public String getCourseStarterkit() {
        return courseStarterkit;
    }

    public void setCourseStarterkit(String courseStarterkit) {
        this.courseStarterkit = courseStarterkit;
    }

    public String getCourseStarterKitImg() {
        return courseStarterKitImg;
    }

    public void setCourseStarterKitImg(String courseStarterKitImg) {
        this.courseStarterKitImg = courseStarterKitImg;
    }

    public String getCourseCertifiedBy() {
        return courseCertifiedBy;
    }

    public void setCourseCertifiedBy(String courseCertifiedBy) {
        this.courseCertifiedBy = courseCertifiedBy;
    }

    public String getCourseCertifiedByImg() {
        return courseCertifiedByImg;
    }

    public void setCourseCertifiedByImg(String courseCertifiedByImg) {
        this.courseCertifiedByImg = courseCertifiedByImg;
    }

    public String getCourseLiveWebinars() {
        return courseLiveWebinars;
    }

    public void setCourseLiveWebinars(String courseLiveWebinars) {
        this.courseLiveWebinars = courseLiveWebinars;
    }

    public String getCourseCouncelling() {
        return courseCouncelling;
    }

    public void setCourseCouncelling(String courseCouncelling) {
        this.courseCouncelling = courseCouncelling;
    }

    public String getCourseCareerOptions() {
        return courseCareerOptions;
    }

    public void setCourseCareerOptions(String courseCareerOptions) {
        this.courseCareerOptions = courseCareerOptions;
    }

    public String getCourseWorkshops() {
        return courseWorkshops;
    }

    public void setCourseWorkshops(String courseWorkshops) {
        this.courseWorkshops = courseWorkshops;
    }
}
