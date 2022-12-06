package com.hamstechonline.datamodel.homepage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessStory {

    @SerializedName("postid")
    @Expose
    private Integer postid;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("videourl")
    @Expose
    private String videourl;
    @SerializedName("likedislike")
    @Expose
    private Integer likedislike;
    @SerializedName("externallabel")
    @Expose
    private String externallabel;
    @SerializedName("externallink")
    @Expose
    private String externallink;

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public Integer getLikedislike() {
        return likedislike;
    }

    public void setLikedislike(Integer likedislike) {
        this.likedislike = likedislike;
    }

    public String getExternallabel() {
        return externallabel;
    }

    public void setExternallabel(String externallabel) {
        this.externallabel = externallabel;
    }

    public String getExternallink() {
        return externallink;
    }

    public void setExternallink(String externallink) {
        this.externallink = externallink;
    }
}
