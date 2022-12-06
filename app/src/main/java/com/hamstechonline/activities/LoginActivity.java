package com.hamstechonline.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CheckStudent;
import com.hamstechonline.datamodel.LoginSignupData;
import com.hamstechonline.datamodel.UserDatamodel;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventParameter;
import com.hamstechonline.utils.ConnectionDetector;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.CustomAsync;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.OnAsyncCompleteRequest;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.Properties;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

public class LoginActivity extends AppCompatActivity {

    LinearLayout linOtpLayout,chatUs;
    Button btnResend, btnGetOtp,btnVerify;
    EditText txtOTP,txtMobile;
    TextView txtExpire,txtOr,otpMessage;
    boolean isNet,isTimeOut = false;
    String resMobile,otphash,otptimestamp,cityName="",getPincode="",getAddress="",getState="",
            getCountryName="",gcm_id,countrycode,networkType = "",lastNumber = "";
    private LocationManager locationManager;
    private String provider;
    int lengthMobile = 0,downSpeed = 0, upSpeed = 0,c;
    ImageView imgTop, imgBottom;
    Location location;
    Criteria criteria;
    String eventType = "",android_id,adId;
    CountDownTimer countDownTimer;
    CountryCodePicker countryCodePicker;
    LogEventsActivity logEventsActivity;
    UserDataBase dh;
    String langPref = "Language";
    SharedPreferences prefs;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    HocLoadingDialog hocLoadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity);

        linOtpLayout = findViewById(R.id.linOtpLayout);
        btnResend = findViewById(R.id.btnResend);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnVerify = findViewById(R.id.btnVerify);
        txtOTP = findViewById(R.id.txtOTP);
        txtMobile = findViewById(R.id.txtMobile);
        txtExpire = findViewById(R.id.txtExpire);
        countryCodePicker = findViewById(R.id.ccp);
        txtOr = findViewById(R.id.txtOr);
        otpMessage = findViewById(R.id.otpMessage);
        chatUs = findViewById(R.id.chatUs);
        imgTop = findViewById(R.id.imgTop);
        imgBottom = findViewById(R.id.imgBottom);

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(this);

        logEventsActivity = new LogEventsActivity();
        hocLoadingDialog = new HocLoadingDialog(this);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        dh = new UserDataBase(LoginActivity.this);
        sharedPrefsUtils = new SharedPrefsUtils(LoginActivity.this, getString(R.string.app_name));

        txtOr.setText(getResources().getString(R.string.Or)+"  ");
        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);
        checkNetwork();
        if (countryCodePicker.getSelectedCountryCode().equals("91")){
            lengthMobile = 10;
            countrycode = countryCodePicker.getSelectedCountryCode();
        } else {
            lengthMobile = 0;
        }
        //takeCameraPermission();
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycode = countryCodePicker.getSelectedCountryCode();
                if (countryCodePicker.getSelectedCountryCode().equals("91")){
                    lengthMobile = 10;
                    if (txtMobile.getText().toString().length() == 10) {
                        GetOtp(LoginActivity.this);
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.enterMobileNumber), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    lengthMobile = 0;
                    GetOtp(LoginActivity.this);
                }
            }
        });

        if (getIntent().getStringExtra("getMobile")!= null){
            txtMobile.setText(getIntent().getStringExtra("getMobile"));
            countrycode = getIntent().getStringExtra("getCountryCode");
            if (getIntent().getStringExtra("getCountryCode").equals("91")){
                lengthMobile = 10;
            } else {
                lengthMobile = 0;
            }
            GetOtp(LoginActivity.this);
        }

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycode = countryCodePicker.getSelectedCountryCode();
                if (countryCodePicker.getSelectedCountryCode().equals("91")){
                    lengthMobile = 10;
                } else {
                    lengthMobile = 0;
                }
                btnResend.setBackgroundResource(R.drawable.button_shape_gray);
                GetOtp(LoginActivity.this);
                btnResend.setEnabled(false);
            }
        });
        btnVerify.setEnabled(false);

        txtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtOTP.getText().length() == 4){
                    btnVerify.setEnabled(true);
                    btnVerify.setBackgroundResource(R.drawable.button_shape_orange);
                } else {
                    btnVerify.setEnabled(false);
                    btnVerify.setBackgroundResource(R.drawable.button_shape_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify(LoginActivity.this);
            }
        });

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sharedPrefsUtils.setDeviceId(ApiConstants.deviceId, android_id);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(LoginActivity.this);
                    adId = adInfo != null ? adInfo.getId() : null;
                    sharedPrefsUtils.setDeviceId(ApiConstants.gaid, adId);
                    // Use the advertising id
                } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
                    // Error handling if needed
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                gcm_id = newToken;
            }
        });
        chatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventType = "Contact Us";
                getLogEvent(LoginActivity.this, txtMobile.getText().toString().trim());
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(getResources().getString(R.string.chatURL)));
                startActivity(myIntent);
            }
        });
        getSignupData();
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtExpire.setVisibility(View.VISIBLE);
                long sec = ((millisUntilFinished / 1000) % 60);
                txtExpire.setText("OTP "+getResources().getString(R.string.expireIn)+String.format("%02d", sec)+" seconds");
            }

            @Override
            public void onFinish() {
                getBlockApi();
            }
        };
    }

    public void getBlockApi() {
        CheckStudent checkStudent = new CheckStudent("Hamstech", getResources().getString(R.string.lblApiKey),
                resMobile);

        Call<CheckStudent> call = apiService.getCheckStudent(checkStudent);
        call.enqueue(new Callback<CheckStudent>() {
            @Override
            public void onResponse(Call<CheckStudent> call, retrofit2.Response<CheckStudent> response) {
                if (response.body().getStudent().equalsIgnoreCase("no")) {
                    btnResend.setVisibility(View.VISIBLE);
                    btnResend.setEnabled(true);
                    btnResend.setBackgroundResource(R.drawable.button_shape_orange);
                    txtExpire.setVisibility(View.GONE);
                    isTimeOut = true;
                    lastNumber = resMobile.substring(resMobile.length() - 4);
                    txtOTP.setText(resMobile.substring(resMobile.length() - 4));
                    otpMessage.setVisibility(View.VISIBLE);
                    Verify(LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CheckStudent> call, Throwable t) {

            }
        });
    }


    public void getSignupData() {
        LoginSignupData loginSignupRequest = new LoginSignupData("Hamstech",getResources().getString(R.string.lblApiKey),langPref);
        Call<LoginSignupData> call = apiService.getLoginSignupData(loginSignupRequest);
        call.enqueue(new Callback<LoginSignupData>() {
            @Override
            public void onResponse(Call<LoginSignupData> call, retrofit2.Response<LoginSignupData> response) {
                try {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        Glide.with(LoginActivity.this)
                                .load(response.body().getImage_1())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgTop);
                        Glide.with(LoginActivity.this)
                                .load(response.body().getImage_2())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgBottom);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginSignupData> call, Throwable t) {

            }
        });
    }

    public void GetOtp(Context context){
        if(!ValidateInputs()) {
            return;
        } else {
            ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);
            isNet = cd.isConnectingToInternet();
            resMobile = txtMobile.getText().toString().trim();
            if (isNet) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("page", "login");
                    jsonObject.put("phone", txtMobile.getText().toString().trim());
                    jsonObject.put("countrycode", countrycode);
                    jsonObject.put("lang", langPref);
                    getLogEventLanguage(LoginActivity.this,txtMobile.getText().toString().trim());
                    postGetOtp(jsonObject, ApiConstants.getOTP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void RegisterHere(View view){
        Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void Verify(Context context) {
        hocLoadingDialog.showLoadingDialog();
        ConnectionDetector cd = new ConnectionDetector(LoginActivity.this);
        isNet = cd.isConnectingToInternet();
        resMobile = txtMobile.getText().toString().trim();
        if (isNet) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("appname", "Hamstech");
                jsonObject.put("apikey", "dsf99898398i3jofklese93");
                jsonObject.put("page", "login");
                jsonObject.put("name", "");
                jsonObject.put("phone", resMobile);
                jsonObject.put("cityName", cityName);
                jsonObject.put("user_address", getAddress);
                jsonObject.put("user_state", getState);
                jsonObject.put("user_pincode", getPincode);
                jsonObject.put("user_country", getCountryName);
                jsonObject.put("otp", txtOTP.getText().toString().trim());
                jsonObject.put("otptimestamp", otptimestamp);
                //jsonObject.put("device", "android");
                jsonObject.put("last_number", lastNumber);
                jsonObject.put("networkconnection", networkType);
                jsonObject.put("downSpeed", ""+downSpeed);
                jsonObject.put("upSpeed", ""+upSpeed);
                jsonObject.put("gcm_id", gcm_id);
                jsonObject.put("countrycode", countrycode);
                jsonObject.put("lang", langPref);
                jsonObject.put("device_id", android_id);
                jsonObject.put("ga_id", adId);

                if (isTimeOut == true) {
                    postVerify(jsonObject);
                } else {
                    if (txtOTP.getText().toString().trim().equals(resMobile.substring(resMobile.length() - 4))){
                        Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    } else {
                        postVerify(jsonObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ValidateInputs() {
        boolean result = IsValid(txtMobile, "Enter Mobile Number");
        return result;
    }

    private boolean IsValid(EditText txtText, String validationMessage) {
        if (txtText.getText().toString().trim().equals("")) {
            txtText.setError(validationMessage);
            return false;
        }
        return true;
    }

    private  void postGetOtp(JSONObject jo, String url) {
        CustomAsync ca=new CustomAsync(LoginActivity.this, jo, url, new OnAsyncCompleteRequest() {

            @Override
            public void asyncResponse(String result) {
                // TODO Auto-generated method stub
                if(result==null||result.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
                } else{
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject object = jsonObject.getJSONObject("status");
                        JSONObject objData = object.getJSONObject("metadata");
                        if (object.getString("data").equals("OTP Sent Successfully")){
                            otptimestamp = String.valueOf(objData.getInt("timestamp"));
                            eventType = "Clicked on OTP";
                            getLogEvent(LoginActivity.this,txtMobile.getText().toString().trim());
                            linOtpLayout.setVisibility(View.VISIBLE);
                            btnGetOtp.setVisibility(View.GONE);
                            btnVerify.setVisibility(View.VISIBLE);
                            txtMobile.setEnabled(false);
                            countDownTimer.start();

                        } else {
                            Toast.makeText(LoginActivity.this, ""+objData.getString("status"), Toast.LENGTH_SHORT).show();
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        ca.execute();
    }
    public void postVerify(JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String mRequestBody = jsonObject.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.verifyOTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            hocLoadingDialog.hideDialog();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("status");
                            JSONObject objData = object.getJSONObject("data");
                            if (objData.getString("OTP").equals("OTP validated Successfully")){
                                countDownTimer.cancel();
                                c = dh.getCount();
                                UserDataConstants.userName = objData.getString("name");
                                UserDataConstants.userMobile = objData.getString("phone");
                                UserDataConstants.isNewUser = false;
                                Properties properties = new Properties();
                                properties.addAttribute("name", objData.getString("name"))
                                        .addAttribute("phone", objData.getString("phone"))
                                        .addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref)
                                        .setNonInteractive();
                                MoEHelper.getInstance(LoginActivity.this).trackEvent("login",properties);
                                MoEHelper.getInstance(LoginActivity.this).setFirstName(objData.getString("name"));
                                MoEHelper.getInstance(LoginActivity.this).setNumber(objData.getString("phone"));
                                MoEHelper.getInstance(LoginActivity.this).setUniqueId(objData.getString("phone"));
                                if(c == 0) {
                                    dh.InsertUser(new UserDatamodel(objData.getString("name"),objData.getString("phone")));
                                } else {
                                    dh.updatetUserId(new UserDatamodel(objData.getString("name"),objData.getString("phone")));
                                }
                                Intent intent = new Intent(LoginActivity.this,HomePageActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                                eventType = "Clicked on Verify";
                                getLogEvent(LoginActivity.this,objData.getString("phone"));
                            } else {
                                Toast.makeText(LoginActivity.this, ""+objData.getString("message"), Toast.LENGTH_SHORT).show();
                                txtExpire.setVisibility(View.GONE);
                                btnResend.setVisibility(View.VISIBLE);
                                btnResend.setEnabled(true);
                                btnResend.setBackgroundResource(R.drawable.button_shape_orange);
                            }
                        } catch(JSONException e) {
                            hocLoadingDialog.hideDialog();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this, "No Internet Connection! Please try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Toast.makeText(LoginActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void takeCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            requestLocation();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void requestLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            try {
                provider = locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(provider);
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                StringBuilder builder = new StringBuilder();
                try {
                    List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                    int maxLines = address.get(0).getMaxAddressLineIndex();
                    for (int i=0; i<maxLines; i++) {
                        String addressStr = address.get(0).getAddressLine(i);
                        builder.append(addressStr);
                        builder.append(" ");
                    }

                    cityName = address.get(0).getLocality();
                    getPincode = address.get(0).getPostalCode();
                    getAddress = address.get(0).getAddressLine(0);
                    getState = address.get(0).getAdminArea();
                    getCountryName = address.get(0).getCountryName();

                } catch (IOException e) {
                    // Handle IOException
                } catch (NullPointerException e) {
                    // Handle NullPointerException
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        c = dh.getCount();
        if (c!=0){
            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
        }
    }

    public void  checkNetwork(){
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        try {
            networkType = networkInfo.getTypeName();
            if (!networkType.equals("WIFI")) {
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                networkType = manager.getNetworkOperatorName();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public void getLogEvent(Context context,String userMobile){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", userMobile);
            data.put("fullname","");
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson","");
            data.put("activity","Login");
            data.put("pagename",eventType);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getLogEventLanguage(Context context,String userMobile){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", userMobile);
            data.put("fullname","");
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson",gcm_id);
            data.put("activity","Before Registration/Login");
            data.put("pagename",langPref);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
