package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.EnrolNotification;
import com.hamstechonline.datamodel.FlashOfferResponse;
import com.hamstechonline.datamodel.MiniCoursesModel;
import com.hamstechonline.datamodel.PayinstallmentRequest;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.DynamicWhatsAppChat;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class MiniLessonsEnrolNowActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        PaymentResultWithDataListener {

    BottomNavigationView navigation;
    TextView txtFinalAmount, txtViewOrderSummary;
    LinearLayout paymentOptions, txtChat, callUs, howtoPay, linearQR;
    CheckBox viewSummayDetails;
    EnrolItemAdapter enrolItemAdapter;
    selectedAdapter selectedAdapter;
    ListSelectedNames listSelectedNamesAdapter;
    RecyclerView listSummaryItems, itemsSelected, optionsList;
    TextView btnContinue;
    ArrayList<MiniCoursesModel> coursesList = new ArrayList<>();
    ArrayList<MiniCoursesModel> coursesOriginalList = new ArrayList<>();
    int selectedPlan = 1, paymentOption = 10, planNSDC = 2, mMenuId, flashInput = 1;
    private FirebaseAnalytics mFirebaseAnalytics;
    float finalAmount;
    long orderID;
    String course_type = "Preferred", selectLaguage = "", discount_percentage, orderType,
            mobile = "", fullname = "", email = "", razorpayOrderid, paymentName, offerImage, rezorOrderID;
    UserDataBase userDataBase;
    RadioGroup radioGroup;
    ArrayList<String> selectedNames = new ArrayList<>();
    ArrayList<Integer> courseIds = new ArrayList<>();
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String CategoryName, CourseLog, LessonLog = "", ActivityLog, PagenameLog, m_strEmail;
    Dialog dialog;
    String langPref = "Language", mRequestBody, selectedPayment, htmlAllTaxesString, tracking_id = "";
    SharedPreferences prefs;
    List<String> categories = new ArrayList<String>();
    List<String> enrollment_type = new ArrayList<String>();
    List<String> payment_mode = new ArrayList<String>();
    RadioButton rbCOD;
    private Locale myLocale;
    Bundle params;
    AppEventsLogger logger;
    FirebaseAnalytics firebaseAnalytics;
    SharedPrefsUtils sharedPrefsUtils;
    RadioButton btnHindi, btnEnglish;
    ApiInterface apiService;
    CountDownTimer countDownTimer;
    PaymentsOptionsAdapter paymentsOptionsAdapter;
    String[] optionNames = {"Gpay", "Paytm", "PhonePe", "BharatPe", "Debit Card/ Credit Card", "Net Banking", "Other Payments"};
    DynamicWhatsAppChat dynamicWhatsAppChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.minilessons_enrolnow_activity);

        navigation = findViewById(R.id.navigation);
        paymentOptions = findViewById(R.id.paymentOptions);
        radioGroup = findViewById(R.id.radioGroup);
        btnContinue = findViewById(R.id.btnContinue);
        rbCOD = findViewById(R.id.rbCOD);
        listSummaryItems = findViewById(R.id.listSummaryItems);
        viewSummayDetails = findViewById(R.id.viewSummayDetails);
        txtViewOrderSummary = findViewById(R.id.txtViewOrderSummary);
        txtFinalAmount = findViewById(R.id.txtFinalAmount);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnHindi = findViewById(R.id.btnHindi);
        itemsSelected = findViewById(R.id.itemsSelected);
        optionsList = findViewById(R.id.optionsList);
        txtChat = findViewById(R.id.txtChat);
        callUs = findViewById(R.id.callUs);
        howtoPay = findViewById(R.id.howtoPay);
        linearQR = findViewById(R.id.linearQR);

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        userDataBase = new UserDataBase(this);
        dialog = new Dialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        params = new Bundle();
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);
        logger = AppEventsLogger.newLogger(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharedPrefsUtils = new SharedPrefsUtils(this, getString(R.string.app_name));
        CategoryName = getIntent().getStringExtra("CategoryName");
        if (getIntent().getStringExtra("Email") != null) {
            m_strEmail = getIntent().getStringExtra("Email");
        } else m_strEmail = "no-reply@hunarcourses.com";
        if (langPref.equals("en")) {
            categories.add("English");
            categories.add("Hindi");
            selectLaguage = "English";
        } else {
            categories.add("अंग्रेज़ी");
            categories.add("हिंदी");
            selectLaguage = "Hindi";
        }

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = "";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setUserId(userDataBase.getUserMobileNumber(1));

        //validateHorizontalCourseList();

        getCategories(this);
        htmlAllTaxesString = getResources().getString(R.string.txtInclusiveAllTaxes);
        Checkout.preload(getApplicationContext());

        btnHindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    langPref = "hi";
                    selectLaguage = "Hindi";
                }
            }
        });
        btnEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    langPref = "en";
                    selectLaguage = "English";
                }
            }
        });

        paymentsOptionsAdapter = new PaymentsOptionsAdapter(MiniLessonsEnrolNowActivity.this);
        optionsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //itemLessons.addItemDecoration(new GridSpacingItemDecoration(1,0,false));
        optionsList.setAdapter(paymentsOptionsAdapter);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentOptions();
                /*if (paymentOption != 10 ) {
                    ActivityLog = optionNames[paymentOption];
                    PagenameLog = "Pay Now";
                    LessonLog = "";
                    getLogEvent(MiniLessonsEnrolNowActivity.this);
                    paymentOptions();
                } else {
                    Toast.makeText(MiniLessonsEnrolNowActivity.this, getResources().getString(R.string.choosePaymentOption), Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(group.getCheckedRadioButtonId());
                boolean isChecked = checkedRadioButton.isChecked();
                paymentOption = 0;
                if (isChecked) {
                    paymentOption = group.indexOfChild(checkedRadioButton);
                    selectedPayment = ((RadioButton) findViewById(group.getCheckedRadioButtonId()))
                            .getText().toString();

                }
            }
        });

        viewSummayDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listSummaryItems.setVisibility(View.VISIBLE);
                } else {
                    listSummaryItems.setVisibility(View.GONE);
                }
            }
        });

        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Need Help";
                PagenameLog = "Payment Page";
                LessonLog = "";
                getLogEvent(MiniLessonsEnrolNowActivity.this);
                dynamicWhatsAppChat = new DynamicWhatsAppChat(MiniLessonsEnrolNowActivity.this,"Mini lessons enrol","","");
                dynamicWhatsAppChat.getEnrollChatNumber(userDataBase.getUserMobileNumber(1));
            }
        });
        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Call us";
                PagenameLog = "Payment Page";
                LessonLog = "";
                getLogEvent(MiniLessonsEnrolNowActivity.this);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+919154257082"));
                startActivity(intent);
            }
        });
        howtoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (langPref.equalsIgnoreCase("en")) {
                    watchYoutubeVideo("YBEdiv7xtW4");
                } else if (langPref.equalsIgnoreCase("hi")) {
                    watchYoutubeVideo("oqTbPvUUNus");
                }
            }
        });
        linearQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderID = System.currentTimeMillis();
                init_QR_transactions(MiniLessonsEnrolNowActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void setLanguage(String lang) {
        if (lang.equals("en")) {
            btnEnglish.setChecked(true);
        } else if (lang.equals("hi")) {
            btnHindi.setChecked(true);
        }
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void getCategories(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        hocLoadingDialog.showLoadingDialog();
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "category");
            params.put("apikey", getResources().getString(R.string.lblApiKey));
            params.put("lang", langPref);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = params.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.getHomePageData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            coursesList.clear();
                            if (jsonObject.isNull("mini_lessons")) {

                            } else {
                                int position = (getIntent().getIntExtra("getCourseId", 0));
                                JSONArray jsonArray = jsonObject.getJSONArray("mini_lessons");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    MiniCoursesModel datamodel = new MiniCoursesModel();
                                    datamodel.setCourseId(object.getString("courseId"));
                                    datamodel.setCourse_title(object.getString("course_title"));
                                    datamodel.setCourse_description(object.getString("course_description"));
                                    datamodel.setImage_url(object.getString("image_url"));
                                    datamodel.setAmount(object.getString("amount"));
                                    datamodel.setLanguage(object.getString("language"));

                                    coursesList.add(datamodel);
                                }

                                if (getIntent().getIntExtra("getCourseId", 0) != 0) {
                                    for (int i = 0; i < coursesList.size(); i++) {
                                        if (Integer.parseInt(coursesList.get(i).getCourseId()) == getIntent().getIntExtra("getCourseId", 0)) {
                                            position = i;
                                        }
                                    }
                                    coursesOriginalList.add(coursesList.get(position));
                                    selectedNames.add(coursesList.get(position).getCourse_title());
                                    CourseLog = coursesList.get(position).getCourse_title();
                                    courseIds.add(Integer.parseInt(coursesList.get(position).getCourseId()));
                                    coursesList.remove(position);
                                }

                                selectedAdapter = new selectedAdapter(MiniLessonsEnrolNowActivity.this, position);
                                itemsSelected.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, LinearLayoutManager.VERTICAL, false));
                                itemsSelected.setAdapter(selectedAdapter);

                                listSelectedNamesAdapter = new ListSelectedNames(MiniLessonsEnrolNowActivity.this, position);
                                listSummaryItems.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                listSummaryItems.setAdapter(listSelectedNamesAdapter);
                                finalAmount = Float.parseFloat(coursesOriginalList.get(0).getAmount());
                                String htmlFinalAmount = getResources().getString(R.string.txtTotal) + "\n" + "₹" +
                                        NumberFormat.getInstance().format(finalAmount) + "/-";
                                txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

                            }
                            try {
                                setLanguage(prefs.getString("Language", "en"));
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                            }
                            if (getIntent().getStringExtra("status") != null) {

                                LessonLog = getIntent().getStringExtra("notificationTtitle");
                                PagenameLog = "Enrol Page";
                                ActivityLog = "Notification Clicked";
                                CategoryName = getIntent().getStringExtra("CategoryName");
                                //CourseLog = "";
                                getLogEvent(MiniLessonsEnrolNowActivity.this);

                            } else {
                                getEnrolNotification(String.valueOf(getIntent().getIntExtra("getCourseId", 0)));
                            }
                            hocLoadingDialog.hideDialog();
                            getMiniLessonOffer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MiniLessonsEnrolNowActivity.this, "Internet error", Toast.LENGTH_LONG).show();
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
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    Toast.makeText(MiniLessonsEnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(stringRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        CategoryName = "";
        CourseLog = "";
        LessonLog = "";
        mMenuId = item.getItemId();
        for (int i = 0; i < navigation.getMenu().size(); i++) {
            MenuItem menuItem = navigation.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }
        switch (item.getItemId()) {
            case R.id.navigation_home:
                PagenameLog = "Home Page";
                if (flashInput == 1) FlashSalePopUp();
                else {
                    params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                    logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                    Intent intentCourses = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                    startActivity(intentCourses);
                }

                return true;
            case R.id.navigation_chat:
                ActivityLog = "enrol now";
                PagenameLog = "chat with whatsapp";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                getLogEvent(MiniLessonsEnrolNowActivity.this);

                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "919010100240" + "&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    /*if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.navigation_enrol:
                PagenameLog = "Success Story";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                Intent enrol = new Intent(MiniLessonsEnrolNowActivity.this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                Intent hamstech = new Intent(MiniLessonsEnrolNowActivity.this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                Intent about = new Intent(MiniLessonsEnrolNowActivity.this, ContactActivity.class);
                startActivity(about);
                return true;
        }
        return false;
    }

    public void getEnrolNotification(String course_id) {
        EnrolNotification enrolNotification = new EnrolNotification("Hamstech", getResources().getString(R.string.lblApiKey),
                course_id, userDataBase.getUserMobileNumber(1));
        Call<EnrolNotification> call = apiService.getEnrolNotification(enrolNotification);
        call.enqueue(new Callback<EnrolNotification>() {
            @Override
            public void onResponse(Call<EnrolNotification> call, retrofit2.Response<EnrolNotification> response) {

            }

            @Override
            public void onFailure(Call<EnrolNotification> call, Throwable t) {

            }
        });
    }

    public class EnrolItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        ArrayList<MiniCoursesModel> coursesList;

        public EnrolItemAdapter(Context context, ArrayList<MiniCoursesModel> coursesList) {
            this.context = context;
            this.coursesList = coursesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.enrol_item_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                holder.txtTitle.setText(coursesList.get(position).getCourse_title());
                Glide.with(context)
                        .load(coursesList.get(position).getImage_url())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);
                holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        coursesOriginalList.add(coursesList.get(position));
                        listSelectedNamesAdapter = new ListSelectedNames(MiniLessonsEnrolNowActivity.this, position);
                        listSummaryItems.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSummaryItems.setAdapter(listSelectedNamesAdapter);
                        selectedNames.add(coursesList.get(position).getCourse_title());
                        courseIds.add(Integer.parseInt(coursesList.get(position).getCourseId()));
                        CategoryName = "";
                        CourseLog = coursesList.get(position).getCourse_title();
                        LessonLog = "";
                        ActivityLog = "Course added";
                        PagenameLog = "Enrol Page";
                        logEnrolEvent(CourseLog, ActivityLog);
                        getLogEvent(MiniLessonsEnrolNowActivity.this);

                        if (coursesOriginalList.size() == 0) {
                            planNSDC = 2;
                            selectedPlan = 1;
                        } else {
                        }
                        coursesList.remove(position);
                        if (coursesList.size() == 0) {
                            dialog.dismiss();
                        }
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtTitle, txtName, txtAmount, txtPrice, txtRemove, txtLanguage;
        CheckBox itemCheck;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgCategory = view.findViewById(R.id.imgCategory);
            txtTitle = view.findViewById(R.id.txtTitle);
            itemCheck = view.findViewById(R.id.itemCheck);
            txtName = view.findViewById(R.id.txtName);
            txtAmount = view.findViewById(R.id.txtAmount);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtRemove = view.findViewById(R.id.txtRemove);
            txtLanguage = view.findViewById(R.id.txtLanguage);
        }
    }

    public class selectedAdapter extends RecyclerView.Adapter<ViewHolder> {

        Context context;
        int courseId;

        public selectedAdapter(Context context, int courseId) {
            this.context = context;
            this.courseId = courseId;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.enrol_item_selected_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                float courseAmount = 0;
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                holder.txtTitle.setText(coursesOriginalList.get(position).getCourse_title());
                holder.txtPrice.setText("₹" + coursesOriginalList.get(position).getAmount()+ "/-");

                Glide.with(context)
                        .load(coursesOriginalList.get(position).getImage_url())
                        .placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgCategory);

                /*listSelectedNamesAdapter = new ListSelectedNames(MiniLessonsEnrolNowActivity.this);
                listSelectedNames.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, RecyclerView.VERTICAL, false));
                listSelectedNames.setAdapter(listSelectedNamesAdapter);
                listSummaryItems.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, RecyclerView.VERTICAL, false));
                listSummaryItems.setAdapter(listSelectedNamesAdapter);*/

                courseAmount = Float.parseFloat(coursesOriginalList.get(position).getAmount());
                courseAmount = Float.parseFloat(String.format("%.0f", (courseAmount)));
                String htmlString = "₹" + NumberFormat.getInstance().format(courseAmount) + "/-";
                holder.txtPrice.setText(Html.fromHtml(htmlString));
                if (coursesOriginalList.get(position).getLanguage().equalsIgnoreCase("hindi")) {
                    holder.txtLanguage.setText("हिंदी");
                    selectLaguage = "Hindi";
                } else {
                    holder.txtLanguage.setText("English");
                    selectLaguage = "English";
                }

                holder.txtRemove.setVisibility(View.GONE);

                holder.txtRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow();
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setGravity(Gravity.CENTER);
                        dialog.setContentView(R.layout.lock_popup);
                        dialog.setCancelable(true);

                        Button btnNext = dialog.findViewById(R.id.btnNext);
                        TextView txtDialogTitle = dialog.findViewById(R.id.txtDialogTitle);
                        TextView txtContent = dialog.findViewById(R.id.txtContent);
                        txtContent.setVisibility(View.GONE);
                        txtDialogTitle.setText(getResources().getString(R.string.RemoveCourse));
                        btnNext.setText(getResources().getString(R.string.lblYes));
                        txtDialogTitle.setVisibility(View.VISIBLE);
                        dialog.show();

                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                coursesList.add(coursesOriginalList.get(position));
                                CourseLog = coursesOriginalList.get(position).getCourse_title();
                                coursesOriginalList.remove(position);
                                selectedNames.remove(position);
                                enrollment_type.remove(position);
                                payment_mode.remove(position);
                                courseIds.remove(position);
                                listSelectedNamesAdapter = new ListSelectedNames(MiniLessonsEnrolNowActivity.this, position);
                                listSummaryItems.setLayoutManager(new LinearLayoutManager(MiniLessonsEnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                listSummaryItems.setAdapter(listSelectedNamesAdapter);
                                CategoryName = "";
                                LessonLog = "";
                                ActivityLog = "Course removed";
                                PagenameLog = "Enrol Page";
                                logEnrolEvent(CourseLog, ActivityLog);
                                getLogEvent(MiniLessonsEnrolNowActivity.this);

                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesOriginalList.size();
        }
    }

    public void paymentOptions() {
        orderID = System.currentTimeMillis();
        hocCreateRequest();
        orderType = "online";

        init_transactions(this);
    }

    public class ListSelectedNames extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        int position;

        public ListSelectedNames(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.selecte_names_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtName.setText(coursesOriginalList.get(position).getCourse_title());
                holder.txtAmount.setText("₹" + NumberFormat.getInstance().format(Float.parseFloat(coursesOriginalList.get(position).getAmount())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesOriginalList.size();
        }
    }

    public void hocCreateRequest() {
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        if (selectedNames.size() > 1) {
            discount_percentage = "10";
        } else {
            discount_percentage = "0";
        }
        try {
            params.put("name", userDataBase.getUserName(1));
            params.put("email", UserDataConstants.userMail);
            params.put("mobile", userDataBase.getUserMobileNumber(1));
            params.put("country", UserDataConstants.userCountryName);
            params.put("state", "");
            params.put("city", "");
            params.put("address", "");
            params.put("pincode", "");
            params.put("course", new JSONArray(selectedNames));
            params.put("courseIds", new JSONArray(courseIds));
            params.put("course_type", course_type);
            params.put("language", selectLaguage);
            params.put("amount", finalAmount);
            params.put("order_id", orderID);
            params.put("status", "Success");
            params.put("discount_percentage", discount_percentage);
            params.put("gst", "");
            params.put("discount_amount", "");
            params.put("skill_id", "");
            metaData.put("data", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestBody = params.toString();
    }

    public void createOnineOrder(Context context) {
        JSONObject params = new JSONObject();
        if (selectedNames.size() > 1) {
            discount_percentage = "10";
        } else {
            discount_percentage = "0";
        }
        try {
            params.put("name", userDataBase.getUserName(1));
            params.put("email", m_strEmail);
            params.put("mobile", userDataBase.getUserMobileNumber(1));
            params.put("country", "");
            params.put("state", "");
            params.put("city", "");
            params.put("address", "");
            params.put("pincode", "");
            params.put("course", new JSONArray(selectedNames));
            params.put("courseIds", new JSONArray(courseIds));
            params.put("course_type", course_type);
            params.put("language", selectLaguage);
            params.put("amount", String.valueOf(finalAmount));
            params.put("discount_percentage", discount_percentage);
            params.put("gst", "");
            params.put("discount_amount", "");
            params.put("skill_id", "");
            params.put("enrollment_type", new JSONArray(enrollment_type));
            params.put("payment_mode", new JSONArray(payment_mode));
            params.put("total_amount", String.valueOf(finalAmount));
            mRequestBody = params.toString();
            startPayment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init_transactions(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        if (selectedNames.size() > 1) {
            discount_percentage = "10";
        } else {
            discount_percentage = "0";
        }
        CourseLog = selectedNames.get(0);
        ActivityLog = "Pay Now";
        PagenameLog = "Enrol Page";
        LessonLog = "";
        getLogEvent(MiniLessonsEnrolNowActivity.this);
        try {
            params.put("apikey", getResources().getString(R.string.lblApiKey));
            params.put("appname", "Hamstech");
            params.put("phone", userDataBase.getUserMobileNumber(1));
            params.put("page", "PaymentPage");
            params.put("courseid", courseIds.toString().substring(1, (courseIds.toString().length()) - 1));
            params.put("course_language", selectLaguage);
            params.put("amount", finalAmount);
            params.put("discount_percentage", discount_percentage);
            params.put("gst_amount", "");
            params.put("discount_amount", "");
            params.put("course_type", course_type);
            params.put("amount_type", "fullAmount");
            params.put("skill_set", "");
            params.put("orderid", orderID);
            params.put("ordertype", orderType);
            params.put("order_amount", finalAmount);
            params.put("billing_address", "");
            params.put("billing_city", "");
            params.put("billing_email", "");
            params.put("billing_country", "");
            params.put("billing_pincode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.init_transactions, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    JSONObject jsonObject = object.getJSONObject("message");
                    JSONObject jsonOrderId = object.getJSONObject("data");
                    if (jsonObject.getString("status_message").equals("success")) {
                        razorpayOrderid = jsonOrderId.getString("razorpay_order_id");
                        createOnineOrder(MiniLessonsEnrolNowActivity.this);
                        hocLoadingDialog.hideDialog();
                    } else {
                        Toast.makeText(MiniLessonsEnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    Toast.makeText(MiniLessonsEnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void init_QR_transactions(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        if (selectedNames.size() > 1) {
            discount_percentage = "10";
        } else {
            discount_percentage = "0";
        }
        hocLoadingDialog.showLoadingDialog();
        CourseLog = selectedNames.get(0);
        ActivityLog = "QR Scan";
        PagenameLog = "Enrol Page";
        LessonLog = "";
        getLogEvent(MiniLessonsEnrolNowActivity.this);
        try {
            params.put("apikey", getResources().getString(R.string.lblApiKey));
            params.put("appname", "Hamstech");
            params.put("phone", UserDataConstants.userMobile);
            params.put("page", "PaymentPage");
            params.put("courseid", courseIds.toString().substring(1, (courseIds.toString().length()) - 1));
            params.put("course_language", selectLaguage);
            params.put("amount", finalAmount);
            params.put("discount_percentage", discount_percentage);
            params.put("gst_amount", "");
            params.put("discount_amount", "");
            params.put("course_type", course_type);
            params.put("skill_set", "");
            params.put("orderid", orderID);
            params.put("ordertype", orderType);
            params.put("order_amount", finalAmount);
            params.put("billing_address", "");
            params.put("billing_city", "");
            params.put("billing_email", "");
            params.put("billing_country", "");
            params.put("billing_pincode", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.init_QR_transactions, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    JSONObject jsonObject = object.getJSONObject("message");
                    JSONObject jsonOrderId = object.getJSONObject("data");
                    if (jsonObject.getString("status_message").equals("success")) {
                        razorpayOrderid = jsonOrderId.getString("razorpay_order_id");
                        //createOnineOrder(MiniLessonsEnrolNowActivity.this);
                        SuccessQRcode(jsonOrderId.getString("response"));
                        hocLoadingDialog.hideDialog();
                    } else {
                        Toast.makeText(MiniLessonsEnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    hocLoadingDialog.hideDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hocLoadingDialog.hideDialog();
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
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    Toast.makeText(MiniLessonsEnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void ViewAllCources(View view) {
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.courses_dialogue);
        dialog.setCancelable(true);

        RecyclerView listItems = dialog.findViewById(R.id.listItems);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        enrolItemAdapter = new EnrolItemAdapter(MiniLessonsEnrolNowActivity.this, coursesList);
        listItems.setLayoutManager(new GridLayoutManager(this, 2));
        listItems.addItemDecoration(new GridSpacingItemDecoration(2, 30, false));
        listItems.setAdapter(enrolItemAdapter);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void SuccessfulPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.successfull_layout);
        dialog.setCancelable(false);
        dialog.show();

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);

        onlinePaymentLayout.setVisibility(View.GONE);
        cod_layout.setVisibility(View.VISIBLE);

        Glide.with(MiniLessonsEnrolNowActivity.this)
                .asGif()
                .load(R.drawable.successfull_gif)
                .into(progressBar);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void CallMethod(View view) {
        logEnrolEvent("Call Us", "Enrol page");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getResources().getString(R.string.lblContactus)));
        startActivity(intent);
    }

    public class PaymentsOptionsAdapter extends RecyclerView.Adapter<PaymentsOptionsAdapter.ViewHolder> {

        Context context;
        int[] optionIcons = {R.drawable.ic_gpay, R.drawable.ic_paytm, R.drawable.ic_phonepe, R.drawable.ic_baratpay,
                R.drawable.ic_debit_card, R.drawable.ic_netbanking, R.drawable.ic_otherpayment};

        //int paymentOption = 100;

        public PaymentsOptionsAdapter(Context context) {
            this.context = context;

            //branchNames = Arrays.asList(context.getResources().getStringArray(R.array.branchNames));
            //branchAddress = Arrays.asList(context.getResources().getStringArray(R.array.branchAddress));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.payments_options_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.txtTitle.setText(optionNames[position]);
                Glide.with(context)

                        .load(optionIcons[position])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgLock);
                holder.btnOption.setEnabled(false);
                if (paymentOption == position) {
                    holder.btnOption.setChecked(true);
                } else holder.btnOption.setChecked(false);

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentOption = position;
                        paymentName = optionNames[position];
                        //holder.txtTitle.setChecked(true);
                        notifyDataSetChanged();
                    }
                });
                holder.txtTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentOption = position;
                        //holder.txtTitle.setChecked(true);
                        notifyDataSetChanged();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            //return branchNames.size();
            return optionNames.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            ImageView imgLock;
            RadioButton btnOption;
            RelativeLayout mainLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                txtTitle = view.findViewById(R.id.txtTitle);
                imgLock = view.findViewById(R.id.imgLock);
                mainLayout = view.findViewById(R.id.mainLayout);
                btnOption = view.findViewById(R.id.btnOption);
            }
        }
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            /*try {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", 12000); // amount in the smallest currency unit
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "order_rcptid_11");

                Order order = razorpay.Orders.create(orderRequest);
            } catch (RazorpayException e) {
                // Handle Exception
                System.out.println(e.getMessage());
            }*/
            //finalAmount = 1;
            String vAmount = String.format("%.0f", finalAmount);
            vAmount = vAmount + "00";
            JSONObject options = new JSONObject();

            options.put("name", "Hunar");
            options.put("description", "Online Courses");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://res.cloudinary.com/hamstechonline-com/image/upload/f_auto,q_auto/v1615006126/hunar%20website%20images/Website_lkujhk.jpg");
            options.put("currency", "INR");
            options.put("amount", vAmount);
           // options.put("invoice_number", "inv_KQyyZZXNQRMZxj");
            //options.put("receipt", "inv_KQyyZZXNQRMZxj");
            options.put("order_id", razorpayOrderid);
            //options.put("wallet", "0");

            //options.put("receipt", "txn_123456");
            //Order order = razorpayClient.Orders.create(options)

            JSONObject preFill = new JSONObject();
            if (UserDataConstants.userMail != null) {
                if (UserDataConstants.userMail.equalsIgnoreCase("")) {
                    preFill.put("email", "no-reply@hunarcourses.com");
                } else preFill.put("email", UserDataConstants.userMail);
            } else {
                preFill.put("email", "no-reply@hunarcourses.com");
            }

            preFill.put("email", m_strEmail);
            preFill.put("contact", mobile);
            //preFill.put("method", "upi");
            //preFill.put("vpa", "");

            options.put("prefill", preFill);

            //OnlineSuccessfulPopUp();
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        try {
            tracking_id = razorpayPaymentID;
            rezorOrderID = paymentData.getOrderId();
            PaymentSuccessAPi(tracking_id);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            //Toast.makeText(this, "Payment error: "+"  " + s, Toast.LENGTH_SHORT).show();
            OnlineFailurePopUp();
        } catch (Exception e) {

        }
    }

    public void PaymentSuccessAPi(String tracking_id) {
        PaymentSuccessResponse paymentSuccessResponse = new PaymentSuccessResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1), String.valueOf(orderID), tracking_id);
        Call<PaymentSuccessResponse> call = apiService.getPaymentSuccess(paymentSuccessResponse);
        call.enqueue(new Callback<PaymentSuccessResponse>() {
            @Override
            public void onResponse(Call<PaymentSuccessResponse> call, retrofit2.Response<PaymentSuccessResponse> response) {
                if (response.body().getMesssage().equalsIgnoreCase("Payment updated successfully"))
                    OnlineSuccessfulPopUp();
                else
                    Toast.makeText(MiniLessonsEnrolNowActivity.this, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {

            }
        });
    }

    public void OnlineSuccessfulPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_sucess);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);

        onlinePaymentLayout.setVisibility(View.VISIBLE);
        cod_layout.setVisibility(View.GONE);

        CourseLog = new JSONArray(selectedNames).toString();
        LessonLog = "";
        ActivityLog = "Payment Success";
        PagenameLog = "Enroll Page";
        getLogEvent(MiniLessonsEnrolNowActivity.this);

        Glide.with(MiniLessonsEnrolNowActivity.this)
                .load(R.drawable.ic_sucess_payment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_payment)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                dialog.dismiss();
                startActivity(intent);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void SuccessQRcode(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_sucess);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);
        TextView paymentComment = dialog.findViewById(R.id.paymentComment);

        onlinePaymentLayout.setVisibility(View.VISIBLE);
        cod_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        paymentComment.setText(msg);

        CourseLog = new JSONArray(selectedNames).toString();
        LessonLog = "";
        ActivityLog = "Payment Scan code Success";
        PagenameLog = "Enroll Page";
        getLogEvent(MiniLessonsEnrolNowActivity.this);

        Glide.with(MiniLessonsEnrolNowActivity.this)
                .load(R.drawable.ic_sucess_payment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_payment)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void OnlineFailurePopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_sucess);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);
        TextView paymentComment = dialog.findViewById(R.id.paymentComment);

        onlinePaymentLayout.setVisibility(View.VISIBLE);
        cod_layout.setVisibility(View.GONE);

        Glide.with(MiniLessonsEnrolNowActivity.this)
                .load(R.drawable.ic_failed_paymennt)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_failed_paymennt)
                .into(progressBar);

        paymentComment.setText("Payment Failure,\nPlease Try Again!");
        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ActivityLog = "Payment Failure";
                PagenameLog = "Enroll Page";
                getLogEvent(MiniLessonsEnrolNowActivity.this);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void FlashSalePopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.flash_sale_popup);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView imgOfferImage = dialog.findViewById(R.id.imgOfferImage);
        TextView txtActualAmount = dialog.findViewById(R.id.txtActualAmount);
        TextView txtOfferTime = dialog.findViewById(R.id.txtOfferTime);
        TextView btnEnrol = dialog.findViewById(R.id.btnEnrol);

        Glide.with(MiniLessonsEnrolNowActivity.this)
                .load(offerImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgOfferImage);

        txtActualAmount.setText("₹ 499");
        txtActualAmount.setPaintFlags(txtActualAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ActivityLog = "Offer Enroll Cancel";
                PagenameLog = "Enroll Page";
                countDownTimer.cancel();
                getLogEvent(MiniLessonsEnrolNowActivity.this);
                flashInput = 2;
            }
        });

        countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtOfferTime.setVisibility(View.VISIBLE);
                //long sec = ((millisUntilFinished / 1000) % 60);
                long sec = millisUntilFinished / 1000;
                long Minutes = (millisUntilFinished / (60 * 1000)) % 60;
                sec = sec % 60;
                txtOfferTime.setText("Offer Ends in " + String.format("%02d", Minutes)
                        + ":" + String.format("%02d", sec) + " mins");
            }

            @Override
            public void onFinish() {
                flashInput = 2;
                dialog.dismiss();
            }
        };

        countDownTimer.start();

        btnEnrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                flashInput = 2;
                ActivityLog = "Offer Enroll Button";
                PagenameLog = "Enroll Page";
                countDownTimer.cancel();
                finalAmount = 399;
                String htmlFinalAmount = getResources().getString(R.string.txtTotal) + "\n" + "₹" +
                        NumberFormat.getInstance().format(finalAmount) + "/-";
                txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));
                getLogEvent(MiniLessonsEnrolNowActivity.this);
                paymentOptions();

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    Intent intent = new Intent(MiniLessonsEnrolNowActivity.this, HomePageActivity.class);
                    //startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void getMiniLessonOffer() {
        FlashOfferResponse flashOfferResponse = new FlashOfferResponse("Hamstech", getResources().getString(R.string.lblApiKey));
        Call<FlashOfferResponse> call = apiService.getFlashOfferResponse(flashOfferResponse);
        call.enqueue(new Callback<FlashOfferResponse>() {
            @Override
            public void onResponse(Call<FlashOfferResponse> call, retrofit2.Response<FlashOfferResponse> response) {
                if (langPref.equalsIgnoreCase("en")) {
                    offerImage = response.body().getEnglish();
                } else if (langPref.equalsIgnoreCase("hi")) {
                    offerImage = response.body().getHindi();
                }
            }

            @Override
            public void onFailure(Call<FlashOfferResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (flashInput == 1) FlashSalePopUp();
        else super.onBackPressed();
    }

    public void saveLocale(String lang) {
        SharedPreferences prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Language", lang);
        editor.commit();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    public void logEnrolEvent(String event, String action) {
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, event);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, courseIds.toString());
        //params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, action);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, String.valueOf(finalAmount));
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);
        params.putString(FirebaseAnalytics.Param.CURRENCY, String.valueOf(finalAmount));
        params.putString(FirebaseAnalytics.Param.ITEM_LIST, courseIds.toString());
        params.putString(FirebaseAnalytics.Param.VALUE, event);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(event);
    }

    public void getLogEvent(Context context) {
        JSONObject data = new JSONObject();
        try {
            data.put("apikey", context.getResources().getString(R.string.lblApiKey));
            data.put("appname", "Dashboard");
            data.put("mobile", mobile);
            data.put("fullname", fullname);
            data.put("email", email);
            data.put("category", CategoryName);
            data.put("course", CourseLog);
            data.put("lesson", LessonLog);
            data.put("activity", ActivityLog);
            data.put("pagename", PagenameLog);
            logEventsActivity.LogEventsActivity(context, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}