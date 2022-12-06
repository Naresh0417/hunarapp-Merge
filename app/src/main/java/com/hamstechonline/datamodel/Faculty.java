package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Faculty {

    @SerializedName("facultyId")
    @Expose
    private String facultyId;
    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("faculty_name")
    @Expose
    private String facultyName;
    @SerializedName("faculty_description")
    @Expose
    private String facultyDescription;
    @SerializedName("faculty_image")
    @Expose
    private String facultyImage;

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getFacultyDescription() {
        return facultyDescription;
    }

    public void setFacultyDescription(String facultyDescription) {
        this.facultyDescription = facultyDescription;
    }

    public String getFacultyImage() {
        return facultyImage;
    }

    public void setFacultyImage(String facultyImage) {
        this.facultyImage = facultyImage;
    }
}
