package com.hamstechonline.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.hamstechonline.R;
import com.hamstechonline.activities.BuzzActivity;
import com.hamstechonline.activities.ContactActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.LiveClassesActivity;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class FooterNavigationPaid extends Fragment {

    LinearLayout layoutContact,layoutHome,imgHunarClub,layoutLiveClass;
    String CourseLog="",LessonLog,ActivityLog,PagenameLog;
    AppEventsLogger logger;
    Bundle params;
    LogEventsActivity logEventsActivity;
    CheckBox checkboxHome,checkboxHunarClub,checkboxLiveClass,checkboxContactus;

    public static FooterNavigationPaid newInstance(){
        return new FooterNavigationPaid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.footer_menu_paid,
                container, false);

        layoutHome = view.findViewById(R.id.layoutHome);
        layoutContact = view.findViewById(R.id.layoutContact);
        imgHunarClub = view.findViewById(R.id.imgHunarClub);
        checkboxHome = view.findViewById(R.id.checkboxHome);
        layoutLiveClass = view.findViewById(R.id.layoutLiveClass);
        checkboxHunarClub = view.findViewById(R.id.checkboxHunarClub);
        checkboxLiveClass = view.findViewById(R.id.checkboxLiveClass);
        checkboxContactus = view.findViewById(R.id.checkboxContactus);

        logger = AppEventsLogger.newLogger(getActivity());
        params = new Bundle();
        logEventsActivity = new LogEventsActivity();

        Fragment fragment = getFragmentManager().findFragmentById(R.id.footer_menu);
        String tag = (String) fragment.getTag();
        Log.e("tagname", "67     "+tag);
        LessonLog = (String) fragment.getTag();

        /*checkboxHome.setEnabled(false);
        checkboxHunarClub.setEnabled(false);
        checkboxLiveClass.setEnabled(false);
        checkboxContactus.setEnabled(false);*/

        if (tag.equalsIgnoreCase("Home Page")) {
            checkboxHome.setChecked(true);
            checkboxHunarClub.setChecked(false);
            checkboxLiveClass.setChecked(false);
            checkboxContactus.setChecked(false);

        } else if (tag.equalsIgnoreCase("Hunar Club")) {
            checkboxHome.setChecked(false);
            checkboxHunarClub.setChecked(true);
            checkboxLiveClass.setChecked(false);
            checkboxContactus.setChecked(false);
        } else if (tag.equalsIgnoreCase("Live Class")) {
            checkboxHome.setChecked(false);
            checkboxHunarClub.setChecked(false);
            checkboxLiveClass.setChecked(true);
            checkboxContactus.setChecked(false);
        } else if (tag.equalsIgnoreCase("Contact Us")) {
            checkboxHome.setChecked(false);
            checkboxHunarClub.setChecked(false);
            checkboxLiveClass.setChecked(false);
            checkboxContactus.setChecked(true);
        }

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Click";
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(getActivity());
                Intent intentCourses = new Intent(getActivity(), HomePageActivity.class);
                startActivity(intentCourses);
            }
        });

        imgHunarClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Click";
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);

                getLogEvent(getActivity());
                Intent hamstech = new Intent(getActivity(), BuzzActivity.class);
                startActivity(hamstech);
            }
        });

        layoutLiveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Click";
                PagenameLog = "Live Class";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);

                getLogEvent(getActivity());
                Intent hamstech = new Intent(getActivity(), LiveClassesActivity.class);
                startActivity(hamstech);
            }
        });

        layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Home page";
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);

                getLogEvent(getActivity());
                new AppsFlyerEventsHelper(getActivity()).EventContactus();
                Intent about = new Intent(getActivity(), ContactActivity.class);
                startActivity(about);
            }
        });
        checkboxHome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ActivityLog = "Click";
                    PagenameLog = "Home Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(getActivity());
                    Intent intentCourses = new Intent(getActivity(), HomePageActivity.class);
                    startActivity(intentCourses);
                }
            }
        });

        checkboxHunarClub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ActivityLog = "Click";
                    PagenameLog = "Home Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(getActivity());
                    Intent intentCourses = new Intent(getActivity(), BuzzActivity.class);
                    startActivity(intentCourses);
                }
            }
        });
        checkboxLiveClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ActivityLog = "Click";
                    PagenameLog = "Home Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(getActivity());
                    Intent intentCourses = new Intent(getActivity(), LiveClassesActivity.class);
                    startActivity(intentCourses);
                }
            }
        });
        checkboxContactus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ActivityLog = "Click";
                    PagenameLog = "Home Page";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(getActivity());
                    Intent intentCourses = new Intent(getActivity(), ContactActivity.class);
                    startActivity(intentCourses);
                }
            }
        });

        return view;
    }

    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
