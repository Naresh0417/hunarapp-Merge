package com.hamstechonline.activities.lesson;

public class LessonDetail {

    public String lessonId;
    public String course_title;
    public String lesson_title;
    public String lesson_description;
    public String lesson_image_url;
    public String video_duration;
    public String video_url;
    public String study_material_url;
    public String lesson_page_text;

    public String getLesson_page_text() {
        return lesson_page_text;
    }

    public void setLesson_page_text(String lesson_page_text) {
        this.lesson_page_text = lesson_page_text;
    }

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

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getStudy_material_url() {
        return study_material_url;
    }

    public void setStudy_material_url(String study_material_url) {
        this.study_material_url = study_material_url;
    }
}
