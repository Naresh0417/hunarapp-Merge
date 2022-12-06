package com.hamstechonline.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {

    @SerializedName("reply_id")
    @Expose
    private String replyId;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("profilepic")
    @Expose
    private String profilepic;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
