package com.hamstechonline.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Properties;

import java.util.HashMap;
import java.util.Map;

public class AppsFlyerEventsHelper {
    Context context;
    Map<String,Object> eventData = new HashMap<>();
    SharedPrefsUtils sharedPrefsUtils;
    AppEventsLogger logger;
    Bundle params;
    FirebaseAnalytics mFirebaseAnalytics;
    Properties properties;
    String langPref = "Language";
    SharedPreferences prefs;

    public AppsFlyerEventsHelper(Context context) {
        this.context = context;
        params = new Bundle();
        properties = new Properties();
        logger = AppEventsLogger.newLogger(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        sharedPrefsUtils = new SharedPrefsUtils(context, context.getString(R.string.app_name));
        prefs = context.getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
    }

    public void EventRegistration(){
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_REGISTRATION,properties);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_LANGUAGE,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventEnroll(){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        params.putString(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        params.putString(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        params.putString(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        params.putString(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        logger.logEvent(SocialMediaEventsParameters.KEY_ENROLL_NOW,params);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.KEY_ENROLL_NOW, params);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_ENROLL,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventContactus(){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_CONTACTUS, params);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_CONTACTUS,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventIsStudent(){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        //AppsFlyerLib.getInstance().trackEvent(context, AppsFlyerEventParameter.EVENT_CONTACTUS,eventData);
        logger.logEvent(SocialMediaEventsParameters.SUBSCRIBE,params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_NAME_SUBSCRIBE, params);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.KEY_ISSTUDENT,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventMyCourse(){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        //AppsFlyerLib.getInstance().trackEvent(context, AppsFlyerEventParameter.EVENT_CONTACTUS,eventData);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACHIEVED_LEVEL,params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_NAME_SUBSCRIBE, params);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.KEY_ISSTUDENT,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventAccordion(String accordion,String course, String category){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_ACCORDION,accordion);
        eventData.put(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_CATEGORY,category);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.KEY_ISSTUDENT,properties);
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventCategory(String category){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_CATEGORY,category);

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_CATEGORY,category);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_CATEGORY,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventCourse(String course){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_COURSE,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventLessons(String category, String course, String lesson){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_CATEGORY,category);
        eventData.put(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_LESSON_NAME,lesson);

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        properties.addAttribute(AppsFlyerEventParameter.KEY_CATEGORY,category);
        properties.addAttribute(AppsFlyerEventParameter.KEY_LESSON_NAME,lesson);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_LESSON,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventNotification(String notification){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_NOTIFICATION,notification);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_NOTIFICATION,notification);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_NOTIFICATION,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventAdmission(String course,String payment_method){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_PAYMENT_METHOD,payment_method);
        eventData.put(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_PAYMENT_METHOD,payment_method);
        properties.addAttribute(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.EVENT_ADMISSION,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventLessonsDetails(String category, String course, String lesson, String eventName){
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_CATEGORY,category);
        eventData.put(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        eventData.put(AppsFlyerEventParameter.KEY_LESSON_NAME,lesson);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));

        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_CATEGORY,category);
        properties.addAttribute(AppsFlyerEventParameter.KEY_COURSE_NAME,course);
        properties.addAttribute(AppsFlyerEventParameter.KEY_LESSON_NAME,lesson);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(eventName,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventSuccessStories(){
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.KEY_SuccessStories,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
    public void EventAboutUs(){
        eventData.put(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_USER_NAME,UserDataConstants.userName);
        properties.addAttribute(AppsFlyerEventParameter.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        properties.addAttribute(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
        MoEHelper.getInstance(context).trackEvent(AppsFlyerEventParameter.KEY_ABOUTUS,properties);
        MoEHelper.getInstance(context).setFirstName(UserDataConstants.userName);
        MoEHelper.getInstance(context).setNumber(UserDataConstants.userMobile);
        MoEHelper.getInstance(context).setUniqueId(UserDataConstants.userMobile);
    }
}
