package com.hamstechonline.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.adapters.FaqsAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class FaqsActivity extends AppCompatActivity {

    DrawerLayout drawer;
    BottomNavigationView navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    RecyclerView listItems;
    FaqsAdapter faqsAdapter;
    UserDataBase userDataBase;
    String PagenameLog;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.faqs_activity);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        listItems = findViewById(R.id.listItems);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);
        faqsAdapter = new FaqsAdapter(this,getResources().getStringArray(R.array.faqs_qns),getResources().getStringArray(R.array.faqs_ans));
        listItems.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listItems.setAdapter(faqsAdapter);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
            @Override
            public void onDrawerOpened(View drawerView) {

            }
            @Override
            public void onDrawerClosed(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    PagenameLog = "Home Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    Intent intentCourses = new Intent(FaqsActivity.this, HomePageActivity.class);
                    startActivity(intentCourses);
                    return true;
                case R.id.navigation_chat:
                    PagenameLog = "chat with whatsapp";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "chat with whatsapp");
                    logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
                    PackageManager packageManager = getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                                URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        /*if (i.resolveActivity(packageManager) != null) {
                            startActivity(i);
                        }*/
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                case R.id.navigation_enrol:
                    PagenameLog = "Success Story";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    Intent enrol = new Intent(FaqsActivity.this, SuccessStoryActivity.class);
                    startActivity(enrol);
                    return true;
                case R.id.navigation_today:
                    PagenameLog = "Hunar Club";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    Intent hamstech = new Intent(FaqsActivity.this, BuzzActivity.class);
                    startActivity(hamstech);
                    return true;
                case R.id.navigation_aboutus:
                    PagenameLog = "Contact Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    new AppsFlyerEventsHelper(FaqsActivity.this).EventContactus();
                    Intent about = new Intent(FaqsActivity.this, ContactActivity.class);
                    startActivity(about);
                    return true;
            }
            return false;
        }
    };

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course","");
            data.put("lesson","");
            data.put("activity","");
            data.put("pagename","Chat support");
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }
}
