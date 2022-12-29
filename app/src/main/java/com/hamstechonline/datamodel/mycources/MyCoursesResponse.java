package com.hamstechonline.datamodel.mycources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hamstechonline.datamodel.CourseOverview;
import com.hamstechonline.datamodel.ExpertFaculty;
import com.hamstechonline.datamodel.LastLessonDetails;
import com.hamstechonline.datamodel.SimilarCourse;

import java.util.List;

public class MyCoursesResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("course_overview")
    @Expose
    private List<CourseOverview> courseOverview;
    @SerializedName("last_lesson_details")
    @Expose
    private LastLessonDetails lastLessonDetails;
    @SerializedName("lessons")
    @Expose
    private List<Lesson> lessons = null;
    @SerializedName("course_details")
    @Expose
    private CourseDetails courseDetails;
    @SerializedName("course_assignment")
    @Expose
    private String courseAssignment;
    @SerializedName("course_completed_percentage")
    @Expose
    private Integer courseCompletedPercentage;
    @SerializedName("download_certificate")
    @Expose
    private String downloadCertificate;
    @SerializedName("playing_icon")
    @Expose
    private String playingIcon;
    @SerializedName("play_icon")
    @Expose
    private String playIcon;
    @SerializedName("expert_faculty")
    @Expose
    private List<ExpertFaculty> expertFaculty = null;
    @SerializedName("know_how_image")
    @Expose
    private String knowHowImage;
    @SerializedName("similar_courses")
    @Expose
    private List<SimilarCourse> similarCourses = null;

    String appname, apikey, page, courseId, language, lang,phone,order_id;
    public MyCoursesResponse (String appname,String apikey,String page,String courseId,String language,String lang,
                              String phone, String order_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.page = page;
        this.courseId = courseId;
        this.language = language;
        this.lang = lang;
        this.phone = phone;
        this.order_id = order_id;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public CourseDetails getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(CourseDetails courseDetails) {
        this.courseDetails = courseDetails;
    }

    public String getCourseAssignment() {
        return courseAssignment;
    }

    public void setCourseAssignment(String courseAssignment) {
        this.courseAssignment = courseAssignment;
    }

    public Integer getCourseCompletedPercentage() {
        return courseCompletedPercentage;
    }

    public void setCourseCompletedPercentage(Integer courseCompletedPercentage) {
        this.courseCompletedPercentage = courseCompletedPercentage;
    }

    public String getDownloadCertificate() {
        return downloadCertificate;
    }

    public void setDownloadCertificate(String downloadCertificate) {
        this.downloadCertificate = downloadCertificate;
    }

    public String getPlayingIcon() {
        return playingIcon;
    }

    public void setPlayingIcon(String playingIcon) {
        this.playingIcon = playingIcon;
    }

    public String getPlayIcon() {
        return playIcon;
    }

    public void setPlayIcon(String playIcon) {
        this.playIcon = playIcon;
    }

    public List<CourseOverview> getCourseOverview() {
        return courseOverview;
    }

    public void setCourseOverview(List<CourseOverview> courseOverview) {
        this.courseOverview = courseOverview;
    }

    public String getKnowHowImage() {
        return knowHowImage;
    }

    public void setKnowHowImage(String knowHowImage) {
        this.knowHowImage = knowHowImage;
    }

    public LastLessonDetails getLastLessonDetails() {
        return lastLessonDetails;
    }

    public void setLastLessonDetails(LastLessonDetails lastLessonDetails) {
        this.lastLessonDetails = lastLessonDetails;
    }

    public List<ExpertFaculty> getExpertFaculty() {
        return expertFaculty;
    }

    public void setExpertFaculty(List<ExpertFaculty> expertFaculty) {
        this.expertFaculty = expertFaculty;
    }

    public List<SimilarCourse> getSimilarCourses() {
        return similarCourses;
    }

    public void setSimilarCourses(List<SimilarCourse> similarCourses) {
        this.similarCourses = similarCourses;
    }
}
