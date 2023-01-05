package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class NotificationsActivity extends AppCompatActivity {

    DrawerLayout drawer;
    RelativeLayout navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    RecyclerView listItems;
    NotificationAdapter notificationAdapter;
    ArrayList<LessonsDataModel> dataNotifications = new ArrayList<>();
    UserDataBase userDataBase;
    int mMenuId;
    String langPref;
    SharedPreferences prefs,footerStatus;
    HocLoadingDialog hocLoadingDialog;
    ArrayList<LessonsDataModel> notificationDetail = new ArrayList<>();
    ImageViewTouch imageView;
    ImageView imgCancel;
    TextView txtTitle,txtDescription;
    String PagenameLog,activityLog = "",postId,footerMenuStatus;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.notification_activity);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        listItems = findViewById(R.id.listItems);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();

        hocLoadingDialog = new HocLoadingDialog(this);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getNotifications(this);
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
        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Notifications page").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Notifications page")
                    .commit();
        }
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

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    public void getNotifications(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","notification");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getNotifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    dataNotifications.clear();
                    if (jo.isNull("list")){
                        Toast.makeText(NotificationsActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray jsonArray = jo.getJSONArray("list");

                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            LessonsDataModel dataModel = new LessonsDataModel();

                            dataModel.setLessonId(object.getString("termid"));
                            dataModel.setLesson_title(object.getString("title"));
                            dataModel.setLesson_description(object.getString("description"));
                            dataModel.setLesson_image_url(object.getString("image"));
                            dataModel.setDate(object.getString("date"));

                            dataNotifications.add(dataModel);
                        }
                        notificationAdapter = new NotificationAdapter(NotificationsActivity.this,dataNotifications);
                        listItems.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this, RecyclerView.VERTICAL, false));
                        listItems.setAdapter(notificationAdapter);
                        hocLoadingDialog.hideDialog();
                        if (getIntent().getStringExtra("notificationId")!= null){
                            postId = "Post Id - "+getIntent().getStringExtra("notificationId");
                            showNotifications(NotificationsActivity.this,Integer.parseInt(getIntent().getStringExtra("notificationId")));
                        }
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
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
                    Toast.makeText(NotificationsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

        Context context;
        ArrayList<LessonsDataModel> dataNotifications;

        public NotificationAdapter(Context context,ArrayList<LessonsDataModel> dataNotifications){
            this.context = context;
            this.dataNotifications = dataNotifications;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.notification_item_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                holder.txtTitle.setText(dataNotifications.get(position).getLesson_title());
                holder.txtDescription.setText(dataNotifications.get(position).getLesson_description());
                holder.txtdate.setText(dataNotifications.get(position).getDate());
                Glide.with(context)
                        .load(dataNotifications.get(position).getLesson_image_url())
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imgNotification);
                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dataNotifications.get(position).getDate().equals("")){

                        } else showNotifications(context,Integer.parseInt(dataNotifications.get(position).getLessonId()));
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dataNotifications.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgNotification;
            TextView txtTitle,txtDescription, txtdate;
            RelativeLayout mainLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgNotification = view.findViewById(R.id.imgNotification);
                txtTitle = view.findViewById(R.id.txtTitle);
                txtDescription = view.findViewById(R.id.txtDescription);
                txtdate = view.findViewById(R.id.txtdate);
                mainLayout = view.findViewById(R.id.mainLayout);
            }
        }


    }
    public void showNotifications(final Context context, final int place){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","notificationdetail");
            params.put("apikey",context.getResources().getString(R.string.lblApiKey));
            params.put("termid",place);
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getNotifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    notificationDetail.clear();
                    if (jo.getString("status").equals("ok")){
                        JSONArray jsonArray = jo.getJSONArray("details");
                        if (jsonArray.length() !=0){
                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                LessonsDataModel dataModel = new LessonsDataModel();

                                dataModel.setLessonId(object.getString("postid"));
                                dataModel.setLesson_title(object.getString("title"));
                                dataModel.setLesson_description(object.getString("description"));
                                dataModel.setLesson_image_url(object.getString("image"));

                                notificationDetail.add(dataModel);
                            }
                            hocLoadingDialog.hideDialog();
                            imageViewPop(notificationDetail);
                        }

                    } else {
                        Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
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
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public void imageViewPop(ArrayList<LessonsDataModel> notificationDetail){
        final Dialog dialog = new Dialog(NotificationsActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.hamstech_dialogue);
        dialog.setCancelable(true);

        imageView = dialog.findViewById(R.id.imageView);
        imgCancel = dialog.findViewById(R.id.imgCancel);
        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtDescription = dialog.findViewById(R.id.txtDescription);

        Glide.with(NotificationsActivity.this)

                .load(notificationDetail.get(0).getLesson_image_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        txtTitle.setText(notificationDetail.get(0).getLesson_title());
        txtDescription.setText(notificationDetail.get(0).getLesson_description());
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        dialog.show();
        postId = notificationDetail.get(0).getLesson_title();
        activityLog = "Notification Clicked";
        PagenameLog = "Notification";
        getLogEvent(NotificationsActivity.this);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            data.put("lesson",postId);
            data.put("activity",activityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentCourses = new Intent(NotificationsActivity.this, HomePageActivity.class);
        startActivity(intentCourses);
        finish();
    }
}
