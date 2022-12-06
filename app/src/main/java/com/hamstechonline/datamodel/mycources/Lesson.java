package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Lesson implements Serializable {

    @SerializedName("lessonId")
    @Expose
    private String lessonId;
    @SerializedName("course_title")
    @Expose
    private String courseTitle;
    @SerializedName("lesson_title")
    @Expose
    private String lessonTitle;
    @SerializedName("lesson_description")
    @Expose
    private String lessonDescription;
    @SerializedName("lesson_image_url")
    @Expose
    private String lessonImageUrl;
    @SerializedName("lesson_page_url")
    @Expose
    private String lessonPageUrl;
    @SerializedName("lesson_page_text")
    @Expose
    private String lessonPageText;
    @SerializedName("video_duration")
    @Expose
    private String videoDuration;
    @SerializedName("lock_value")
    @Expose
    private String lockValue;
    @SerializedName("orderno")
    @Expose
    private String orderno;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("study_material_url")
    @Expose
    private String studyMaterialUrl;
    @SerializedName("video_thumbnail")
    @Expose
    private String video_thumbnail;
    @SerializedName("video_source")
    @Expose
    private String videoSource;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("external_link")
    @Expose
    private String externalLink;
    @SerializedName("watched_percentage")
    @Expose
    private String watchedPercentage;

    public String getAssignment_url() {
        return assignment_url;
    }

    public void setAssignment_url(String assignment_url) {
        this.assignment_url = assignment_url;
    }

    @SerializedName("assignment_url")
    @Expose
    private String assignment_url;


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

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getWatchedPercentage() {
        return watchedPercentage;
    }

    public void setWatchedPercentage(String watchedPercentage) {
        this.watchedPercentage = watchedPercentage;
    }
}
