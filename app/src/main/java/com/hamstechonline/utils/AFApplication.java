package com.hamstechonline.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContextParams;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.hamstechonline.R;
import com.hamstechonline.activities.SplashScreenActivity;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;
import com.moengage.inapp.MoEInAppHelper;

import java.util.ArrayList;

public class AFApplication extends Application {

    private static final String AF_DEV_KEY = "iMyoCF4yG7EqtzUPPW845e";
    AdjustConfig config;

    @Override
    public void onCreate() {
        super.onCreate();

        ArrayList inAppOptOut = new ArrayList<>();
        inAppOptOut.add(SplashScreenActivity.class);

        MoEngage moEngage = new MoEngage.Builder(this, "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                //.optOutDefaultInAppDisplay()
                .optOutInAppOnActivity(inAppOptOut)
                .build();
        MoEngage.initialise(moEngage);

        MoEInAppHelper.getInstance().getSelfHandledInApp(this);

        MoEInAppHelper.getInstance().showInApp(this);

        //MoEHelper.getInstance(requireContext()).resetAppContext();

        String appToken = "fxa01vah756o";
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
        config = new AdjustConfig(this, appToken, environment);
        config.setUrlStrategy(AdjustConfig.URL_STRATEGY_INDIA);
        //config.setLogLevel(LogLevel.WARN);
        Adjust.onCreate(config);

        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

    }
    @Override public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MoEInAppHelper.getInstance().onConfigurationChanged();
    }
    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }

        //...
    }


}
