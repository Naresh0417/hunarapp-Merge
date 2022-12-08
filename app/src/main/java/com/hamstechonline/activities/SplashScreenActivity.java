package com.hamstechonline.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.RemoteException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.ReferrerDetails;
import com.appsflyer.AppsFlyerLib;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamstechonline.R;
import com.hamstechonline.activities.splash.VersionRequest;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.VersionUpload;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ForceUpdate;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

public class SplashScreenActivity extends AppCompatActivity  {

    int version;
    ForceUpdate forceUpdate;
    ImageView splashImg;
    String gcm_id;
    LogEventsActivity logEventsActivity;
    String langPref = "Language";
    SharedPreferences prefs;
    private Locale myLocale;
    private InstallReferrerClient mReferrerClient;
    ApiInterface apiService;
    UserDataBase userDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        splashImg = findViewById(R.id.splashImg);

        forceUpdate = new ForceUpdate(this);
        logEventsActivity = new LogEventsActivity();


        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(this);
        //AppsFlyerLib.getInstance().startTracking(this);
        AppsFlyerLib.getInstance().startTracking(this);
        userDataBase = new UserDataBase(this);

        AppLinkData.fetchDeferredAppLinkData(this,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        // Process app link data
                    }
                }
        );
        Glide.with(this)
                .asGif()
                .load(R.drawable.splash_loading_gif)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(splashImg);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "");
        changeLang(langPref);
        getVersion();

    }


    private void getReferralUser() throws RemoteException {
        ReferrerDetails response = mReferrerClient.getInstallReferrer();
        String referrerData = response.getInstallReferrer();


        // for utm terms
        HashMap<String, String> values = new HashMap<>();
        if (values.containsKey("utm_medium") && values.containsKey("utm_term")) {
            try {
                if (referrerData != null) {
                    String referrers[] = referrerData.split("&");

                    for (String referrerValue : referrers) {
                        String keyValue[] = referrerValue.split("=");
                        values.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                    }

                }
            } catch (Exception e) {

            }
        }
    }

    private class DbCheck extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(result!=null) {
                if(result.equals("0")) {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( SplashScreenActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            gcm_id = newToken;
                            getLogEvent(SplashScreenActivity.this);
                            startActivity(new Intent(SplashScreenActivity.this,OnBoardingActivity.class));

                        }
                    });
                } else {

                    UserDataConstants.isNewUser = false;
                    uploadVersion();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            UserDataBase dh = new UserDataBase(SplashScreenActivity.this);
            int c = dh.getCount();
            if(c == 0) {
                return "0";
            } else {
                return "1";
            }

        }
    }

    public void checkDeepLink(){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        //changeLang(langPref);
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e("data","125   "+deepLink.toString());
                            Intent intent = new Intent(SplashScreenActivity.this,DynamicLinkingActivity.class);
                            intent.putExtra("deepLink",deepLink.toString());
                            startActivity(intent);
                            finish();
                        } else {

                            startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("data","125   "+e.toString());
                        startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
                    }
                });
    }

    public void getVersion(){
        VersionRequest versionRequest = new VersionRequest(getResources().getString(R.string.lblApiKey));
        Call<VersionRequest> call = apiService.getVersion(versionRequest);
        call.enqueue(new Callback<VersionRequest>() {
            @Override
            public void onResponse(Call<VersionRequest> call, retrofit2.Response<VersionRequest> response) {
                try {
                    if (response.body().getVersionResponse().getStatus().equals("200")) {
                        float recommendedVersion = Integer.parseInt(response.body().getVersionResponse().getVersion());

                        if (version >= recommendedVersion){
                            new DbCheck().execute();
                        } else{
                            forceUpdate.showLoadingDialog();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<VersionRequest> call, Throwable t) {

            }
        });
    }

    public void uploadVersion() {
        VersionUpload versionUpload = new VersionUpload(getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),version);
        Call<VersionUpload> call = apiService.uploadVersion(versionUpload);

        call.enqueue(new Callback<VersionUpload>() {
            @Override
            public void onResponse(Call<VersionUpload> call, Response<VersionUpload> response) {
                checkDeepLink();

            }

            @Override
            public void onFailure(Call<VersionUpload> call, Throwable t) {
                changeLang(langPref);
                startActivity(new Intent(SplashScreenActivity.this,HomePageActivity.class));
            }
        });
    }

    public void saveLocale(String lang){
        SharedPreferences prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }

    public void changeLang(String lang){
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        getVersion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getContent(SplashScreenActivity.this);
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", "");
            data.put("fullname","");
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson",gcm_id);
            data.put("activity","New User");
            data.put("pagename","GCM Id");
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
