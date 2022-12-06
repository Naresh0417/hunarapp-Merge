package com.hamstechonline.datamodel;

import com.google.gson.annotations.SerializedName;

public class CommentsData {
    @SerializedName("comment_id")
    private String comment_id;
    @SerializedName("post_id")
    private String post_id;
    @SerializedName("page")
    private String page;
    @SerializedName("comment")
    private String comment;
    @SerializedName("name")
    private String name;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("profile_pic")
    private String profile_pic;
    @SerializedName("profilepic")
    private String profilepic;
    @SerializedName("created_at")
    private String created_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
