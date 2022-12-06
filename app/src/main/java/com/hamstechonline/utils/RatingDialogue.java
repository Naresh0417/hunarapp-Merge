package com.hamstechonline.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;
import com.hamstechonline.R;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingDialogue {

    Context context;
    Dialog dialog;
    TextView txtSure, txtLater,txtHeader,txtContent;
    ImageView imgCancel;
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog;
    boolean isRatingSelected;
    AppEventsLogger logger;
    Bundle params;
    ReviewManager manager;

    public RatingDialogue(Context context){
        this.context = context;
        manager = ReviewManagerFactory.create(context);
    }

    public void showLoadingDialog() {

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgCancel = dialog.findViewById(R.id.imgCancel);
        txtSure = dialog.findViewById(R.id.txtSure);
        txtLater = dialog.findViewById(R.id.txtLater);
        txtHeader = dialog.findViewById(R.id.txtHeader);
        txtContent = dialog.findViewById(R.id.txtContent);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(context);
        params = new Bundle();
        dialog.show();

        txtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRatingSelected == false) {
                    isRatingSelected = true;
                    ActivityLog = "Clicked on "+txtSure.getText().toString();
                    txtHeader.setText(context.getResources().getString(R.string.ratingHeader));
                    txtSure.setText(context.getResources().getString(R.string.ratingSure));
                    txtLater.setPadding(20,5,20,12);
                    txtLater.setText(context.getResources().getString(R.string.ratingLater));
                    txtContent.setVisibility(View.VISIBLE);
                    PagenameLog = "Rating Pop";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(context);
                } else if (isRatingSelected == true) {
                    /*ActivityLog = "Clicked on "+txtSure.getText().toString();
                    PagenameLog = "Rating Pop";
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                    getLogEvent(context);
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.hamstechonline&hl=en")));
                    isRatingSelected = false;
                    dialog.dismiss();*/

                    Task<ReviewInfo> request = manager.requestReviewFlow();
                    request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
                        @Override
                        public void onComplete(@NonNull Task<ReviewInfo> task) {
                            if (task.isSuccessful()) {
                                // We can get the ReviewInfo object
                                final ReviewInfo reviewInfo = task.getResult();
                                Task<Void> flow = manager.launchReviewFlow((Activity) context, reviewInfo);
                                flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=com.hamstechonline&hl=en")));
                                    }
                                });
                            } else {
                                // There was some problem, continue regardless of the result.
                            }
                        }
                    });
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRatingSelected = false;
                hideDialog();
            }
        });
        txtLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Clicked on "+txtLater.getText().toString();
                PagenameLog = "Rating Pop";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(context);
                isRatingSelected = false;
                hideDialog();
            }
        });
    }
    public void hideDialog(){
        dialog.dismiss();
    }
    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile",UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson","");
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
