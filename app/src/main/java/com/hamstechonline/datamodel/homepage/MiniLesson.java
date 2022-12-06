package com.hamstechonline.datamodel.homepage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MiniLesson {

    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("course_title")
    @Expose
    private String courseTitle;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("course_description")
    @Expose
    private String courseDescription;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("enrolled")
    @Expose
    private String enrolled;
    @SerializedName("price_sticker")
    @Expose
    private String priceSticker;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(String enrolled) {
        this.enrolled = enrolled;
    }

    public String getPriceSticker() {
        return priceSticker;
    }

    public void setPriceSticker(String priceSticker) {
        this.priceSticker = priceSticker;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
