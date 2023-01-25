package com.hamstechonline.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.adapters.CentersAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.DynamicWhatsAppChat;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.Params;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContactActivity extends AppCompatActivity {

    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    TextView btnRequestCall,txtChatUs,txtCallUs;
    DrawerLayout drawer;
    RecyclerView listItems;
    CentersAdapter centersAdapter;
    UserDataBase userDataBase;
    int mMenuId;
    HocLoadingDialog hocLoadingDialog;
    SharedPreferences footerStatus;
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog,lessonLog,mobile = "",fullname = "",email = "",footerMenuStatus;
    AppEventsLogger logger;
    Bundle params;
    FirebaseAnalytics firebaseAnalytics;
    ImageButton stickyWhatsApp;
    DynamicWhatsAppChat dynamicWhatsAppChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.contact_activity);

        drawer = findViewById(R.id.drawer_layout);
        navSideMenu = findViewById(R.id.navSideMenu);
        listItems = findViewById(R.id.listItems);
        btnRequestCall = findViewById(R.id.btnRequestCall);
        txtChatUs = findViewById(R.id.txtChatUs);
        txtCallUs = findViewById(R.id.txtCallUs);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        params = new Bundle();

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        listItems.setFocusable(false);

        hocLoadingDialog = new HocLoadingDialog(this);

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        centersAdapter = new CentersAdapter(this);
        listItems.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listItems.setAdapter(centersAdapter);

        logger = AppEventsLogger.newLogger(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = "";
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Contact Us").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Contact Us")
                    .commit();
        }

        if (getIntent().getStringExtra("notificationId")!= null){
            lessonLog = UserDataConstants.notificationTitle;
            PagenameLog = "Contact us page";
            ActivityLog = "Notification Clicked";
            getLogEvent(this);
        }

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

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

        btnRequestCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLog = "";
                ActivityLog = "Request callback";
                PagenameLog = "";
                logContactusEvent(ActivityLog);
                getLogEvent(ContactActivity.this);
                getRequestCall(ContactActivity.this);
            }
        });
        txtCallUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLog = "";
                ActivityLog = "Call Us";
                PagenameLog = "";
                logContactusEvent(ActivityLog);
                getLogEvent(ContactActivity.this);
                CallMethod();
            }
        });
        txtChatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonLog = "";
                PagenameLog = "chat with whatsapp";
                ActivityLog = "Contact Us";
                logContactusEvent(ActivityLog);
                getLogEvent(ContactActivity.this);
                PackageManager packageManager = getPackageManager();
                dynamicWhatsAppChat = new DynamicWhatsAppChat(ContactActivity.this,"","","");
                dynamicWhatsAppChat.getChatNumber(userDataBase.getUserMobileNumber(1));
            }
        });

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWhatsAppChat = new DynamicWhatsAppChat(ContactActivity.this,"Contact us","","");
                dynamicWhatsAppChat.getChatNumber(userDataBase.getUserMobileNumber(1));
            }
        });
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void CallMethod(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+getResources().getString(R.string.lblContactus)));
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(ContactActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.post_upload_successfull);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        TextView txtlabelBold = dialog.findViewById(R.id.txtlabelBold);
        TextView txtlabelNormal = dialog.findViewById(R.id.txtlabelNormal);

        txtlabelNormal.setVisibility(View.GONE);

        txtlabelBold.setText(getResources().getString(R.string.call_request_accepted));

        Glide.with(ContactActivity.this)
                .load(R.drawable.discussion_post_submit_thumsup)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.discussion_post_submit_thumsup)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void WebLink(View view){
        TextView textView = (TextView) view;
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(textView.getText().toString()));
        startActivity(myIntent);
    }

    public void getRequestCall(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        hocLoadingDialog.showLoadingDialog();
        JSONObject params = new JSONObject();

        try {
            params.put("appname","Hamstech");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("leadid",UserDataConstants.prospectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getRequestCallBack, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    if (object.getInt("status")==200){
                        hocLoadingDialog.hideDialog();
                        uploadSuccessPopUp();
                    } else {
                        hocLoadingDialog.hideDialog();
                        Toast.makeText(ContactActivity.this, ""+jo.getString("messsage"), Toast.LENGTH_SHORT).show();
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                    hocLoadingDialog.hideDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

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
                    Toast.makeText(ContactActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);

        queue.add(sr);
    }

    public void logContactusEvent(String eventValue){
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, eventValue);
        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
        params.putString(Params.CONTENT_TYPE, eventValue);
        firebaseAnalytics.logEvent("contact_us", params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(eventValue);
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",email);
            data.put("category","");
            data.put("course","");
            data.put("lesson",lessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
