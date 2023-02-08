package com.hamstechonline.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.hamstechonline.datamodel.CalculateCoursePayment;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class EnrolNowActivity extends AppCompatActivity implements
        PaymentResultWithDataListener {

    TextView amountPreferred, amountPremium, txtPreferredTitle, txtPremiumTitle, txtDiscountAmount, txtTaxesAmount, txtGrandTotal,
            txtAddCourse, txtFinalAmount, txtViewOrderSummary, txtTankyouChoose, txtDiscountText, paymentTotal, paymentDiscountAmount,
            amounttotal,txtInstalment,txtSecondInstalment,txtAddDiscount;
    LinearLayout linearBeforePayment, linearAfterPayment, linearBill, onlineDiscountLayout;
    LinearLayout linearPreferred, linearPremium;
    LinearLayout linearonlinepay, linearcashdelevery;
    RelativeLayout listCourse, paymentOptions, courseLayout, relativeDiscount;
    TextView txtTermsCond, txtDiscount;
    TextView headerTitle;
    CheckBox checkboxNSDC, viewSummayDetails;
    EnrolItemAdapter enrolItemAdapter;
    selectedAdapter selectedAdapter;
    ListSelectedNames listSelectedNamesAdapter;
    allSelectedCourses allSelectedCourses;
    RecyclerView listItems, itemsSelected, selectedItems, listSelectedNames, listSummaryItems;
    Button btnContinue, btnNext, btnStartLearn;
    Spinner spnSelectLanguage;
    LinearLayout linCourses, linSelectedItem;
    ArrayList<CategoryDatamodel> coursesList = new ArrayList<>();
    ArrayList<CategoryDatamodel> coursesOriginalList = new ArrayList<>();
    int selectedPlan = 1, paymentOption = 10, dicount_value, gst_value, layout_type = 1001, planNSDC = 2, mMenuId,
            paymentmod = -1,additional_course_discount;
    TextView txtChatUs;
    private FirebaseAnalytics mFirebaseAnalytics;
    float normal_amount, amount, premiumCost, discount_amount, full_amount, gst_amount, finalAmount, Actual_amount,
            instalment_amount, second_instalment;
    long orderID;
    String course_type = "Preferred", selectLaguage = "", discount_percentage, skillIdString, orderType,
            mobile = "", fullname = "", email = "",amount_type = "fullAmount";
    UserDataBase userDataBase;
    RadioGroup radioGroup;
    ArrayList<String> selectedNames = new ArrayList<>();
    ArrayList<Integer> courseIds = new ArrayList<>();
    String[] skillIds;
    EditText txtName, txtPhone, txtAddress, txtPincode, txtCity, txtEmail, txtState, txtCountry;
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String CategoryName, CourseLog, LessonLog, ActivityLog, PagenameLog;
    Dialog dialog;
    String langPref = "Language", mRequestBody, selectedPayment, htmlAllTaxesString, tracking_id = "",razorpayOrderid,footerMenuStatus;
    SharedPreferences prefs,footerStatus;
    List<String> categories = new ArrayList<String>();
    List<String> enrollment_type = new ArrayList<String>();
    List<String> payment_mode = new ArrayList<String>();
    RadioButton rbCOD;
    private Locale myLocale;
    Bundle params;
    AppEventsLogger logger;
    FirebaseAnalytics firebaseAnalytics;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    ImageButton stickyWhatsApp;

    private String m_strEmail = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.enrolnow_activity_latest);

        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Email"))
                m_strEmail = savedInstanceState.getString("Email");
        }

        listItems = findViewById(R.id.listItems);
        spnSelectLanguage = findViewById(R.id.spnSelectLanguage);
        linCourses = findViewById(R.id.linCourses);
        linSelectedItem = findViewById(R.id.linSelectedItem);
        txtChatUs = findViewById(R.id.txtChatUs);
        itemsSelected = findViewById(R.id.itemsSelected);
        linearPreferred = findViewById(R.id.linearPreferred);
        linearPremium = findViewById(R.id.linearPremium);
        amountPreferred = findViewById(R.id.amountPreferred);
        amountPremium = findViewById(R.id.amountPremium);
        txtInstalment = findViewById(R.id.txtInstalment);
        txtTermsCond = findViewById(R.id.txtTermsCond);
        linearBeforePayment = findViewById(R.id.linearBeforePayment);
        linearAfterPayment = findViewById(R.id.linearAfterPayment);
        listCourse = findViewById(R.id.listCourse);
        paymentOptions = findViewById(R.id.paymentOptions);
        courseLayout = findViewById(R.id.courseLayout);
        txtDiscount = findViewById(R.id.txtDiscount);
        selectedItems = findViewById(R.id.selectedItems);
        radioGroup = findViewById(R.id.radioGroup);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtAddress = findViewById(R.id.txtHouseno);
        txtPincode = findViewById(R.id.txtPincode);
        txtCity = findViewById(R.id.txtCity);
        txtState = findViewById(R.id.txtState);
        txtCountry = findViewById(R.id.txtCountry);
        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(email);
        btnContinue = findViewById(R.id.btnContinue);
        txtPreferredTitle = findViewById(R.id.txtPreferredTitle);
        txtPremiumTitle = findViewById(R.id.txtPremiumTitle);
        listSelectedNames = findViewById(R.id.listSelectedNames);
        linearBill = findViewById(R.id.linearBill);
        relativeDiscount = findViewById(R.id.relativeDiscount);
        txtDiscountAmount = findViewById(R.id.txtDiscountAmount);
        txtGrandTotal = findViewById(R.id.txtGrandTotal);
        txtTaxesAmount = findViewById(R.id.txtTaxesAmount);
        checkboxNSDC = findViewById(R.id.checkboxNSDC);
        rbCOD = findViewById(R.id.rbCOD);
        txtAddCourse = findViewById(R.id.txtAddCourse);
        listSummaryItems = findViewById(R.id.listSummaryItems);
        viewSummayDetails = findViewById(R.id.viewSummayDetails);
        txtViewOrderSummary = findViewById(R.id.txtViewOrderSummary);
        txtFinalAmount = findViewById(R.id.txtFinalAmount);
        txtTankyouChoose = findViewById(R.id.txtTankyouChoose);
        txtDiscountText = findViewById(R.id.txtDiscountText);
        onlineDiscountLayout = findViewById(R.id.onlineDiscountLayout);
        paymentTotal = findViewById(R.id.paymentTotal);
        paymentDiscountAmount = findViewById(R.id.paymentDiscountAmount);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        headerTitle = findViewById(R.id.headerTitle);
        linearonlinepay = findViewById(R.id.linearonlinepay);
        linearcashdelevery = findViewById(R.id.linearcashdelevery);
        txtInstalment = findViewById(R.id.txtInstalment);
        txtSecondInstalment = findViewById(R.id.txtSecondInstalment);
        txtAddDiscount = findViewById(R.id.txtAddDiscount);

        amounttotal = findViewById(R.id.amounttotal);

        btnNext = findViewById(R.id.btnNext);

        btnStartLearn = findViewById(R.id.btnstartlearn);
        btnStartLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidateInputs()) {
                    learnSuccess();

                    /*Toast.makeText(EnrolNowActivity.this, "service pending", Toast.LENGTH_SHORT).show();
                    createOnineOrder(EnrolNowActivity.this);*/
                }
            }
        });

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        txtPhone.setEnabled(false);

        userDataBase = new UserDataBase(this);
        dialog = new Dialog(this);
        params = new Bundle();
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);
        logger = AppEventsLogger.newLogger(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        sharedPrefsUtils = new SharedPrefsUtils(this, getString(R.string.app_name));
        apiService = ApiClient.getClient().create(ApiInterface.class);
        if (langPref.equals("en")) {
            categories.add("English");
            categories.add("Hindi");
        } else {
            categories.add("अंग्रेज़ी");
            categories.add("हिंदी");
        }

        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = UserDataConstants.userMail;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectLanguage.setAdapter(dataAdapter);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setUserId(userDataBase.getUserMobileNumber(1));

        linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
        linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
//        txtPreferredTitle.setTextColor(getResources().getColor(R.color.black_two));
//        amountPreferred.setTextColor(getResources().getColor(R.color.black_two));
//        txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));

        linearonlinepay.setBackgroundResource(R.drawable.unselect_payment_method);
        linearcashdelevery.setBackgroundResource(R.drawable.unselect_payment_method);

        validateHorizontalCourseList();

        getCategories(this);
        htmlAllTaxesString = getResources().getString(R.string.txtInclusiveAllTaxes);
        Checkout.preload(getApplicationContext());

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Enrol page").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Enrol page")
                    .commit();
        }

        linearPreferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coursesOriginalList.size() != 0) {
                    amount_type = "fullAmount";
                    selectedPlan = 1;
                    course_type = "Preferred";
                    logEnrolEvent(course_type, "Selected");
                    linearPreferred.setBackgroundResource(R.drawable.payment_method_blue_shadow);
                    linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                    //txtPreferredTitle.setTextColor(getResources().getColor(R.color.white));
                    //amountPreferred.setTextColor(getResources().getColor(R.color.white));
                    //txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));
                    //amountPremium.setTextColor(getResources().getColor(R.color.black_two));
                    listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                    listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                    listSelectedNames.setAdapter(listSelectedNamesAdapter);
                    listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                    listSummaryItems.setAdapter(listSelectedNamesAdapter);
                    LessonLog = "Payment Plan";
                    ActivityLog = "fullAmount";
                    PagenameLog = "Enrol page";
                    getLogEvent(EnrolNowActivity.this);
                    getTotal(coursesOriginalList);
                    //if (paymentmod != -1) paymentmod = -1;
                }
            }
        });

        linearPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coursesOriginalList.size() != 0) {
                    amount_type = "part";
                    selectedPlan = 2;
                    course_type = "Premium";
                    logEnrolEvent(course_type, "Selected");
                    linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
                    linearPremium.setBackgroundResource(R.drawable.payment_method_blue_shadow);
                    //txtPreferredTitle.setTextColor(getResources().getColor(R.color.black_two));
                    //amountPreferred.setTextColor(getResources().getColor(R.color.black_two));
                    //txtPremiumTitle.setTextColor(getResources().getColor(R.color.white));
                    //amountPremium.setTextColor(getResources().getColor(R.color.white));
                    listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                    listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                    listSelectedNames.setAdapter(listSelectedNamesAdapter);
                    listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                    listSummaryItems.setAdapter(listSelectedNamesAdapter);
                    LessonLog = "Payment Plan";
                    ActivityLog = "Installment";
                    PagenameLog = "Enrol page";
                    getLogEvent(EnrolNowActivity.this);
                    getTotal(coursesOriginalList);

                    //if (paymentmod != -1) paymentmod = -1;
                }
            }
        });

        linearonlinepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearonlinepay.setBackgroundResource(R.drawable.cod_blue_shadow);
                linearcashdelevery.setBackgroundResource(R.drawable.extra_discount_bg_shadow);


                paymentOption = 1;
                String payamount = getResources().getString(R.string.paynow);
                String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal);
                if (selectedPlan == 1) {
                    payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)));
                    //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + " ");
                    htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                            NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + "/-";
                } else {
                    //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(premiumCost - (premiumCost * 0.05)) + " ");
                    payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)));
                    htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                            NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + "/-";
                }

                btnNext.setText(payamount);


                txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

                btnNext.setBackgroundResource(R.drawable.payment_blue_strok);
                btnNext.setEnabled(true);

                paymentmod = 0;
                LessonLog = "Payment option";
                ActivityLog = "Online";
                PagenameLog = "Enrol page";
                getLogEvent(EnrolNowActivity.this);
            }
        });

        linearcashdelevery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearonlinepay.setBackgroundResource(R.drawable.extra_discount_bg_shadow);
                linearcashdelevery.setBackgroundResource(R.drawable.cod_blue_shadow);

                String payamount = getResources().getString(R.string.paynow);
                //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount) + " ");
                payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount));
                btnNext.setText(payamount);

                String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                        NumberFormat.getInstance().format(finalAmount) + "/-";
                txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

                btnNext.setBackgroundResource(R.drawable.payment_blue_strok);
                btnNext.setEnabled(true);
                paymentmod = 1;
                LessonLog = "Payment option";
                ActivityLog = "COD";
                PagenameLog = "Enrol page";
                getLogEvent(EnrolNowActivity.this);
            }
        });

        checkboxNSDC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    planNSDC = 2;
                    if (coursesOriginalList.size() != 0) {
                        listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                        listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSelectedNames.setAdapter(listSelectedNamesAdapter);
                        listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSummaryItems.setAdapter(listSelectedNamesAdapter);
                        getTotal(coursesOriginalList);
                    }
                } else {
                    planNSDC = 1;
                    if (coursesOriginalList.size() != 0) {
                        listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                        listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSelectedNames.setAdapter(listSelectedNamesAdapter);
                        listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSummaryItems.setAdapter(listSelectedNamesAdapter);
                        getTotal(coursesOriginalList);
                    }
                }
            }
        });

        txtTermsCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logEnrolEvent("Terms & Conditions", "Enrol page");
                TermsConditions();
            }
        });
        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "919010100240" + "&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        spnSelectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectLaguage = spnSelectLanguage.getSelectedItem().toString();
                if (spnSelectLanguage.getSelectedItem().toString().equals("English")) {
                    langPref = "en";
                } else if (spnSelectLanguage.getSelectedItem().toString().equals("Hindi")) {
                    langPref = "hi";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                    if (paymentOption < 4) {
                        onlineDiscountLayout.setVisibility(View.VISIBLE);
                        if (onlineDiscountLayout.getVisibility() == View.VISIBLE) {
                            paymentTotal.setText("₹" + NumberFormat.getInstance().format(finalAmount));
                            paymentDiscountAmount.setText("₹" + NumberFormat.getInstance().format((finalAmount * 0.05)));
                        }
                        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                                NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + "/-";
                        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));
                    } else {
                        onlineDiscountLayout.setVisibility(View.GONE);
                        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                                NumberFormat.getInstance().format(finalAmount) + "/-";
                        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));
                    }
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidateInputs()) {
                } else {
                    String email = txtEmail.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
                    if (email.matches(emailPattern)) {
                        if (paymentOption <= 4) {
                            getSkillId(EnrolNowActivity.this);
                        } else {
                            Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.choosePaymentOption), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                    }
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            updatetv_back_ground();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }


    private void updatetv_back_ground() {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    boolean ispink = false;

    private void updateTextView() {
        TextView tv_5percent = findViewById(R.id.tv_5percent);

        if (ispink) {
            tv_5percent.setBackgroundColor(getResources().getColor(R.color.muted_blue));
            ispink = false;
        } else {
            tv_5percent.setBackgroundColor(getResources().getColor(R.color.dark_pink));
            ispink = true;
        }

    }

    public void onlinePay() {
        linearonlinepay.setBackgroundResource(R.drawable.cod_blue_shadow);
        linearcashdelevery.setBackgroundResource(R.drawable.extra_discount_bg_shadow);


        paymentOption = 1;
        String payamount = getResources().getString(R.string.paynow);
        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal);
        if (selectedPlan == 1) {
            payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)));
            //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + " ");
            htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                    NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + "/-";
        } else {
            //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(premiumCost - (premiumCost * 0.05)) + " ");
            payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)));
            htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                    NumberFormat.getInstance().format(finalAmount - (finalAmount * 0.05)) + "/-";
        }

        btnNext.setText(payamount);


        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

        btnNext.setBackgroundResource(R.drawable.payment_blue_strok);
        btnNext.setEnabled(true);

        paymentmod = 0;
    }

    public void CODpay() {
        linearonlinepay.setBackgroundResource(R.drawable.extra_discount_bg_shadow);
        linearcashdelevery.setBackgroundResource(R.drawable.cod_blue_shadow);

        String payamount = getResources().getString(R.string.paynow);
        //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount) + " ");
        payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount));
        btnNext.setText(payamount);

        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" +
                NumberFormat.getInstance().format(finalAmount) + "/-";
        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

        btnNext.setBackgroundResource(R.drawable.payment_blue_strok);
        btnNext.setEnabled(true);
        paymentmod = 1;
    }

    public void setLanguage(String lang) {
        if (lang.equals("en")) {
            spnSelectLanguage.setSelection(0);
        } else if (lang.equals("hi")) {
            spnSelectLanguage.setSelection(1);
        }
    }

    private boolean ValidateInputs() {
        if (txtPincode.getText().toString().trim().length() == 6) {
            boolean result = IsValid(txtName, getResources().getString(R.string.lblName))
                    && IsValid(txtPhone, getResources().getString(R.string.enterMobileNumber))
                    && IsValid(txtAddress, getResources().getString(R.string.Address))
                    && IsValid(txtPincode, getResources().getString(R.string.Pincode))
                    && IsValid(txtCity, getResources().getString(R.string.City));
            return result;
        } else if (txtPincode.getText().toString().trim().length() == 0) {
            boolean result = IsValid(txtPincode, getResources().getString(R.string.Pincode));
            return result;
        } else if (txtPincode.getText().toString().trim().length() < 6) {
            Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.validPincode), Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private boolean IsValid(EditText txtText, String validationMessage) {
        if (txtText.getText().toString().trim().equals("")) {
            txtText.setError(validationMessage);
            return false;
        }
        return true;
    }

    public void getCategories(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        hocLoadingDialog.showLoadingDialog();
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "course");
            params.put("apikey", getResources().getString(R.string.lblApiKey));
            params.put("phone", mobile);
            params.put("lang", langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = metaData.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.getListCourses,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            coursesList.clear();
                            if (jsonObject.isNull("list")) {

                            } else {
                                gst_value = Integer.parseInt(jsonObject.getString("gst"));
                                dicount_value = Integer.parseInt(jsonObject.getString("discount"));
                                additional_course_discount = Integer.parseInt(jsonObject.getString("additional_course_discount"));
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                if (langPref.equalsIgnoreCase("en")) {
                                    txtPreferredTitle.setText(getResources().getString(R.string.lblPreferred)+dicount_value+"% discount");
                                    txtAddDiscount.setText(getResources().getString(R.string.learnanother)+additional_course_discount+"% off!");
                                } else {
                                    txtPreferredTitle.setText(getResources().getString(R.string.lblPreferred)+dicount_value+"% छूट पाइए");
                                    txtAddDiscount.setText(additional_course_discount+getResources().getString(R.string.learnanother));
                                }

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        CategoryDatamodel datamodel = new CategoryDatamodel();
                                        datamodel.setCategoryId(object.getString("courseId"));
                                        datamodel.setCategory_Title(object.getString("course_title"));
                                        datamodel.setCategory_description(object.getString("course_description"));
                                        datamodel.setCat_image_url(object.getString("image_url"));
                                        datamodel.setStatus(object.getString("status"));
                                        datamodel.setPreferredDuration(object.getJSONArray("metadata").getJSONObject(0).getString("preferred_duration"));
                                        datamodel.setPremiumDuration(object.getJSONArray("metadata").getJSONObject(0).getString("premium_duration"));
                                        datamodel.setCourse_amount(object.getJSONArray("metadata").getJSONObject(0).getString("course_amount"));
                                        datamodel.setInstalment_amount(object.getJSONArray("metadata").getJSONObject(0).getString("instalment_amount"));
                                        datamodel.setSecond_instalment(object.getJSONArray("metadata").getJSONObject(0).getString("second_instalment"));
                                        datamodel.setStatusNSDC(object.getJSONArray("metadata").getJSONObject(0).getString("nsdc_status"));
                                        datamodel.setCategory_language(object.getJSONArray("metadata").getJSONObject(0).getString("course_language"));


                                        if (object.getJSONArray("metadata").length() != 0) {
                                            datamodel.setPreferred_amount("" + object.getJSONArray("metadata").getJSONObject(0).getString("course_amount"));
                                            datamodel.setPremium_amount("" + object.getJSONArray("metadata").getJSONObject(1).getString("course_amount"));
                                            if (object.getJSONArray("metadata").getJSONObject(0).getString("nsdc_amount_6_months").equals("0")) {
                                                datamodel.setNsdcPreferred_amount("" + object.getJSONArray("metadata").getJSONObject(0).getString("course_amount"));
                                            } else {
                                                datamodel.setNsdcPreferred_amount("" + object.getJSONArray("metadata").getJSONObject(0).getString("nsdc_amount_6_months"));
                                            }
                                            if (object.getJSONArray("metadata").getJSONObject(1).getString("nsdc_amount_12_months").equals("0")) {
                                                datamodel.setNsdcPremium_amount("" + object.getJSONArray("metadata").getJSONObject(1).getString("course_amount"));
                                            } else {
                                                datamodel.setNsdcPremium_amount("" + object.getJSONArray("metadata").getJSONObject(1).getString("nsdc_amount_12_months"));
                                            }
                                        }

                                        coursesList.add(datamodel);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                enrolItemAdapter = new EnrolItemAdapter(EnrolNowActivity.this, coursesList);
                                listItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                listItems.setAdapter(enrolItemAdapter);
                                if (getIntent().getIntExtra("getCourseId", 0) != 0) {
                                    int position = (getIntent().getIntExtra("getCourseId", 0));
                                    for (int i = 0; i < coursesList.size(); i++) {
                                        if (Integer.parseInt(coursesList.get(i).getCategoryId()) == position) {
                                            position = i;
                                        }
                                    }
                                    txtPremiumTitle.setText(getResources().getString(R.string.lblPremium));
                                    linSelectedItem.setVisibility(View.VISIBLE);

                                    selectedAdapter = new selectedAdapter(EnrolNowActivity.this, position);
                                    allSelectedCourses = new allSelectedCourses(EnrolNowActivity.this, position);
                                    itemsSelected.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.VERTICAL, false));
                                    selectedItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                    itemsSelected.setAdapter(selectedAdapter);
                                    selectedItems.setAdapter(allSelectedCourses);
                                    coursesOriginalList.add(coursesList.get(position));
                                    selectedNames.add(coursesList.get(position).getCategory_Title());
                                    enrollment_type.add(hocLoadingDialog.getNsdc(coursesList.get(position).getStatusNSDC()));
                                    payment_mode.add(hocLoadingDialog.getPayment(coursesList.get(position).getInstalment_amount()));
                                    courseIds.add(Integer.parseInt(coursesList.get(position).getCategoryId()));
                                    coursesList.remove(position);
                                }
                            }
                            try {
                                setLanguage(UserDataConstants.lang);
                            } catch (NullPointerException ex) {
                                ex.printStackTrace();
                            }
                            if (getIntent().getStringExtra("notificationId") != null) {
                                LessonLog = UserDataConstants.notificationTitle;
                                PagenameLog = "Enrol Page";
                                ActivityLog = "Notification Clicked";
                                CategoryName = "";
                                CourseLog = "";
                                getLogEvent(EnrolNowActivity.this);
                            }
                            hocLoadingDialog.hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EnrolNowActivity.this, "Internet error", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(EnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(stringRequest);
    }

    public void validateHorizontalCourseList() {
        if (sharedPrefsUtils.getIsFromCoursePageBoolean(ApiConstants.isFromCourse, false)) {
            listCourse.setVisibility(View.GONE);
        } else {
            listCourse.setVisibility(View.VISIBLE);
        }
    }

    public void CalculateCoursePaymentApi() {
        CalculateCoursePayment calculateCoursePayment = new CalculateCoursePayment("Hamstech",
                getResources().getString(R.string.lblApiKey),courseIds);
        Call<CalculateCoursePayment> call = apiService.getCalculateCoursePayment(calculateCoursePayment);
        call.enqueue(new Callback<CalculateCoursePayment>() {
            @Override
            public void onResponse(Call<CalculateCoursePayment> call, retrofit2.Response<CalculateCoursePayment> response) {
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    if (selectedPlan == 1) {
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(response.body().getFullPayment()); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(response.body().getInstallmentAmount()); // + "\n" + htmlAllTaxesString;
                        amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        amountPremium.setText(Html.fromHtml(htmlPremium));
                        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" + NumberFormat.getInstance().format(response.body().getFullPayment()) + "/-";
                        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));
                        finalAmount = response.body().getFullPayment();
                        String payamount = getResources().getString(R.string.paynow);
                        //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount) + " ");
                        payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount));
                        btnNext.setText(payamount);
                    } else if (selectedPlan == 2) {
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(response.body().getFullPayment()); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(response.body().getInstallmentAmount()); // + "\n" + htmlAllTaxesString;
                        amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        amountPremium.setText(Html.fromHtml(htmlPremium));
                        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" + NumberFormat.getInstance().format(response.body().getInstallmentAmount()) + "/-";
                        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));
                        String payamount = getResources().getString(R.string.paynow);
                        finalAmount = response.body().getInstallmentAmount();
                        //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount) + " ");
                        payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount));
                        btnNext.setText(payamount);
                    }
                    txtSecondInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 120 "+getResources().getString(R.string.days));

                    if (paymentOption == 10 || paymentmod == 0) {
                        onlinePay();
                    } else if (paymentmod == 1) {
                        CODpay();
                    }
                }
            }

            @Override
            public void onFailure(Call<CalculateCoursePayment> call, Throwable t) {

            }
        });
    }

    public class EnrolItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        Context context;
        ArrayList<CategoryDatamodel> coursesList;

        public EnrolItemAdapter(Context context, ArrayList<CategoryDatamodel> coursesList) {
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
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                holder.txtTitle.setText(coursesList.get(position).getCategory_Title());
                Glide.with(context)
                        .load(coursesList.get(position).getCat_image_url())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);
                holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linSelectedItem.setVisibility(View.VISIBLE);
                        txtPremiumTitle.setText(getResources().getString(R.string.lblPremium));
                        selectedAdapter = new selectedAdapter(EnrolNowActivity.this, holder.getAdapterPosition());
                        allSelectedCourses = new allSelectedCourses(EnrolNowActivity.this, holder.getAdapterPosition());
                        itemsSelected.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.VERTICAL, false));
                        selectedItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        itemsSelected.setAdapter(selectedAdapter);
                        selectedItems.setAdapter(allSelectedCourses);
                        coursesOriginalList.add(coursesList.get(holder.getAdapterPosition()));
                        listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                        listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSelectedNames.setAdapter(listSelectedNamesAdapter);
                        listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                        listSummaryItems.setAdapter(listSelectedNamesAdapter);
                        selectedNames.add(coursesList.get(holder.getAdapterPosition()).getCategory_Title());
                        enrollment_type.add(hocLoadingDialog.getNsdc(coursesList.get(holder.getAdapterPosition()).getStatusNSDC()));
                        payment_mode.add(hocLoadingDialog.getPayment(coursesList.get(holder.getAdapterPosition()).getInstalment_amount()));
                        courseIds.add(Integer.parseInt(coursesList.get(holder.getAdapterPosition()).getCategoryId()));
                        CategoryName = "";
                        CourseLog = coursesList.get(holder.getAdapterPosition()).getCategory_Title();
                        LessonLog = "";
                        ActivityLog = "Course added";
                        PagenameLog = "Enrol Page";
                        logEnrolEvent(CourseLog, ActivityLog);
                        getLogEvent(EnrolNowActivity.this);

                        if (coursesOriginalList.size() == 0) {
                            planNSDC = 2;
                            selectedPlan = 1;
                            amountPreferred.setVisibility(View.GONE);
                            amountPremium.setVisibility(View.GONE);
                            txtInstalment.setVisibility(View.GONE);
                            linearBill.setVisibility(View.GONE);
                            checkboxNSDC.setChecked(false);
                            linearPreferred.setEnabled(false);
                            linearPremium.setEnabled(false);
                            linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
                            linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                            txtPreferredTitle.setTextColor(getResources().getColor(R.color.muted_blue));
                            amountPreferred.setTextColor(getResources().getColor(R.color.muted_blue));
                            txtPremiumTitle.setTextColor(getResources().getColor(R.color.muted_blue));
                        } else {
                            amountPreferred.setVisibility(View.VISIBLE);
                            amountPremium.setVisibility(View.VISIBLE);
                            //txtInstalment.setVisibility(View.VISIBLE);
//                            linearBill.setVisibility(View.VISIBLE);
                            linearPreferred.setEnabled(true);
                            linearPremium.setEnabled(true);
                        }
                        coursesList.remove(holder.getAdapterPosition());
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
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                float courseAmount = 0;
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                holder.txtTitle.setText(coursesOriginalList.get(holder.getAdapterPosition()).getCategory_Title());

                Glide.with(context)
                        .load(coursesOriginalList.get(holder.getAdapterPosition()).getCat_image_url())
                        .placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgCategory);

                getTotal(coursesOriginalList);
                listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                listSelectedNames.setAdapter(listSelectedNamesAdapter);
                listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                listSummaryItems.setAdapter(listSelectedNamesAdapter);

                if (coursesOriginalList.size() == 0) {
                    planNSDC = 2;
                    selectedPlan = 1;
                    amountPreferred.setVisibility(View.GONE);
                    amountPremium.setVisibility(View.GONE);
                    txtInstalment.setVisibility(View.GONE);
                    linearBill.setVisibility(View.GONE);
                    checkboxNSDC.setChecked(false);
                    linearPreferred.setEnabled(false);
                    linearPremium.setEnabled(false);
                    linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
                    linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                    txtPreferredTitle.setTextColor(getResources().getColor(R.color.black_two));
                    amountPreferred.setTextColor(getResources().getColor(R.color.black_two));
                    txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));
                } else {
                    amountPreferred.setVisibility(View.VISIBLE);
                    amountPremium.setVisibility(View.VISIBLE);
                    //txtInstalment.setVisibility(View.VISIBLE);
//                    linearBill.setVisibility(View.VISIBLE);
                    linearPreferred.setEnabled(true);
                    linearPremium.setEnabled(true);
                }
                courseAmount = Float.parseFloat(coursesOriginalList.get(holder.getAdapterPosition()).getCourse_amount());
                courseAmount = Float.parseFloat(String.format("%.0f", (courseAmount + ((courseAmount) / 100.0f) * 18)));
                String htmlString = "₹" + NumberFormat.getInstance().format(courseAmount) + "/-";
                holder.txtPrice.setText(Html.fromHtml(htmlString));
                if (langPref.equalsIgnoreCase("hi")) {
                    holder.txtLanguage.setText("हिंदी");
                } else {
                    holder.txtLanguage.setText("English");
                }

                if (coursesOriginalList.size() == 1) {
                    holder.txtRemove.setVisibility(View.GONE);
                }

                holder.txtRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getItemCount() > 1) {
                            final Dialog dialog = new Dialog(context);
                            dialog.getWindow();
                            dialog.setCancelable(true);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setGravity(Gravity.CENTER);
                            dialog.setContentView(R.layout.lock_popup);
                            dialog.setCancelable(true);

                            TextView btnNext = dialog.findViewById(R.id.btnNext);
                            TextView txtInstallment = dialog.findViewById(R.id.txtInstallment);
                            TextView txtDialogTitle = dialog.findViewById(R.id.txtDialogTitle);
                            TextView txtContent = dialog.findViewById(R.id.txtContent);
                            ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
                            ImageView imgLock = dialog.findViewById(R.id.imgLock);
                            txtInstallment.setVisibility(View.GONE);
                            txtContent.setVisibility(View.GONE);
                            txtDialogTitle.setText(getResources().getString(R.string.RemoveCourse));
                            btnNext.setText(getResources().getString(R.string.lblYes));
                            txtDialogTitle.setVisibility(View.VISIBLE);
                            dialog.show();

                            imgLock.setVisibility(View.GONE);

                            imgCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });

                            btnNext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    coursesList.add(coursesOriginalList.get(holder.getAdapterPosition()));
                                    CourseLog = coursesOriginalList.get(holder.getAdapterPosition()).getCategory_Title();
                                    if (planNSDC == 2) {
                                        checkboxNSDC.setText(getResources().getString(R.string.getCertified));
                                        checkboxNSDC.setChecked(true);
                                        checkboxNSDC.setEnabled(true);
                                    } else {
                                        checkboxNSDC.setText(getResources().getString(R.string.certificationNotApplicable));
                                        checkboxNSDC.setChecked(false);
                                        checkboxNSDC.setEnabled(false);
                                    }
                                    coursesOriginalList.remove(holder.getAdapterPosition());
                                    selectedNames.remove(holder.getAdapterPosition());
                                    enrollment_type.remove(holder.getAdapterPosition());
                                    payment_mode.remove(holder.getAdapterPosition());
                                    courseIds.remove(holder.getAdapterPosition());
                                    enrolItemAdapter = new EnrolItemAdapter(EnrolNowActivity.this, coursesList);
                                    listItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                    listItems.setAdapter(enrolItemAdapter);
                                    listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                                    listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                    listSelectedNames.setAdapter(listSelectedNamesAdapter);
                                    listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                    listSummaryItems.setAdapter(listSelectedNamesAdapter);
                                    CategoryName = "";
                                    LessonLog = "";
                                    ActivityLog = "Course removed";
                                    PagenameLog = "Enrol Page";
                                    logEnrolEvent(CourseLog, ActivityLog);
                                    getLogEvent(EnrolNowActivity.this);
                                    if (coursesOriginalList.size() == 0) {
                                        planNSDC = 2;
                                        selectedPlan = 1;
                                        linSelectedItem.setVisibility(View.GONE);
                                        amountPreferred.setVisibility(View.GONE);
                                        amountPremium.setVisibility(View.GONE);
                                        txtInstalment.setVisibility(View.GONE);
                                        linearBill.setVisibility(View.GONE);
                                        checkboxNSDC.setChecked(false);
                                        linearPreferred.setEnabled(false);
                                        linearPremium.setEnabled(false);
                                        linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
                                        linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                                        txtPreferredTitle.setTextColor(getResources().getColor(R.color.muted_blue));
                                        amountPreferred.setTextColor(getResources().getColor(R.color.muted_blue));
                                        txtPremiumTitle.setTextColor(getResources().getColor(R.color.muted_blue));
                                        getTotal(coursesOriginalList);
                                    } else if (coursesOriginalList.size() == 1){
                                        amount_type = "fullAmount";
                                        selectedPlan = 1;
                                        course_type = "Preferred";
                                        logEnrolEvent(course_type, "Selected");
                                        linearPreferred.setBackgroundResource(R.drawable.payment_method_blue_shadow);
                                        linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                                        //txtPreferredTitle.setTextColor(getResources().getColor(R.color.white));
                                        //amountPreferred.setTextColor(getResources().getColor(R.color.white));
                                        //txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));
                                        //amountPremium.setTextColor(getResources().getColor(R.color.black_two));
                                        listSelectedNamesAdapter = new ListSelectedNames(EnrolNowActivity.this);
                                        listSelectedNames.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                        listSelectedNames.setAdapter(listSelectedNamesAdapter);
                                        listSummaryItems.setLayoutManager(new LinearLayoutManager(EnrolNowActivity.this, RecyclerView.VERTICAL, false));
                                        listSummaryItems.setAdapter(listSelectedNamesAdapter);
                                        getTotal(coursesOriginalList);
                                    } else if (coursesOriginalList.size() > 1){
                                        amountPreferred.setVisibility(View.VISIBLE);
//                                    linearBill.setVisibility(View.VISIBLE);
                                        amountPremium.setVisibility(View.VISIBLE);
                                        linearPremium.setVisibility(View.GONE);
                                        //txtInstalment.setVisibility(View.VISIBLE);
                                        linearPreferred.setEnabled(true);
                                        linearPremium.setEnabled(true);
                                        for (int k = 0; k < coursesOriginalList.size(); k++) {
                                            if (!coursesOriginalList.get(k).getCategory_Title().contains("Short Course")) {
                                                linearPremium.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    } else {

                                    }
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            //Toast.makeText(EnrolNowActivity.this, "Atleast one item needed", Toast.LENGTH_SHORT).show();
                        }
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

    public void ChatWithUs(View view) {
        CategoryName = "";
        CourseLog = "";
        LessonLog = "";
        ActivityLog = "chat with whatsapp";
        PagenameLog = "Enrol Now";
        logEnrolEvent(ActivityLog, PagenameLog);
        getLogEvent(EnrolNowActivity.this);
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
    }

    public void PayNow() {
        String vAmount = "";
        String vAccessCode = "AVIH91HE94AK04HIKA";
        String vMerchantId = "151047";
        String vCurrency = "INR";
        if (selectedPlan == 1) {
            vAmount = "" + finalAmount;
        } else if (selectedPlan == 2) {
            vAmount = "" + finalAmount;
        }
        vAmount = "" + finalAmount;

/*        if(!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AvenuesParams.ACCESS_CODE, vAccessCode);
            intent.putExtra(AvenuesParams.MERCHANT_ID, vMerchantId);
            intent.putExtra(AvenuesParams.ORDER_ID, String.valueOf(orderID));
            intent.putExtra(AvenuesParams.CURRENCY, vCurrency);
            intent.putExtra(AvenuesParams.AMOUNT, vAmount);
            //intent.putExtra(AvenuesParams.AMOUNT, "1");
            intent.putExtra(AvenuesParams.BILLING_NAME, txtName.getText().toString().trim());
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, txtAddress.getText().toString().trim());
            intent.putExtra(AvenuesParams.BILLING_CITY, txtCity.getText().toString().trim());
            intent.putExtra(AvenuesParams.BILLING_TEL, UserDataConstants.userMobile);
            intent.putExtra(AvenuesParams.BILLING_EMAIL, txtEmail.getText().toString().trim());

            intent.putExtra(AvenuesParams.REDIRECT_URL, ApiConstants.redirectUrl);
            intent.putExtra(AvenuesParams.CANCEL_URL, ApiConstants.redirectUrl);
            intent.putExtra(AvenuesParams.RSA_KEY_URL, ApiConstants.getRsaKey+String.valueOf(orderID));
            intent.putExtra("mRequestBody",mRequestBody);
            startActivity(intent);
        }else{
            Toast.makeText(this, "All parameters are mandatory.", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void PaymentDetails(View view) {

        if (amount == 0 || selectLaguage.trim().equals("Choose your Language")) {
            Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.FieldsNotEmpty), Toast.LENGTH_SHORT).show();
        } else {

            if (paymentmod == -1) {
                paymentmod = 0;
                orderType = "online";
                getSkillId(this);
                //Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.choose_paytype), Toast.LENGTH_SHORT).show();
            } else {
                getSkillId(this);
            }
        }
    }


    public void AfterOrder() {

        CategoryName = "";
        CourseLog = selectedNames.toString().substring(1, (selectedNames.toString().length()) - 1);
        LessonLog = "";
        ActivityLog = "Add cart";
        PagenameLog = "Enrol Page";
        getLogEvent(EnrolNowActivity.this);
        linearBeforePayment.setVisibility(View.GONE);
        linearAfterPayment.setVisibility(View.VISIBLE);
        linCourses.setVisibility(View.GONE);
        selectedItems.setVisibility(View.GONE);
        findViewById(R.id.linearadddiscount).setVisibility(View.GONE);
        headerTitle.setText(getResources().getString(R.string.txtAddressDetails));
        headerTitle.setTextSize(18.0f);
        txtName.setText(UserDataConstants.userName);
        txtPhone.setText(UserDataConstants.userMobile);
        txtEmail.setText(UserDataConstants.userMail);
        txtCity.setText(UserDataConstants.userCity);
        txtAddress.setText(UserDataConstants.userAddress);
        if (!UserDataConstants.userPincode.equalsIgnoreCase("0"))
            txtPincode.setText(UserDataConstants.userPincode);
        layout_type = 1;
    }

    public void goForPayment(View view) {
        if (!ValidateInputs()) {
        } else {
            String email = txtEmail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
            if (email.matches(emailPattern)) {
                courseLayout.setVisibility(View.GONE);
                CategoryName = "";
                CourseLog = selectedNames.toString().substring(1, (selectedNames.toString().length()) - 1);
                LessonLog = "";
                ActivityLog = "Shipping Address";
                PagenameLog = "Enrol Page";
                logEnrolEvent(ActivityLog, PagenameLog);
                getLogEvent(EnrolNowActivity.this);
                layout_type = 2;
            } else {
                Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void paymentOptions() {
        orderID = System.currentTimeMillis();
        hocCreateRequest();
        CategoryName = "";
        CourseLog = selectedNames.toString().substring(1, (selectedNames.toString().length()) - 1);
        LessonLog = "";
        ActivityLog = "Pay Now";
        PagenameLog = "Enrol Page";
        logEnrolEvent(selectedPayment, "Payment option");
        getLogEvent(EnrolNowActivity.this);
        if (paymentmod == 0) {
            orderType = "online";
            init_transactions(this);
        } else {
            orderType = "cod";
            //SuccessfulPopUp();
            init_transactions(this);
            hocLoadingDialog.hideDialog();
            new AppsFlyerEventsHelper(EnrolNowActivity.this).EventAdmission(selectedNames.toString(), payment_mode.toString());
            AfterOrder();
        }

//        if (paymentOption < 4) {
//            orderType = "online";
//            init_transactions(this);
//        } else if (paymentOption == 4){
//            if(!ValidateInputs()) {
//            } else {
//                String email = txtEmail.getText().toString().trim();
//                String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
//                if (email.matches(emailPattern)){
//                    orderType = "cod";
//                    SuccessfulPopUp();
//                    //postCodHoc(this);
//                } else {
//                    Toast.makeText(EnrolNowActivity.this, getResources().getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }
    }

    public void TermsConditions() {
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.terms_conditions_dialogue);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        final WebView webview = dialog.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);

        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.setVisibility(View.VISIBLE);
            }
        });

        webview.loadUrl(ApiConstants.BaseUrl + "terms-conditions" + "-" + langPref + "/");
        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public class allSelectedCourses extends RecyclerView.Adapter<ViewHolder> {

        Context context;
        int courseId;

        public allSelectedCourses(Context context, int courseId) {
            this.context = context;
            this.courseId = courseId;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.selected_item_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtTitle.setText(coursesOriginalList.get(position).getCategory_Title());
                holder.itemCheck.setBackgroundResource(R.drawable.tick_checkbox);
                Glide.with(context)
                        .load(coursesOriginalList.get(position).getCat_image_url())
                        .placeholder(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgCategory);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesOriginalList.size();
        }
    }

    public class ListSelectedNames extends RecyclerView.Adapter<ViewHolder> {
        Context context;

        public ListSelectedNames(Context context) {
            this.context = context;
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
                holder.txtName.setText(coursesOriginalList.get(position).getCategory_Title());
                if (selectedPlan == 1) {
                    holder.txtAmount.setText("₹" + NumberFormat.getInstance().format(Float.parseFloat(coursesOriginalList.get(position).getCourse_amount())));
                } else {
                    if (coursesOriginalList.get(position).getInstalment_amount().equals("0")) {
                        holder.txtAmount.setText("₹" + NumberFormat.getInstance().format(Float.parseFloat(coursesOriginalList.get(position).getCourse_amount())));
                    } else {
                        holder.txtAmount.setText("₹" + NumberFormat.getInstance().format(Float.parseFloat(coursesOriginalList.get(position).getInstalment_amount())));
                    }
                }
                getTotal(coursesOriginalList);
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

        try {
            params.put("name", userDataBase.getUserName(1));
            params.put("email", txtEmail.getText().toString().trim());
            params.put("mobile", userDataBase.getUserMobileNumber(1));
            params.put("country", UserDataConstants.userCountryName);
            params.put("state", txtState.getText().toString().trim());
            params.put("city", txtCity.getText().toString().trim());
            params.put("address", txtAddress.getText().toString().trim());
            params.put("pincode", txtPincode.getText().toString().trim());
            params.put("course", new JSONArray(selectedNames));
            params.put("courseIds", new JSONArray(courseIds));
            params.put("course_type", course_type);
            params.put("language", selectLaguage);
            params.put("amount", amount);
            params.put("order_id", orderID);
            params.put("status", "Success");
            params.put("discount_percentage", discount_percentage);
            params.put("gst", gst_amount);
            params.put("discount_amount", discount_amount);
            params.put("skill_id", new JSONArray(skillIds));
            metaData.put("data", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestBody = params.toString();
    }

    public void createOnineOrder(Context context) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", userDataBase.getUserName(1));
            params.put("email", txtEmail.getText().toString().trim());
            params.put("mobile", userDataBase.getUserMobileNumber(1));
            params.put("country", txtCountry.getText().toString().trim());
            params.put("state", txtState.getText().toString().trim());
            params.put("city", txtCity.getText().toString().trim());
            params.put("address", txtAddress.getText().toString().trim());
            params.put("pincode", txtPincode.getText().toString().trim());
            params.put("course", new JSONArray(selectedNames));
            params.put("courseIds", new JSONArray(courseIds));
            params.put("course_type", course_type);
            params.put("amount_type", amount_type);
            params.put("language", selectLaguage);
            params.put("amount", String.valueOf(amount));
            params.put("discount_percentage", discount_percentage);
            params.put("gst", String.valueOf(gst_amount));
            params.put("discount_amount", String.valueOf(discount_amount));
            params.put("skill_id", new JSONArray(skillIds));
            params.put("enrollment_type", new JSONArray(enrollment_type));
            params.put("payment_mode", new JSONArray(payment_mode));
            params.put("total_amount", String.valueOf(normal_amount));
            mRequestBody = params.toString();
            startPayment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void postCodHoc(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        if (selectedNames.size() > 1) {
            discount_percentage = "10";
        } else {
            discount_percentage = "0";
        }
        try {
            params.put("name", userDataBase.getUserName(1));
            params.put("email", txtEmail.getText().toString().trim());
            params.put("mobile", userDataBase.getUserMobileNumber(1));
            params.put("country", txtCountry.getText().toString().trim());
            params.put("state", txtState.getText().toString().trim());
            params.put("city", txtCity.getText().toString().trim());
            params.put("address", txtAddress.getText().toString().trim());
            params.put("pincode", txtPincode.getText().toString().trim());
            params.put("course", new JSONArray(selectedNames));
            params.put("enrollment_type", new JSONArray(enrollment_type));
            params.put("courseIds", new JSONArray(courseIds));
            params.put("course_type", course_type);
            params.put("language", selectLaguage);
            params.put("amount", amount);
            params.put("discount_percentage", discount_percentage);
            params.put("gst", gst_amount);
            params.put("discount_amount", discount_amount);
            params.put("skill_id", new JSONArray(skillIds));
            metaData.put("data", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRequestBody = metaData.toString();
        hocLoadingDialog.showLoadingDialog();
        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.post_createhocorder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.getString("message").equals("success")) {
                        hocLoadingDialog.hideDialog();
                        new AppsFlyerEventsHelper(EnrolNowActivity.this).EventAdmission(selectedNames.toString(), payment_mode.toString());
                        SuccessfulPopUp();
                    } else {
                        Toast.makeText(EnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void init_transactions(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
//        if (selectedNames.size()>1){
//            discount_percentage = "25";
//        } else {
        discount_percentage = "10";
//        }
//        if (paymentOption < 4) {
        discount_percentage = String.valueOf(Integer.parseInt(discount_percentage) + 5);
        discount_amount = Float.parseFloat(String.format("%.0f", (discount_amount + (finalAmount * 0.05))));
        amount = Float.parseFloat(String.format("%.0f", (finalAmount - (finalAmount * 0.05))));
        //gst_amount = Float.parseFloat(String.format("%.0f",(amount + (amount * 0.18))));
//        }

        try {
            params.put("apikey", getResources().getString(R.string.lblApiKey));
            params.put("appname", "Hamstech");
            params.put("phone", UserDataConstants.userMobile);
            params.put("page", "PaymentPage");
            params.put("courseid", courseIds.toString().substring(1, (courseIds.toString().length()) - 1));
            params.put("course_language", selectLaguage);
            params.put("amount", amount);
            params.put("discount_percentage", discount_percentage);
            params.put("gst_amount", gst_amount);
            params.put("discount_amount", discount_amount);
            params.put("course_type", course_type);
            params.put("skill_set", skillIdString);
            params.put("orderid", orderID);
            params.put("ordertype", orderType);
            params.put("order_amount", amount);
            params.put("amount_type", amount_type);
            params.put("billing_address", txtAddress.getText().toString().trim());
            params.put("billing_city", txtCity.getText().toString().trim());
            params.put("billing_email", txtEmail.getText().toString().trim());
            params.put("billing_country", txtCountry.getText().toString().trim());
            params.put("billing_pincode", txtPincode.getText().toString().trim());
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
                    JSONObject dataObject = object.getJSONObject("data");
                    if (jsonObject.getString("status_message").equals("success")) {
                        if (orderType.equalsIgnoreCase("online")){
                            razorpayOrderid = dataObject.getString("razorpay_order_id");
                            createOnineOrder(EnrolNowActivity.this);
                        }
                        hocLoadingDialog.hideDialog();
                    } else {
                        Toast.makeText(EnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void getTotal(ArrayList<CategoryDatamodel> listDetails) {
        amount = 0;
        premiumCost = 0;
        normal_amount = 0;
        second_instalment = 0;
        if (selectedPlan == 1) {
            if (listDetails.size() != 0) {
                linearPreferred.setBackgroundResource(R.drawable.payment_method_blue_shadow);
                linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                //txtPreferredTitle.setTextColor(getResources().getColor(R.color.white));
                //amountPreferred.setTextColor(getResources().getColor(R.color.white));
                //txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));
                //amountPremium.setTextColor(getResources().getColor(R.color.black_two));
                //linearPremium.setVisibility(View.VISIBLE);
                txtTankyouChoose.setVisibility(View.GONE);
                linearBill.setVisibility(View.GONE);
                linearPreferred.setEnabled(true);
                linearPremium.setEnabled(true);
                if (listDetails.size() > 1) {
                    for (int k = 0; k < listDetails.size(); k++) {
                        normal_amount = normal_amount + Float.parseFloat(listDetails.get(k).getCourse_amount());
                        if (listDetails.get(k).getInstalment_amount().equalsIgnoreCase("0")) {
                            premiumCost = premiumCost + Float.parseFloat(listDetails.get(k).getCourse_amount());
                            //linearPremium.setVisibility(View.VISIBLE);
                        } else {
                            //linearPremium.setVisibility(View.VISIBLE);
                            premiumCost = premiumCost + Float.parseFloat(listDetails.get(k).getInstalment_amount());
                        }
                        if (!listDetails.get(k).getCategory_Title().contains("Short Course")){
                            linearPremium.setVisibility(View.VISIBLE);
                        }
                        amount = amount + Float.parseFloat(listDetails.get(k).getCourse_amount());
                        instalment_amount = instalment_amount + Float.parseFloat(listDetails.get(k).getInstalment_amount());
                        second_instalment = second_instalment + Float.parseFloat(listDetails.get(k).getSecond_instalment());
                    }
                    Actual_amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    discount_amount = Float.parseFloat(String.format("%.0f", ((amount) / 100.0f) * 25));
                    amount = Float.parseFloat(String.format("%.0f", (amount - ((amount) / 100.0f) * 25)));
                    normal_amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost - ((premiumCost) / 100.0f) * 5)));
                    gst_amount = Float.parseFloat(String.format("%.0f", (((amount) / 100.0f) * 18)));
                    premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                    String htmlPreferred = "₹" + NumberFormat.getInstance().format(normal_amount); // + "\n" + htmlAllTaxesString;
                    String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                    /*amountPreferred.setText(Html.fromHtml(htmlPreferred));
                    amountPremium.setText(Html.fromHtml(htmlPremium));
                    //amountPremium.setText(R.string.instalment);
                    txtInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 60 "+getResources().getString(R.string.days));*/
                    txtSecondInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 120 "+getResources().getString(R.string.days));
                    txtDiscountText.setText(getResources().getString(R.string.discount_25));
                    /*txtDiscountAmount.setText("-₹" + NumberFormat.getInstance().format(discount_amount) + "/-");
                    txtTaxesAmount.setText("₹" + NumberFormat.getInstance().format(gst_amount) + "/-");
                    txtGrandTotal.setText("₹" + NumberFormat.getInstance().format(normal_amount) + "/-");*/
                    finalAmount = normal_amount;
                    relativeDiscount.setVisibility(View.GONE);
                } else {
                    Actual_amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    normal_amount = Float.parseFloat(listDetails.get(0).getCourse_amount());
                    premiumCost = Float.parseFloat(listDetails.get(0).getInstalment_amount());
                    instalment_amount = Float.parseFloat(listDetails.get(0).getInstalment_amount());
                    second_instalment = Float.parseFloat(listDetails.get(0).getSecond_instalment());
                    if (listDetails.get(0).getCategory_Title().contains("Short Course")){
                        linearPremium.setVisibility(View.GONE);
                    } else linearPremium.setVisibility(View.VISIBLE);
                    if (listDetails.get(0).getInstalment_amount().equals("0")) {
                        //linearPremium.setVisibility(View.VISIBLE);
                        amount = Float.parseFloat(listDetails.get(0).getCourse_amount());
                        Actual_amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                        discount_amount = (float) (amount * 0.15);
                        finalAmount = (float) (amount - (amount * 0.15));
                        normal_amount = Float.parseFloat(String.format("%.0f", (finalAmount + ((finalAmount) / 100.0f) * 18)));
                        full_amount = Float.parseFloat(String.format("%.0f", (finalAmount + ((finalAmount) / 100.0f) * 18)));
                        premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(normal_amount); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                        /*amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        amountPremium.setText(Html.fromHtml(htmlPremium));*/
                    } else {
                        amount = Float.parseFloat(listDetails.get(0).getCourse_amount());
                        Actual_amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                        discount_amount = (float) (amount * 0.15);
                        finalAmount = (float) (amount - (amount * 0.15));
                        full_amount = Float.parseFloat(String.format("%.0f", (finalAmount + ((finalAmount) / 100.0f) * 18)));
                        premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(full_amount); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                        /*amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        amountPremium.setText(Html.fromHtml(htmlPremium));*/
                    }
                    gst_amount = Float.parseFloat(String.format("%.0f", ((finalAmount) / 100.0f) * 18));
                    amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    //full_amount = Float.parseFloat(String.format("%.0f",(full_amount+((full_amount) / 100.0f) * 18)));
                    /*txtInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 60 "+getResources().getString(R.string.days));*/
                    txtSecondInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 120 "+getResources().getString(R.string.days));


                    relativeDiscount.setVisibility(View.GONE);
                    txtDiscountText.setText(getResources().getString(R.string.discount));
                    /*txtDiscountAmount.setText("-₹" + NumberFormat.getInstance().format(discount_amount) + "/-");
                    txtTaxesAmount.setText("₹" + NumberFormat.getInstance().format(gst_amount) + "/-");
                    txtGrandTotal.setText("₹" + NumberFormat.getInstance().format(full_amount) + "/-");*/
                    finalAmount = full_amount;
                }

            } else {
                planNSDC = 2;
                linearPreferred.setEnabled(false);
                linearPremium.setEnabled(false);
                linearPreferred.setBackgroundResource(R.drawable.unselect_payment_method);
                linearPremium.setBackgroundResource(R.drawable.unselect_payment_method);
                //txtPreferredTitle.setTextColor(getResources().getColor(R.color.black_two));
                //amountPreferred.setTextColor(getResources().getColor(R.color.black_two));
                //txtPremiumTitle.setTextColor(getResources().getColor(R.color.black_two));
                //amountPremium.setTextColor(getResources().getColor(R.color.black_two));
                linearBill.setVisibility(View.GONE);
                txtTankyouChoose.setVisibility(View.GONE);
            }
        } else if (selectedPlan == 2) {
            if (listDetails.size() != 0) {
                //linearPremium.setVisibility(View.VISIBLE);
                linearBill.setVisibility(View.GONE);
                linearPreferred.setEnabled(true);
                linearPremium.setEnabled(true);
                txtTankyouChoose.setVisibility(View.GONE);
                if (listDetails.size() > 1) {
                    for (int k = 0; k < listDetails.size(); k++) {
                        if (listDetails.get(k).getInstalment_amount().equalsIgnoreCase("0")) {
                            premiumCost = premiumCost + Float.parseFloat(listDetails.get(k).getCourse_amount());
                        } else
                            premiumCost = premiumCost + Float.parseFloat(listDetails.get(k).getInstalment_amount());
                        normal_amount = normal_amount + Float.parseFloat(listDetails.get(k).getCourse_amount());
                        instalment_amount = instalment_amount + Float.parseFloat(listDetails.get(k).getInstalment_amount());
                        second_instalment = second_instalment + Float.parseFloat(listDetails.get(k).getSecond_instalment());
                        if (!listDetails.get(k).getCategory_Title().contains("Short Course")){
                            linearPremium.setVisibility(View.VISIBLE);
                        }
                        if (listDetails.get(k).getInstalment_amount().equalsIgnoreCase("0")) {
                            amount = amount + Float.parseFloat(listDetails.get(k).getCourse_amount());
                        } else
                            amount = amount + Float.parseFloat(listDetails.get(k).getInstalment_amount());
                    }
                    discount_amount = Float.parseFloat(String.format("%.0f", ((amount) / 100.0f) * 15));
                    amount = Float.parseFloat(String.format("%.0f", (amount - ((amount) / 100.0f) * 15)));
                    normal_amount = Float.parseFloat(String.format("%.0f", (normal_amount - ((normal_amount) / 100.0f) * (dicount_value+additional_course_discount+5))));
                    normal_amount = Float.parseFloat(String.format("%.0f", (normal_amount + ((normal_amount) / 100.0f) * 18)));
                    premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost - ((premiumCost) / 100.0f) * 15)));
                    premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                    String htmlPreferred = "₹" + NumberFormat.getInstance().format(normal_amount); // + "\n" + htmlAllTaxesString;
                    String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                    /*amountPreferred.setText(Html.fromHtml(htmlPreferred));
                    amountPremium.setText(Html.fromHtml(htmlPremium));
                    txtInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 60 "+getResources().getString(R.string.days));*/
                    txtSecondInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 120 "+getResources().getString(R.string.days));
                    txtDiscountText.setText(getResources().getString(R.string.discount));
                    gst_amount = Float.parseFloat(String.format("%.0f", ((amount) / 100.0f) * 18));
                    amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    /*txtDiscountAmount.setText("-₹" + NumberFormat.getInstance().format(discount_amount) + "/-");
                    txtTaxesAmount.setText("₹" + NumberFormat.getInstance().format(gst_amount) + "/-");
                    txtGrandTotal.setText("₹" + NumberFormat.getInstance().format(amount) + "/-");*/
                    finalAmount = amount;
                    relativeDiscount.setVisibility(View.VISIBLE);
                } else {
                    premiumCost = Float.parseFloat(listDetails.get(0).getInstalment_amount());
                    normal_amount = Float.parseFloat(listDetails.get(0).getCourse_amount());
                    instalment_amount = instalment_amount + Float.parseFloat(listDetails.get(0).getInstalment_amount());
                    second_instalment = second_instalment + Float.parseFloat(listDetails.get(0).getSecond_instalment());
                    if (listDetails.get(0).getCategory_Title().contains("Short Course")){
                        linearPremium.setVisibility(View.GONE);
                    } else linearPremium.setVisibility(View.VISIBLE);
                    if (listDetails.get(0).getInstalment_amount().equals("0")) {
                        //linearPremium.setVisibility(View.VISIBLE);
                        amount = Float.parseFloat(listDetails.get(0).getCourse_amount());
                        normal_amount = Float.parseFloat(String.format("%.0f", (normal_amount + ((normal_amount) / 100.0f) * 18)));
                        premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(normal_amount); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                        /*amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        amountPremium.setText(Html.fromHtml(htmlPremium));*/
                    } else {
                        amount = Float.parseFloat(listDetails.get(0).getInstalment_amount());
                        discount_amount = (float) (amount * 0.15);
                        finalAmount = (float) (amount - (amount * 0.15));
                        normal_amount = (float) (normal_amount - (normal_amount * 0.15));
                        normal_amount = Float.parseFloat(String.format("%.0f", (normal_amount + ((normal_amount) / 100.0f) * 18)));
                        premiumCost = Float.parseFloat(String.format("%.0f", (premiumCost + ((premiumCost) / 100.0f) * 18)));
                        String htmlPreferred = "₹" + NumberFormat.getInstance().format(normal_amount); // + "\n" + htmlAllTaxesString;
                        String htmlPremium = "₹" + NumberFormat.getInstance().format(premiumCost); // + "\n" + htmlAllTaxesString;
                        //amountPreferred.setText(Html.fromHtml(htmlPreferred));
                        //amountPremium.setText(Html.fromHtml(htmlPremium));
                    }
                    /*txtInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 60 "+getResources().getString(R.string.days));*/
                    txtSecondInstalment.setText(getResources().getString(R.string.instalment)+" 2- "+"₹" + NumberFormat.getInstance().format(second_instalment)+
                            " 120 "+getResources().getString(R.string.days));
                    gst_amount = Float.parseFloat(String.format("%.0f", ((amount) / 100.0f) * 18));
                    amount = Float.parseFloat(String.format("%.0f", (amount + ((amount) / 100.0f) * 18)));
                    relativeDiscount.setVisibility(View.GONE);
                    //txtDiscountAmount.setText("-₹" + NumberFormat.getInstance().format(discount_amount) + "/-");
                    //txtTaxesAmount.setText("₹" + NumberFormat.getInstance().format(gst_amount) + "/-");
                    //txtGrandTotal.setText("₹" + NumberFormat.getInstance().format(amount) + "/-");
                    finalAmount = amount;
                }
            } else {
                planNSDC = 2;
                linearPreferred.setEnabled(false);
                linearPremium.setEnabled(false);
                linearBill.setVisibility(View.GONE);
                txtTankyouChoose.setVisibility(View.GONE);
            }
        }

        /*String payamount = getResources().getString(R.string.paynow);
        //payamount = payamount.replace(" ", " ₹" + NumberFormat.getInstance().format(finalAmount) + " ");
        payamount = String.format(getResources().getString(R.string.pay_now), "₹" + NumberFormat.getInstance().format(finalAmount));
        btnNext.setText(payamount);*/
        //btnNext.setEnabled(false);
        //btnNext.setBackgroundResource(R.drawable.button_shape_gray);

        amounttotal.setText("₹" + NumberFormat.getInstance().format(Actual_amount) + "/-");
        amounttotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        //linearonlinepay.setBackgroundResource(R.drawable.border_gray_strok);
        //linearcashdelevery.setBackgroundResource(R.drawable.border_gray_strok);
        CalculateCoursePaymentApi();
        /*txtViewOrderSummary.setText(getResources().getString(R.string.txtViewOrderSummary) + coursesOriginalList.size() + " " + getResources().getString(R.string.txtSummaryCourse));
        String htmlFinalAmount = getResources().getString(R.string.txtOrderTotal) + "\n" + "₹" + NumberFormat.getInstance().format(finalAmount) + "/-";
        txtFinalAmount.setText(Html.fromHtml(htmlFinalAmount));

        if (paymentOption == 10 || paymentmod == 0) {
            onlinePay();
        } else if (paymentmod == 1) {
            CODpay();
        }*/
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

        enrolItemAdapter = new EnrolItemAdapter(EnrolNowActivity.this, coursesList);
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

    public void getSkillId(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        hocLoadingDialog.showLoadingDialog();
        try {
            params.put("phone", UserDataConstants.userMobile);
            params.put("courseid", courseIds.toString().substring(1, (courseIds.toString().length()) - 1));
            params.put("coursetype", course_type);
            params.put("language", "english");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getskills, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    if (object.getInt("status") == 200) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        skillIdString = jsonObject.getString("skillid");
                        skillIds = skillIdString.split(",");
                        paymentOptions();
                    } else {
                        Toast.makeText(EnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
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

        Glide.with(EnrolNowActivity.this)
                .asGif()
                .load(R.drawable.successfull_gif)
                .into(progressBar);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
                startActivity(intent);

            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
//                    Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
//                    startActivity(intent);

                    AfterOrder();
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

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            String vAmount = String.format("%.0f", finalAmount);
            //if (paymentOption < 4) {
            vAmount = String.format("%.0f", (finalAmount - (finalAmount * 0.05)));
            //}
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
            options.put("order_id", razorpayOrderid);

            JSONObject preFill = new JSONObject();
            //preFill.put("email", txtEmail.getText().toString().trim());
            preFill.put("email", email);
            preFill.put("contact", mobile);

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
            PaymentSuccessAPi(tracking_id);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            //Toast.makeText(this, "Payment error: "+"  " + s, Toast.LENGTH_SHORT).show();
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
                    //OnlineSuccessfulPopUp();
                    onlinePaymentSuccessful(EnrolNowActivity.this);
                else
                    Toast.makeText(EnrolNowActivity.this, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {
                Toast.makeText(EnrolNowActivity.this, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void PaymentSuccessAPi(Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params;
        JSONObject metaData = new JSONObject();
        try {
            //mRequestBody = getIntent().getStringExtra("mRequestBody");
            params = new JSONObject(getIntent().getStringExtra("mRequestBody"));
            params.put("status", "Success");
            params.put("order_id", orderID);
            params.put("tracking_id", tracking_id);
            metaData.put("data", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.create_hoconlineorder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if (jo.getString("message").equals("success")) {
                        OnlineSuccessfulPopUp();
                    } else {
                        Toast.makeText(EnrolNowActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EnrolNowActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void onlinePaymentSuccessful(Context context) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_successful);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        TextView textAddress = dialog.findViewById(R.id.textAddress);
        TextView fillNow = dialog.findViewById(R.id.fillNow);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
                dialog.dismiss();
                startActivity(intent);
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.pay_message));
        int start = getString(R.string.pay_message).indexOf("message") + 7;
        spannableStringBuilder.setSpan(new ImageSpan(context, R.drawable.ic_message), start, start + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textMessage.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

        spannableStringBuilder = new SpannableStringBuilder(getString(R.string.pay_address));
        start = getString(R.string.pay_address).indexOf("home");
        spannableStringBuilder.setSpan(new ImageSpan(context, R.drawable.ic_home), start, start + 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textAddress.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

        fillNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                AfterOrder();
            }
        });

        dialog.show();
    }

    private void learnSuccess() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_successful);
        dialog.setCancelable(false);

        TextView textSuccess = dialog.findViewById(R.id.textSuccess);
        textSuccess.setVisibility(View.GONE);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        TextView textAddress = dialog.findViewById(R.id.textAddress);
        textAddress.setVisibility(View.GONE);
        TextView fillNow = dialog.findViewById(R.id.fillNow);
        fillNow.setVisibility(View.GONE);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
                dialog.dismiss();
                startActivity(intent);
            }
        });

        textMessage.setText(getString(R.string.learn_success));

        dialog.show();
    }

    public void OnlineSuccessfulPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.successfull_layout);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);
        LinearLayout txt_ClickHere = dialog.findViewById(R.id.txt_ClickHere);

        onlinePaymentLayout.setVisibility(View.VISIBLE);
        cod_layout.setVisibility(View.GONE);

        Glide.with(EnrolNowActivity.this)
                .asGif()
                .load(R.drawable.successfull_gif)
                .into(progressBar);

        dialog.show();
        ActivityLog = "Payment success";
        PagenameLog = "Enrol page";
        getLogEvent(EnrolNowActivity.this);

        txt_ClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hoccourses")));
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
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
//                    Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
//                    startActivity(intent);

                    AfterOrder();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (layout_type == 1) {
            linearBeforePayment.setVisibility(View.VISIBLE);
            linearAfterPayment.setVisibility(View.GONE);
            findViewById(R.id.linearadddiscount).setVisibility(View.VISIBLE);
            linCourses.setVisibility(View.VISIBLE);
            selectedItems.setVisibility(View.GONE);
            headerTitle.setText(getResources().getString(R.string.enrol_title));
            headerTitle.setTextSize(14.0f);
            layout_type = 1001;
        } else if (layout_type == 2) {
            courseLayout.setVisibility(View.VISIBLE);
            layout_type = 1;
        } else if (layout_type == 2) {

        } else if (layout_type == 1001) {
            super.onBackPressed();
        }
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
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, String.valueOf(amount));
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "INR");
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);
        params.putString(FirebaseAnalytics.Param.CURRENCY, String.valueOf(amount));
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