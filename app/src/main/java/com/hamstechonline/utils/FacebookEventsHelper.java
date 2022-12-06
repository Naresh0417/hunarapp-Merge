package com.hamstechonline.utils;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;

public class FacebookEventsHelper {
    Context context;
    AppEventsLogger mFirebaseAnalytics;

    public FacebookEventsHelper(Context context){
        this.context = context;
        mFirebaseAnalytics = AppEventsLogger.newLogger(context);
    }

    public void logSpendCreditsEvent(String content){
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, content);
        mFirebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SPENT_CREDITS, params);
    }
}
