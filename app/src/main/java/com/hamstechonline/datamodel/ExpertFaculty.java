package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpertFaculty {
    @SerializedName("name")
    @Expose
    private String facultyName;
    @SerializedName("description")
    @Expose
    private String facultyDescription;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("image")
    @Expose
    private String facultyImage;

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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}