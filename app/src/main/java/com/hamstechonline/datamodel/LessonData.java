package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class LessonData {

    @SerializedName("lessonId")
    private String lessonId;
    @SerializedName("course_title")
    private String courseTitle;
    @SerializedName("lesson_title")
    private String lessonTitle;
    @SerializedName("lesson_description")
    private String lessonDescription;
    @SerializedName("lesson_image_url")
    private String lessonImageUrl;
    @SerializedName("lesson_page_url")
    private String lessonPageUrl;
    @SerializedName("lesson_page_text")
    private String lessonPageText;
    @SerializedName("video_duration")
    private String videoDuration;
    @SerializedName("lock_value")
    private String lockValue;
    @SerializedName("orderno")
    private String orderno;
    @SerializedName("language")
    private String language;
    @SerializedName("video_url")
    private String videoUrl;
    @SerializedName("study_material_url")
    private String studyMaterialUrl;
    @SerializedName("external_link")
    private String externalLink;

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getLessonDescription() {
        return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
    }

    public String getLessonImageUrl() {
        return lessonImageUrl;
    }

    public void setLessonImageUrl(String lessonImageUrl) {
        this.lessonImageUrl = lessonImageUrl;
    }

    public String getLessonPageUrl() {
        return lessonPageUrl;
    }

    public void setLessonPageUrl(String lessonPageUrl) {
        this.lessonPageUrl = lessonPageUrl;
    }

    public String getLessonPageText() {
        return lessonPageText;
    }

    public void setLessonPageText(String lessonPageText) {
        this.lessonPageText = lessonPageText;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getLockValue() {
        return lockValue;
    }

    public void setLockValue(String lockValue) {
        this.lockValue = lockValue;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStudyMaterialUrl() {
        return studyMaterialUrl;
    }

    public void setStudyMaterialUrl(String studyMaterialUrl) {
        this.studyMaterialUrl = studyMaterialUrl;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
}
