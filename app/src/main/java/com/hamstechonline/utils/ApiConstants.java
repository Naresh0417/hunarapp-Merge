package com.hamstechonline.utils;

import android.os.Environment;

import java.io.File;

public class ApiConstants {
    public static String BaseUrl = "https://android.hamstechonline.com/";
    public static String getOTP = BaseUrl+"api/list/getotp/";
    public static String verifyOTP = BaseUrl+"api/list/verifyotp/";
    public static String getCategoriesList = BaseUrl+"api/list/get_categories_list";
    public static String getHomePageData = BaseUrl+"api/list/getHomePageData";
    public static String getListCourses = BaseUrl+"api/list/get_list_courses/";
    public static String getSearchList = BaseUrl+"api/list/get_search_list/";
    public static String getProfile = BaseUrl+"api/list/getprofile/";
    public static String uploadProfile = BaseUrl+"api/list/updateprofile/";
    public static String getListLessons = BaseUrl+"api/list/get_list_lessons/";
    public static String getRequestCallBack = BaseUrl+"api/list/requestcallback/";
    public static String getMaterialData = BaseUrl+"api/list/getmasterdata/";
    public static String uploadImage = BaseUrl+"api/list/updateimage/";
    public static String getNotifications = BaseUrl+"api/list/get_notification";
    public static String get_courses_bycat = BaseUrl+"api/list/get_courses_bycat/";
    public static String get_hamstechtoday = BaseUrl+"api/list/get_hamstechtoday";
    public static String post_createhocorder = "https://www.hunarcourses.com/api/createcodorder";
    public static String create_hoconlineorder = "https://www.hunarcourses.com/api/createonlineorder";
    public static String getAboutData = BaseUrl+"api/list/aboutus";
    public static String init_transactions = BaseUrl+"api/list/init_transactions";
    public static String init_QR_transactions = BaseUrl+"api/list/init_qr_transactions";
    public static String getskills = BaseUrl+"api/list/getskills/";
    public static String getGcmid = BaseUrl+"api/list/updategcm/";
    public static String getOnBoarding = BaseUrl+"api/list/boardingpage";
    public static String getLogevent = BaseUrl+"api/list/logevent";
    public static String redirectUrl = BaseUrl+"api/list/transactions/";
    public static String getRsaKey = BaseUrl+"rsa-handling-ccavenue/GetRSA.php?ORDER_ID=";
    public static String getTopics = BaseUrl+"api/list/get_topics_list";
    public static String getRecommendedTopics = BaseUrl+"api/list/get_recommended_courses";
    public static String setRecommendedTopics = BaseUrl+"api/list/get_recommended_courses_insert";
    public static String save_like_dislike = BaseUrl+"api/list/save_like_dislike";
    public static String getversion = BaseUrl+"api/list/getversion";
    public static String get_nsdc_categories_list = BaseUrl+"api/list/get_nsdc_categories_list/";
    public static String get_lang_list = BaseUrl+"api/list/get_lang_list/";
    public static String get_success_stories = BaseUrl+"api/list/get_success_stories";
    public static String get_lesson_detail = BaseUrl+"api/list/get_lesson_detail/";
    public static String getLikesCount = BaseUrl+"api/list/get_likes_count";
    public static String getCourseDetails = BaseUrl+"api/list/getCourseDetails";
    public static String getEnrolledLessonDetails = BaseUrl + "api/list/getEnrolledLessonDetails";
    public static String uploadAssignment = BaseUrl + "api/list/uploadAssignment";
    public static String saveLessonLikeDislike = BaseUrl + "api/list/saveLessonLikeDislike";
    public static String saveLessonLike = BaseUrl + "api/list/saveLessonLike";
    public static String saveLessonDislike = BaseUrl + "api/list/saveLessonDislike";

    public static String isFromCourse = "isFromCourse";
    public static String deviceId = "deviceId";
    public static String gaid = "gaid";
    public static String userProfilePic = "userProfilePic";

    public static String BASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "HunarOnline";
    //public static String BASE_PATH = Environment.getExternalStorageDirectory()+ "/HunarOnline/";

}
