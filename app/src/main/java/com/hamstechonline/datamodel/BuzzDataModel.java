package com.hamstechonline.datamodel;

import java.io.Serializable;

public class BuzzDataModel implements Serializable {

    String status;
    int postid;
    String category;
    String title;
    String description;
    String image;
    String videourl;
    int likedislike;
    String likes;
    String comments;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    int comment;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
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

    public int getLikedislike() {
        return likedislike;
    }

    public void setLikedislike(int likedislike) {
        this.likedislike = likedislike;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
