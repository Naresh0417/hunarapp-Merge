package com.hamstechonline.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.Properties;

import java.util.HashMap;
import java.util.Map;

public class SocialMediaEventsHelper {

    Context context;
    Map<String,Object> eventData = new HashMap<>();
    AppEventsLogger logger;
    FirebaseAnalytics mFirebaseAnalytics;
    Bundle params;
    SharedPrefsUtils sharedPrefsUtils;
    Properties properties;
    String langPref = "Language";
    SharedPreferences prefs;

    public SocialMediaEventsHelper(Context context) {
        this.context = context;
        params = new Bundle();
        logger = AppEventsLogger.newLogger(context);
        properties = new Properties();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        sharedPrefsUtils = new SharedPrefsUtils(context, context.getString(R.string.app_name));
        prefs = context.getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        if (langPref.equalsIgnoreCase("hi")) {
            langPref = "Hindi";
        } else {
            langPref = "English";
        }
        eventData.put(SocialMediaEventsParameters.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(SocialMediaEventsParameters.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        params.putString(SocialMediaEventsParameters.KEY_USER_NAME,UserDataConstants.userName);
        params.putString(SocialMediaEventsParameters.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        params.putString(SocialMediaEventsParameters.KEY_DEVICE_ID, sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        params.putString(SocialMediaEventsParameters.KEY_GAID, sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        properties.addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref);
    }

    public void EventRegistration(){
        eventData.put(SocialMediaEventsParameters.KEY_USER_NAME,UserDataConstants.userName);
        eventData.put(SocialMediaEventsParameters.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(SocialMediaEventsParameters.KEY_MOBILE_NUMBER,UserDataConstants.userMobile);
        eventData.put(SocialMediaEventsParameters.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(SocialMediaEventsParameters.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        AppsFlyerLib.getInstance().trackEvent(context, SocialMediaEventsParameters.EVENT_REGISTRATION,eventData);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_REGISTRATION, params);
    }

    public void EventContact(){
        logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_CONTACTUS, params);
        AppsFlyerLib.getInstance().trackEvent(context, SocialMediaEventsParameters.EVENT_CONTACTUS,eventData);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
    }
    public void EventRegisterCourse() {
        logger.logEvent(SocialMediaEventsParameters.EVENT_REGISTERCOURSE, params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_REGISTERCOURSE, params);
        AppsFlyerLib.getInstance().trackEvent(context, SocialMediaEventsParameters.EVENT_REGISTERCOURSE,eventData);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
    }
    public void EventCourseWatched(String courseName) {
        params.putString(SocialMediaEventsParameters.KEY_COURSE_NAME,courseName);
        eventData.put(SocialMediaEventsParameters.KEY_COURSE_NAME,courseName);
        logger.logEvent(SocialMediaEventsParameters.EVENT_COURSES_WATCHED, params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_COURSES_WATCHED, params);
        AppsFlyerLib.getInstance().trackEvent(context, SocialMediaEventsParameters.EVENT_COURSES_WATCHED,eventData);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
    }
    public void EventAccordion(String EventName){
        params.putString(SocialMediaEventsParameters.KEY_ACCORDION_NAME,EventName);
        eventData.put(SocialMediaEventsParameters.KEY_ACCORDION_NAME,EventName);
        logger.logEvent(SocialMediaEventsParameters.EVENT_ACCORDION, params);
        logger.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.EVENT_ACCORDION, params);
        eventData.put(AppsFlyerEventParameter.KEY_DEVICE_ID,sharedPrefsUtils.getDeviceId(ApiConstants.deviceId,""));
        eventData.put(AppsFlyerEventParameter.KEY_GAID,sharedPrefsUtils.getGaidId(ApiConstants.gaid,""));
        AppsFlyerLib.getInstance().trackEvent(context, SocialMediaEventsParameters.EVENT_ACCORDION,eventData);
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
    public void logLessonWatchedEvent (String course) {
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, course);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL, params);
        params.putString(SocialMediaEventsParameters.COURSE_NAME, course);
        mFirebaseAnalytics.logEvent(SocialMediaEventsParameters.COURSES_WATCHED, params);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
    }
}
