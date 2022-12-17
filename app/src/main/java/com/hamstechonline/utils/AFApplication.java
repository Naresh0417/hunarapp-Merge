package com.hamstechonline.utils;

import android.app.Application;
import com.hamstechonline.R;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

public class AFApplication extends Application {

    private static final String AF_DEV_KEY = "iMyoCF4yG7EqtzUPPW845e";

    @Override
    public void onCreate() {
        super.onCreate();

        MoEngage moEngage = new MoEngage.Builder(this, "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

    }
}
