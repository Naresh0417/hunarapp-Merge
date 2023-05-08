package com.hamstechonline.activities;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamstechonline.R;
import com.hamstechonline.adapters.CustomPageAdapter;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.OnBoardingRequest;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class OnBoardingNewActivity extends AppCompatActivity {

    String langPref;
    SharedPreferences prefs;
    ImageView splashImg;
    HocLoadingDialog hocLoadingDialog;
    String activityLog,gcm_id,pagenameLog;
    LogEventsActivity logEventsActivity;
    ApiInterface apiService;
    CountDownTimer countDownTimer;
    int imagesPosition = 0,countTime = 3000;
    private List<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        splashImg = findViewById(R.id.splashImg);

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( OnBoardingNewActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                gcm_id = newToken;
            }
        });

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        getContent();

        countDownTimer = new CountDownTimer(countTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("Timer","113   "+countTime);
                long sec = ((millisUntilFinished / 1000) % 60);
                Log.e("Timer","118   "+sec);

            }

            @Override
            public void onFinish() {
                Log.e("Timer","123   "+imagesPosition);
                if(imagesPosition < imagesList.size()-1) {
                    imagesPosition ++;
                    Glide.with(OnBoardingNewActivity.this)
                            .load(imagesList.get(imagesPosition))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(splashImg);
                    countTime = 5000;
                    if (imagesPosition == (imagesList.size() - 1)) {
                        pagenameLog = "Slide2";

                    } else{
                        pagenameLog = "Slide1";
                        //countTime = 3000;
                    }
                    activityLog = "Onboarding";
                    getLogEvent(OnBoardingNewActivity.this);
                    countDownTimer.start();
                    Log.e("Timer","130   "+imagesPosition);
                } else {
                    Intent intent = new Intent(OnBoardingNewActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
                /*Intent intent = new Intent(OnBoardingNewActivity.this, RegistrationActivity.class);
                startActivity(intent);*/
            }
        };
    }

    public void getContent() {
        OnBoardingRequest paymentSuccessResponse = new OnBoardingRequest("Hamstech", getResources().getString(R.string.lblApiKey),
                langPref);
        Call<OnBoardingRequest> call = apiService.getOnBoardResponse(paymentSuccessResponse);
        call.enqueue(new Callback<OnBoardingRequest>() {
            @Override
            public void onResponse(Call<OnBoardingRequest> call, retrofit2.Response<OnBoardingRequest> response) {
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    imagesList.clear();
                    imagesList = response.body().getImages();
                    Glide.with(OnBoardingNewActivity.this)
                            .load(response.body().getImages().get(0))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(splashImg);
                    countTime = 3000;
                    imagesPosition = 0;
                    countDownTimer.start();
                    pagenameLog = "Slide1";
                    activityLog = "Onboarding";
                    getLogEvent(OnBoardingNewActivity.this);
                } else {
                    Intent intent = new Intent(OnBoardingNewActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<OnBoardingRequest> call, Throwable t) {

            }
        });
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", "");
            data.put("fullname",gcm_id);
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson","");
            data.put("activity",activityLog);
            data.put("pagename",pagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
