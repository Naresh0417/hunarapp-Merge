package com.hamstechonline.restapi;

import com.hamstechonline.activities.lesson.LessonData;
import com.hamstechonline.activities.splash.VersionRequest;
import com.hamstechonline.datamodel.AskDoubtResponse;
import com.hamstechonline.datamodel.CheckStudent;
import com.hamstechonline.datamodel.CalculateCoursePayment;
import com.hamstechonline.datamodel.CallWithFacultyResponse;
import com.hamstechonline.datamodel.CommentsCountData;
import com.hamstechonline.datamodel.CommentsDataModel;
import com.hamstechonline.datamodel.CourseDetailsResponse;
import com.hamstechonline.datamodel.CourseType;
import com.hamstechonline.datamodel.DiscussionsModel;
import com.hamstechonline.datamodel.EnrolNotification;
import com.hamstechonline.datamodel.FlashOfferResponse;
import com.hamstechonline.datamodel.GetChatNumber;
import com.hamstechonline.datamodel.GetDynamicData;
import com.hamstechonline.datamodel.HocResponse;
import com.hamstechonline.datamodel.HocTodayResponse;
import com.hamstechonline.datamodel.HunarClubPostClick;
import com.hamstechonline.datamodel.LikesCountData;
import com.hamstechonline.datamodel.LiveClassRegistrationResponse;
import com.hamstechonline.datamodel.LiveClassesResponse;
import com.hamstechonline.datamodel.LoginSignupData;
import com.hamstechonline.datamodel.MyCourseGetChatNumber;
import com.hamstechonline.datamodel.OnBoardingRequest;
import com.hamstechonline.datamodel.PayinstallmentRequest;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.ReferralCallbackRequest;
import com.hamstechonline.datamodel.SaveCommentReply;
import com.hamstechonline.datamodel.SaveCommentReport;
import com.hamstechonline.datamodel.SaveCommentRequest;
import com.hamstechonline.datamodel.SavePostReport;
import com.hamstechonline.datamodel.UploadPostDisscussions;
import com.hamstechonline.datamodel.UploadPostResponse;
import com.hamstechonline.datamodel.UserBlockReport;
import com.hamstechonline.datamodel.UserReplyCommentData;
import com.hamstechonline.datamodel.VersionUpload;
import com.hamstechonline.datamodel.favourite.FavouriteResponse;
import com.hamstechonline.datamodel.homepage.HomepageResponse;
import com.hamstechonline.datamodel.mycources.DownloadCertificate;
import com.hamstechonline.datamodel.mycources.MyCoursesResponse;
import com.hamstechonline.datamodel.mycources.UploadResponse;
import com.hamstechonline.editprofile.datamodel.MentorResponse;
import com.hamstechonline.editprofile.datamodel.ProfileData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("https://android.hamstechonline.com/api/list/getmasterdata/")
    Call<MentorResponse> cityNames(@Body MentorResponse mentorResponse);

    @POST("https://android.hamstechonline.com/api/list/getprofile/")
    Call<ProfileData> userProfile(@Body ProfileData userData);

    @POST("https://android.hamstechonline.com/api/list/getversion")
    Call<VersionRequest> getVersion(@Body VersionRequest versionRequest);

    @POST("https://android.hamstechonline.com/api/list/get_lesson_detail/")
    Call<LessonData> getLessonData(@Body LessonData lessonData);

    @POST("https://android.hamstechonline.com/api/list/get_login_register_images/")
    Call<LoginSignupData> getLoginSignupData(@Body LoginSignupData loginSignupData);

    @POST("https://android.hamstechonline.com/api/list/get_likes_count")
    Call<LikesCountData> getLikesCount(@Body LikesCountData likesCountData);

    @POST("https://android.hamstechonline.com/api/list/get_comments_count")
    Call<CommentsCountData> getCommentsCountData(@Body CommentsCountData commentsCountData);

    @POST("https://android.hamstechonline.com/api/list/get_comments")
    Call<CommentsDataModel> getCommentsData(@Body CommentsDataModel commentsDataModel);

    @POST("https://android.hamstechonline.com/api/list/save_comment")
    Call<SaveCommentRequest> saveComment(@Body SaveCommentRequest saveCommentRequest);

    @POST("http://android.hamstechonline.com/api/list/get_menu_items")
    Call<GetDynamicData> getDynamicData(@Body GetDynamicData getDynamicData);

    @POST("https://android.hamstechonline.com/api/list/getHocTodayPostTags")
    Call<HocResponse> getHocResponse(@Body HocResponse hocResponse);

    @POST("https://android.hamstechonline.com/api/list/getHocTodayNotifications")
    Call<HocTodayResponse> getHocTodayResponse(@Body HocTodayResponse hocTodayResponse);

    @POST("https://android.hamstechonline.com/api/list/getCourseDetails")
    Call<CourseDetailsResponse> getCourseDetails(@Body CourseDetailsResponse courseDetailsResponse);

    @POST("https://android.hamstechonline.com/api/list/getHomePageData")
    Call<HomepageResponse> getHomepageResponse(@Body HomepageResponse homepageResponse);

    @POST("http://android.hamstechonline.com/api/list/getEnrolledCourseDetails")
    Call<MyCoursesResponse> getMyCoursesResponse(@Body MyCoursesResponse myCoursesResponse);

    @POST("http://android.hamstechonline.com/api/list/uploadAssignment")
    Call<UploadResponse> getUploadResponse(@Body UploadResponse uploadResponse);

    @POST("http://android.hamstechonline.com/api/list/downloadCertificate")
    Call<DownloadCertificate> getDownloadCertificate(@Body DownloadCertificate downloadCertificate);

    @POST("http://android.hamstechonline.com/api/list/videoCallWithFaculty")
    Call<CallWithFacultyResponse> getCallWithFacultyResponse(@Body CallWithFacultyResponse callWithFacultyResponse);

    @POST("https://android.hamstechonline.com/api/list/capturePayment")
    Call<PaymentSuccessResponse> getPaymentSuccess(@Body PaymentSuccessResponse paymentSuccessResponse);

    @POST("http://android.hamstechonline.com/api/list/getCourseType")
    Call<CourseType> getCourseType(@Body CourseType courseType);

    @POST("http://android.hamstechonline.com/api/list/enrolNotification")
    Call<EnrolNotification> getEnrolNotification(@Body EnrolNotification enrolNotification);

    @POST("http://android.hamstechonline.com/api/list/getMiniLessonOffer")
    Call<FlashOfferResponse> getFlashOfferResponse(@Body FlashOfferResponse flashOfferResponse);

    @POST("https://android.hamstechonline.com/api/list/createUserPost")
    Call<UploadPostResponse> getUploadPost(@Body UploadPostResponse uploadResponse);

    @POST("https://android.hamstechonline.com/api/list/createDiscussionPost")
    Call<UploadPostDisscussions> getDiscussionUploadPost(@Body UploadPostDisscussions uploadResponse);

    @POST("https://android.hamstechonline.com/api/list/captureVersion")
    Call<VersionUpload> uploadVersion(@Body VersionUpload versionUpload);

    @POST("http://android.hamstechonline.com/api/list/getDiscussions")
    Call<DiscussionsModel> getDiscussionsData(@Body DiscussionsModel discussionsModel);

    @POST("http://android.hamstechonline.com/api/list/saveCommentReply")
    Call<SaveCommentReply> saveCommentReply(@Body SaveCommentReply saveCommentReply);

    @POST("http://android.hamstechonline.com/api/list/getCommentReplies")
    Call<UserReplyCommentData> getUserCommentReplyData(@Body UserReplyCommentData userReplyCommentData);

    @POST("https://www.hunarcourses.com/api/payinstallment")
    Call<PayinstallmentRequest> getPayinstallment(@Body PayinstallmentRequest payinstallmentRequest);

    @POST("http://android.hamstechonline.com/api/list/getSalesWABANumber")
    Call<GetChatNumber> getChatNumber(@Body GetChatNumber getChatNumber);

    @POST("http://android.hamstechonline.com/api/list/getPostSalesWABANumber")
    Call<MyCourseGetChatNumber> getMyCourseChatNumber(@Body MyCourseGetChatNumber getChatNumber);

    @POST("http://android.hamstechonline.com/api/list/getEnrolWABANumber")
    Call<GetChatNumber> getEnrolChatNumber(@Body GetChatNumber getChatNumber);

    @POST("http://android.hamstechonline.com/api/list/calculateCoursePayment")
    Call<CalculateCoursePayment> getCalculateCoursePayment(@Body CalculateCoursePayment calculateCoursePayment);

    @POST("http://android.hamstechonline.com/api/list/savePostReport")
    Call<SavePostReport> getSavePostReport(@Body SavePostReport savePostReport);

    @POST("http://android.hamstechonline.com/api/list/saveCommentReport")
    Call<SaveCommentReport> getCommentPostReport(@Body SaveCommentReport savePostReport);

    @POST("http://android.hamstechonline.com/api/list/blockUser")
    Call<UserBlockReport> getBlockUser(@Body UserBlockReport savePostReport);

    @POST("http://android.hamstechonline.com/api/list/checkStudent")
    Call<CheckStudent> getCheckStudent(@Body CheckStudent savePostReport);

    @POST("http://android.hamstechonline.com/api/list/getFavouriteCategories")
    Call<FavouriteResponse> getFavouriteResponse(@Body FavouriteResponse favouriteResponse);

    @POST("http://android.hamstechonline.com/api/list/getOnboardingVideo")
    Call<OnBoardingRequest> getOnBoardResponse(@Body OnBoardingRequest onBoardingRequest);

    @POST("http://android.hamstechonline.com/api/list/getLiveClasses")
    Call<LiveClassesResponse> getYourLiveClasses(@Body LiveClassesResponse liveClassesResponse);

    @POST("http://android.hamstechonline.com/api/list/getAllLiveClasses")
    Call<LiveClassesResponse> getYourAllLiveClasses(@Body LiveClassesResponse liveClassesResponse);

    @POST("http://android.hamstechonline.com/api/list/liveClassRegistration")
    Call<LiveClassRegistrationResponse> getLiveClassRegistration(@Body LiveClassRegistrationResponse liveClassesResponse);

    @POST("http://android.hamstechonline.com/api/list/savePostVisits")
    Call<HunarClubPostClick> getHunarClubPost(@Body HunarClubPostClick hunarClubPostClick);

    @POST("http://android.hamstechonline.com/api/list/savePostShares")
    Call<HunarClubPostClick> getHunarClubShare(@Body HunarClubPostClick hunarClubPostClick);

    @POST("http://android.hamstechonline.com/api/list/askDoubt")
    Call<AskDoubtResponse> askDoubtApi(@Body AskDoubtResponse hunarClubPostClick);

    @POST("http://android.hamstechonline.com/api/list/referralCallbackRequest")
    Call<ReferralCallbackRequest> referralCallbackRequest(@Body ReferralCallbackRequest referralCallbackRequest);
}
