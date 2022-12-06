package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
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
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CheckStudent;
import com.hamstechonline.datamodel.LoginSignupData;
import com.hamstechonline.datamodel.UserBlockReport;
import com.hamstechonline.datamodel.UserDatamodel;
import com.hamstechonline.editprofile.datamodel.MentorResponse;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventParameter;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ConnectionDetector;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.CustomAsync;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.OnAsyncCompleteRequest;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.hbb20.CountryCodePicker;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

import static android.os.Build.VERSION_CODES.S;
import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

public class RegistrationActivity extends AppCompatActivity {

    LinearLayout linOtpLayout,chatUs;
    Button btnGetOtp, btnResend, btnVerify;
    EditText txtOTP, txtName, txtMobile;
    TextView txtExpire,otpMessage,txtLogin,txtTermsCond;
    boolean isNet,isTimeOut = false;
    String userName, resMobile, otptimestamp, cityName = "", getPincode = "", getAddress = "", getState = "",
            getCountryName = "", countrycode,networkType = "",lastNumber,selectedCity="";
    String eventType = "";
    AutoCompleteTextView txtSelectCity;
    ImageView imgTop, imgBottom;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LocationManager locationManager;
    private String provider;
    Location location;
    int lengthMobile = 0,downSpeed = 0, upSpeed = 0,c;
    String gcm_id;
    Criteria criteria;
    CountDownTimer countDownTimer;
    LogEventsActivity logEventsActivity;
    CountryCodePicker countryCodePicker;
    UserDataBase dh;
    String langPref = "Language",android_id,adId;
    Bundle params;
    Dialog dialog;
    AppEventsLogger logger;
    SharedPreferences prefs;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    List<String> categories = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration_activity);

        linOtpLayout = findViewById(R.id.linOtpLayout);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnResend = findViewById(R.id.btnResend);
        btnVerify = findViewById(R.id.btnVerify);
        txtOTP = findViewById(R.id.txtOTP);
        txtName = findViewById(R.id.txtName);
        txtMobile = findViewById(R.id.txtMobile);
        txtExpire = findViewById(R.id.txtExpire);
        countryCodePicker = findViewById(R.id.ccp);
        otpMessage = findViewById(R.id.otpMessage);
        chatUs = findViewById(R.id.chatUs);
        txtLogin = findViewById(R.id.txtLogin);
        imgTop = findViewById(R.id.imgTop);
        imgBottom = findViewById(R.id.imgBottom);
        txtTermsCond = findViewById(R.id.txtTermsCond);
        txtSelectCity = findViewById(R.id.txtSelectCity);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(this);

        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        sharedPrefsUtils = new SharedPrefsUtils(RegistrationActivity.this, getString(R.string.app_name));
        dh = new UserDataBase(this);
        dialog = new Dialog(this);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        if (countryCodePicker.getSelectedCountryCode().equals("91")) {
            countrycode = countryCodePicker.getSelectedCountryCode();
            lengthMobile = 10;
        } else {
            lengthMobile = 0;
        }

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getCityNames();

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycode = countryCodePicker.getSelectedCountryCode();
                if (!validateName()){
                    return;
                }
                if (countryCodePicker.getSelectedCountryCode().equals("91")) {
                    lengthMobile = 10;
                    if (txtMobile.getText().toString().length() == 10) {
                        GetOtp(RegistrationActivity.this);
                    } else {
                        Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.enterMobileNumber), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    lengthMobile = 0;
                    GetOtp(RegistrationActivity.this);
                }
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countrycode = countryCodePicker.getSelectedCountryCode();
                if (countryCodePicker.getSelectedCountryCode().equals("91")) {
                    lengthMobile = 10;
                } else {
                    lengthMobile = 0;
                }
                btnResend.setBackgroundResource(R.drawable.button_shape_gray);
                GetOtp(RegistrationActivity.this);
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
                if (txtOTP.getText().length() == 4) {
                    btnVerify.setEnabled(true);
                    btnVerify.setBackgroundResource(R.drawable.button_shape_orange);
                    btnVerify.setPadding(10, 0, 10, 0);
                } else {
                    btnVerify.setEnabled(false);
                    btnVerify.setBackgroundResource(R.drawable.button_shape_gray);
                    btnVerify.setPadding(10, 0, 10, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOtp(RegistrationActivity.this);
            }
        });
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        txtName.setFilters(new InputFilter[] { filter });
        txtSelectCity.setFilters(new InputFilter[] { filter });

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sharedPrefsUtils.setDeviceId(ApiConstants.deviceId, android_id);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.Info adInfo = AdvertisingIdClient.getAdvertisingIdInfo(RegistrationActivity.this);
                    adId = adInfo != null ? adInfo.getId() : null;
                    sharedPrefsUtils.setGaidId(ApiConstants.gaid, adId);
                    // Use the advertising id
                } catch (IOException | GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException exception) {
                    // Error handling if needed
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(RegistrationActivity.this, new OnSuccessListener<InstanceIdResult>() {
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
                getLogEvent(RegistrationActivity.this, txtMobile.getText().toString().trim());
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse(getResources().getString(R.string.chatURL)));
                startActivity(myIntent);
            }
        });
        txtTermsCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventType = "Terms and Conditions";
                getLogEvent(RegistrationActivity.this, txtMobile.getText().toString().trim());
                TermsConditions();
            }
        });

        getSignupData();
        txtSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = categories.get(position);
            }
        });

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtExpire.setVisibility(View.VISIBLE);
                long sec = ((millisUntilFinished / 1000) % 60);
                txtExpire.setText("OTP "+getResources().getString(R.string.expireIn)
                        +String.format("%02d", sec)+" seconds");
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
                    VerifyOtp(RegistrationActivity.this);
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
                        Glide.with(RegistrationActivity.this)
                                .load(response.body().getImage_1())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imgTop);
                        Glide.with(RegistrationActivity.this)
                                .load(response.body().getImage_2())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

    private boolean validateName() {

        if (TextUtils.isEmpty(txtName.getText().toString().trim())) {
            Toast.makeText(RegistrationActivity.this, "Name is mandatory", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txtName.getText().toString().trim().length() < 3) {
            Toast.makeText(RegistrationActivity.this, "Name should be atleast 3 Characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    public void getCityNames(){
        MentorResponse mentorRequest = new MentorResponse(langPref);
        Call<MentorResponse> call = apiService.cityNames(mentorRequest);
        call.enqueue(new Callback<MentorResponse>() {
            @Override
            public void onResponse(Call<MentorResponse> call, retrofit2.Response<MentorResponse> response) {
                for (int j = 0; j<response.body().getCities().size(); j++){
                    categories.add(response.body().getCities().get(j).getCityname());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegistrationActivity.this, R.layout.select_dialog_item, categories);
                txtSelectCity.setThreshold(2);
                txtSelectCity.setAdapter(dataAdapter);

            }

            @Override
            public void onFailure(Call<MentorResponse> call, Throwable t) {

            }
        });
    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String blockCharacterSet = "~#^|$%*!@/()-'\":;,?{}=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪:- 0123456789[]&);-):-D:-(:'(:O 1234567890";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public void GetOtp(Context context) {
        checkNetwork();
        if (!ValidateInputs()) {
            return;
        } else {
            ConnectionDetector cd = new ConnectionDetector(RegistrationActivity.this);
            isNet = cd.isConnectingToInternet();
            userName = txtName.getText().toString().trim();
            isNet = cd.isConnectingToInternet();
            resMobile = txtMobile.getText().toString().trim();
            if (isNet) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("page", "registration");
                    jsonObject.put("phone", txtMobile.getText().toString().trim());
                    jsonObject.put("countrycode", countrycode);
                    jsonObject.put("lang", langPref);
                    getLogEventLanguage(RegistrationActivity.this,txtMobile.getText().toString().trim());
                    postGetOtp(jsonObject, ApiConstants.getOTP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(RegistrationActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean ValidateInputs() {

        boolean result = IsValid(txtName, "Enter Name") &&
                IsValid(txtMobile, "Enter Mobile Number") &&
                IsValid(txtSelectCity, "Enter City");
        return result;
    }

    private boolean IsValid(EditText txtText, String validationMessage) {
        if (txtText.getText().toString().trim().equals("")) {
            txtText.setError(validationMessage);
            return false;
        }
        return true;
    }

    public void VerifyOtp(Context context) {
        ConnectionDetector cd = new ConnectionDetector(RegistrationActivity.this);
        isNet = cd.isConnectingToInternet();
        resMobile = txtMobile.getText().toString().trim();
        if (isNet) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("appname", "Hamstech");
                jsonObject.put("apikey", getResources().getString(R.string.lblApiKey));
                jsonObject.put("page", "registration");
                jsonObject.put("name", userName);
                jsonObject.put("phone", resMobile);
                jsonObject.put("cityName", txtSelectCity.getText().toString().trim());
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
                        Toast.makeText(RegistrationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    } else {
                        postVerify(jsonObject);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(RegistrationActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void postGetOtp(JSONObject jo, String url) {
        CustomAsync ca = new CustomAsync(RegistrationActivity.this, jo, url, new OnAsyncCompleteRequest() {

            @Override
            public void asyncResponse(String result) {
                if (result == null || result.equals("")) {
                    Toast.makeText(RegistrationActivity.this, "Please Retry", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject object = jsonObject.getJSONObject("status");
                        JSONObject objData = object.getJSONObject("metadata");
                        if (object.getString("data").equals("OTP Sent Successfully")) {
                            otptimestamp = String.valueOf(objData.getInt("timestamp"));
                            linOtpLayout.setVisibility(View.VISIBLE);
                            btnGetOtp.setVisibility(View.GONE);
                            btnVerify.setVisibility(View.VISIBLE);
                            txtMobile.setEnabled(false);
                            txtName.setEnabled(false);
                            countDownTimer.start();
                            eventType = "Clicked on OTP";
                            getLogEvent(RegistrationActivity.this, txtMobile.getText().toString().trim());
                        } else {
                            Toast.makeText(RegistrationActivity.this, "" + object.getString("status"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("status").equals("Phone number is already register, Please login")){
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                intent.putExtra("getMobile", txtMobile.getText().toString().trim());
                                intent.putExtra("getCountryCode", countrycode);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegistrationActivity.this, ""+jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                            }
                            e.printStackTrace();
                        } catch (Exception ex){
                            ex.printStackTrace();
                        }
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
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("status");
                            JSONObject objData = object.getJSONObject("data");
                            if (objData.getString("OTP").equals("OTP validated Successfully")){
                                countDownTimer.cancel();
                                c = dh.getCount();
                                UserDataConstants.userName = objData.getString("name");
                                UserDataConstants.userMobile = objData.getString("phone");
                                UserDataConstants.isNewUser = true;
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "New User");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                                params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, "Registration");
                                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
                                logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
                                new AppsFlyerEventsHelper(RegistrationActivity.this).EventRegistration();
                                new FacebookEventsHelper(RegistrationActivity.this).logSpendCreditsEvent("New User");
                                Properties properties = new Properties();
                                properties.addAttribute("name", objData.getString("name"))
                                        .addAttribute("phone", objData.getString("phone"))
                                        .addAttribute(AppsFlyerEventParameter.KEY_LANGUAGE,langPref)
                                        .setNonInteractive();
                                //MoEHelper.getInstance(RegistrationActivity.this).trackEvent("Registration",properties);
                                MoEHelper.getInstance(RegistrationActivity.this).setFirstName(objData.getString("name"));
                                MoEHelper.getInstance(RegistrationActivity.this).setNumber(objData.getString("phone"));
                                MoEHelper.getInstance(RegistrationActivity.this).setUniqueId(objData.getString("phone"));
                                if(c == 0) {
                                    dh.InsertUser(new UserDatamodel(objData.getString("name"),objData.getString("phone")));
                                } else {
                                    dh.updatetUserId(new UserDatamodel(objData.getString("name"),objData.getString("phone")));
                                }

                                eventType = "Clicked on Verify";
                                getLogEvent(RegistrationActivity.this,objData.getString("phone"));

                                Intent intent = new Intent(RegistrationActivity.this,HomePageActivity.class);
                                intent.putExtra("isNewRegister",true);
                                startActivity(intent);
                                RegistrationActivity.this.finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, ""+objData.getString("message"), Toast.LENGTH_SHORT).show();
                                txtExpire.setVisibility(View.GONE);
                                btnResend.setVisibility(View.VISIBLE);
                                btnResend.setEnabled(true);
                                btnResend.setBackgroundResource(R.drawable.button_shape_orange);
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(RegistrationActivity.this, "No Internet Connection! Please try again", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegistrationActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void checkNetwork(){
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

    @SuppressLint("SetJavaScriptEnabled")
    public void TermsConditions(){
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.terms_conditions_dialogue);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        final WebView webview = dialog.findViewById(R.id.mWebView);

        final ProgressBar mProgressBar = (ProgressBar) dialog.findViewById(R.id.mProgressBar);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mProgressBar.setVisibility(View.VISIBLE);

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(ApiConstants.BaseUrl+"terms-conditions"+"-"+langPref+"/");

        /*webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        *//*webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webview.setVisibility(View.VISIBLE);
            }
        });

        webview.loadUrl(ApiConstants.BaseUrl+"terms-conditions"+"-"+langPref+"/");*//*
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
            }
        });

        webview.loadUrl("http://www.teluguoneradio.com/rssHostDescr.php?hostId=147");*/

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            data.put("activity","New Register");
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

    @Override
    protected void onResume() {
        super.onResume();
        c = dh.getCount();
        if (c!=0){
            startActivity(new Intent(RegistrationActivity.this,HomePageActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
