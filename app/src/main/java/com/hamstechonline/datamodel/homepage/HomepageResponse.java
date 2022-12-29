package com.hamstechonline.datamodel.homepage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hamstechonline.datamodel.favourite.FavouriteCategories;
import com.hamstechonline.datamodel.favourite.FavouriteCourse;

import java.util.ArrayList;
import java.util.List;

public class HomepageResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("categories_list")
    @Expose
    private List<EnglishCategory> english = null;
    @SerializedName("how_to_use_app")
    @Expose
    private List<HowToUseApp> howToUseApp = null;
    @SerializedName("main_video")
    @Expose
    private MainVideo mainVideo;
    @SerializedName("more_trial_classes")
    @Expose
    private List<MoreTrialClass> moreTrialClasses = null;
    @SerializedName("mini_lessons")
    @Expose
    private List<MiniLesson> miniLessons = null;
    @SerializedName("success_stories")
    @Expose
    private List<SuccessStory> successStories = null;
    @SerializedName("my_courses")
    @Expose
    private ArrayList<MyCourse> myCourses = null;
    @SerializedName("favourite_courses")
    @Expose
    private ArrayList<FavouriteCategories> favouriteCourse = null;
    @SerializedName("footer_ribbon_image")
    @Expose
    private String footerRibbonImage;
    @SerializedName("promotional_video")
    @Expose
    private String promotional_video;
    @SerializedName("celebrity_mentor_video")
    @Expose
    private String celebrity_mentor_video;
    @SerializedName("gif_image")
    @Expose
    private String gif_image;
    @SerializedName("celebrity_mentor_image")
    @Expose
    private String celebrity_mentor_image;

    String appname, page, apikey, lang, phone,type;

    public HomepageResponse(String appname, String page, String apikey, String lang, String phone,String type) {
        this.appname = appname;
        this.page = page;
        this.apikey = apikey;
        this.lang = lang;
        this.phone = phone;
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EnglishCategory> getEnglish() {
        return english;
    }

    public void setEnglish(List<EnglishCategory> english) {
        this.english = english;
    }

    public List<HowToUseApp> getHowToUseApp() {
        return howToUseApp;
    }

    public void setHowToUseApp(List<HowToUseApp> howToUseApp) {
        this.howToUseApp = howToUseApp;
    }

    public MainVideo getMainVideo() {
        return mainVideo;
    }

    public void setMainVideo(MainVideo mainVideo) {
        this.mainVideo = mainVideo;
    }

    public List<MoreTrialClass> getMoreTrialClasses() {
        return moreTrialClasses;
    }

    public void setMoreTrialClasses(List<MoreTrialClass> moreTrialClasses) {
        this.moreTrialClasses = moreTrialClasses;
    }

    public List<MiniLesson> getMiniLessons() {
        return miniLessons;
    }

    public void setMiniLessons(List<MiniLesson> miniLessons) {
        this.miniLessons = miniLessons;
    }

    public List<SuccessStory> getSuccessStories() {
        return successStories;
    }

    public void setSuccessStories(List<SuccessStory> successStories) {
        this.successStories = successStories;
    }

    public ArrayList<MyCourse> getMyCourses() {
        return myCourses;
    }

    public void setMyCourses(ArrayList<MyCourse> myCourses) {
        this.myCourses = myCourses;
    }

    public String getFooterRibbonImage() {
        return footerRibbonImage;
    }

    public void setFooterRibbonImage(String footerRibbonImage) {
        this.footerRibbonImage = footerRibbonImage;
    }

    public String getPromotional_video() {
        return promotional_video;
    }

    public void setPromotional_video(String promotional_video) {
        this.promotional_video = promotional_video;
    }

    public String getCelebrity_mentor_video() {
        return celebrity_mentor_video;
    }

    public void setCelebrity_mentor_video(String celebrity_mentor_video) {
        this.celebrity_mentor_video = celebrity_mentor_video;
    }

    public String getGif_image() {
        return gif_image;
    }

    public void setGif_image(String gif_image) {
        this.gif_image = gif_image;
    }

    public String getCelebrity_mentor_image() {
        return celebrity_mentor_image;
    }

    public void setCelebrity_mentor_image(String celebrity_mentor_image) {
        this.celebrity_mentor_image = celebrity_mentor_image;
    }

    public ArrayList<FavouriteCategories> getFavouriteCourse() {
        return favouriteCourse;
    }

    public void setFavouriteCourse(ArrayList<FavouriteCategories> favouriteCourse) {
        this.favouriteCourse = favouriteCourse;
    }
}
