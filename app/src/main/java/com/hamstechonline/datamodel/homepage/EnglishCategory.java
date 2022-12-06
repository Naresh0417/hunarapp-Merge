package com.hamstechonline.datamodel.homepage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnglishCategory {

    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("categoryname")
    @Expose
    private String categoryname;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("category_description")
    @Expose
    private String categoryDescription;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
