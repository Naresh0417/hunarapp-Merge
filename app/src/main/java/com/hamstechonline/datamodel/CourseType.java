package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseType {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mini_course")
    @Expose
    private String miniCourse;
    @SerializedName("my_course")
    @Expose
    private String my_course;

    String appname, apikey;
    int course_id;

    public CourseType(String appname,String apikey,int course_id) {
        this.appname = appname;
        this.apikey = apikey;
        this.course_id = course_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMiniCourse() {
        return miniCourse;
    }

    public void setMiniCourse(String miniCourse) {
        this.miniCourse = miniCourse;
    }

    public String getMy_course() {
        return my_course;
    }

    public void setMy_course(String my_course) {
        this.my_course = my_course;
    }
}
