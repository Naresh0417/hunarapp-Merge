package com.hamstechonline.datamodel;

import java.io.Serializable;

public class LessonsDataModel implements Serializable {

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public void setLesson_title(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public String getLesson_description() {
        return lesson_description;
    }

    public void setLesson_description(String lesson_description) {
        this.lesson_description = lesson_description;
    }

    public String getLesson_image_url() {
        return lesson_image_url;
    }

    public void setLesson_image_url(String lesson_image_url) {
        this.lesson_image_url = lesson_image_url;
    }

    public String getLesson_video_url() {
        return lesson_video_url;
    }

    public void setLesson_video_url(String lesson_video_url) {
        this.lesson_video_url = lesson_video_url;
    }

    public String getStudy_material_url() {
        return study_material_url;
    }

    public void setStudy_material_url(String study_material_url) {
        this.study_material_url = study_material_url;
    }

    public String lessonId;
    public String course_title;
    public String lesson_title;
    public String lessonImage;

    public String getLessonImage() {
        return lessonImage;
    }

    public void setLessonImage(String lessonImage) {
        this.lessonImage = lessonImage;
    }

    public String getTextImage() {
        return textImage;
    }

    public void setTextImage(String textImage) {
        this.textImage = textImage;
    }

    public String textImage;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String category_name;

    public String getLesson_name() {
        return lesson_name;
    }

    public void setLesson_name(String lesson_name) {
        this.lesson_name = lesson_name;
    }

    public String lesson_name;
    public String lesson_description;
    public String lesson_image_url;
    public String lesson_video_url;
    public String study_material_url;
    public String faculty_description;
    public String faculty_name;
    public String lesson_liked;

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String externalLink;

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String CourseId;

    public String getFaculty_description() {
        return faculty_description;
    }

    public void setFaculty_description(String faculty_description) {
        this.faculty_description = faculty_description;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    public String getFaculty_image() {
        return faculty_image;
    }

    public void setFaculty_image(String faculty_image) {
        this.faculty_image = faculty_image;
    }

    public String faculty_image;

    public int getLock_status() {
        return lock_status;
    }

    public void setLock_status(int lock_status) {
        this.lock_status = lock_status;
    }

    public int lock_status;

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String video_duration;

    public int getLikedislike() {
        return likedislike;
    }

    public void setLikedislike(int likedislike) {
        this.likedislike = likedislike;
    }

    public int likedislike;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLesson_liked() {
        return lesson_liked;
    }

    public void setLesson_liked(String lesson_liked) {
        this.lesson_liked = lesson_liked;
    }

    public String date;


}
