package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class HocTodayData {

    @SerializedName("postid")
    private Integer postid;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("videourl")
    private String videourl;
    @SerializedName("likedislike")
    private Integer likedislike;
    @SerializedName("comment")
    private Integer comment;
    @SerializedName("externallabel")
    private String externallabel;
    @SerializedName("externallink")
    private String externallink;
    @SerializedName("likes")
    private String likes;
    @SerializedName("comments")
    private Integer comments;
    @SerializedName("profile_pic")
    private String profile_pic;
    @SerializedName("name_first_character")
    private String name_first_character;
    @SerializedName("name")
    private String name;

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

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName_first_character() {
        return name_first_character;
    }

    public void setName_first_character(String name_first_character) {
        this.name_first_character = name_first_character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
