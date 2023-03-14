package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.Params;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LessonsPageActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navSideMenu;
    LinearLayout txtPdfTitle,linearLessonContent,playerLayout;
    TextView headerTitle,txtDescription,txtLessonName,txtImageText;
    RelativeLayout layoutHeader,nextMainLayout,navigation;
    UserDataBase userDataBase;
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog,CourseId;
    ImageView imgLike,btnShare,imgLesson;
    TextView nextLessonTitle,previousLessonTitle;
    LinearLayout fabPrevious,fabNext;
    private String mp4URL = "",postStatus = "",postId,pdfURL,footerMenuStatus;
    ArrayList<LessonsDataModel> coursesList = new ArrayList<>();
    int intNext = 0,intPrevious = 0,mMenuId;
    SharedPreferences footerStatus;
    RecyclerView listItems;
    NavigationFragment navigationFragment;
    Button btnEnrolNow;
    CardView btnEnrollLesson;
    CircleImageView profile_image;
    LinearLayout linProfile;
    TextView txtUserMobile,txtUserName;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    Bundle params;
    AppEventsLogger logger;
    FirebaseAnalytics firebaseAnalytics;
    SharedPrefsUtils sharedPrefsUtils;
    ImageButton stickyWhatsApp;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.normal_lessons_page);

        drawer = findViewById(R.id.drawer_layout);
        navSideMenu = findViewById(R.id.navSideMenu);
        headerTitle = findViewById(R.id.headerTitle);
        txtLessonName = findViewById(R.id.txtLessonName);
        txtDescription = findViewById(R.id.txtDescription);
        layoutHeader = findViewById(R.id.layoutHeader);
        linearLessonContent = findViewById(R.id.linearLessonContent);
        btnEnrollLesson = findViewById(R.id.btnEnrollLesson);
        imgLike = findViewById(R.id.imgLike);
        btnShare = findViewById(R.id.btnShare);
        txtImageText = findViewById(R.id.txtImageText);
        imgLesson = findViewById(R.id.imgLesson);
        txtPdfTitle = findViewById(R.id.txtPdfTitle);
        nextLessonTitle = findViewById(R.id.nextLessonTitle);
        previousLessonTitle = findViewById(R.id.previousLessonTitle);
        fabPrevious = findViewById(R.id.fabPrevious);
        fabNext = findViewById(R.id.fabNext);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        playerLayout = findViewById(R.id.playerLayout);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        navigation = findViewById(R.id.navigation);

        nextMainLayout = findViewById(R.id.nextMainLayout);

        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        userDataBase = new UserDataBase(this);
        params = new Bundle();
        logger = AppEventsLogger.newLogger(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        sharedPrefsUtils = new SharedPrefsUtils(this, getString(R.string.app_name));

        coursesList = (ArrayList<LessonsDataModel>) getIntent().getSerializableExtra("LessonData");

        intNext = getIntent().getIntExtra("intNext",0);

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Lessons page").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Lessons page")
                    .commit();
        }

        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                CategoryName = coursesList.get(intNext).getCategory_name();
                CourseLog = coursesList.get(intNext).getCourse_title();
                LessonLog = coursesList.get(intNext).getLesson_title();
                youTubePlayerView.setVisibility(View.VISIBLE);
                playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
                player.loadVideo(mp4URL,0);
            }
            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if (state.toString().equals("PLAYING")){
                    PagenameLog = "Video start";
                    getLogEvent(LessonsPageActivity.this);
                } else if (state.toString().equals("PAUSED")){
                    PagenameLog = "Video paused";
                    getLogEvent(LessonsPageActivity.this);
                } else if (state.toString().equals("STOPPED")){
                    PagenameLog = "Video stopped";
                    getLogEvent(LessonsPageActivity.this);
                } else if (state.toString().equals("ENDED")){
                    PagenameLog = "Video Ended";
                    getLogEvent(LessonsPageActivity.this);
                }
            }
        });
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        watchNext();

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postStatus.equalsIgnoreCase("1")){
                    PagenameLog = "Lesson like";
                    getLogEvent(LessonsPageActivity.this);
                    getLessonsLikeDislike(LessonsPageActivity.this,postId,postStatus);
                } else if (postStatus.equalsIgnoreCase("0")){
                    PagenameLog = "Lesson unlike";
                    getLessonsLikeDislike(LessonsPageActivity.this,postId,postStatus);
                    getLogEvent(LessonsPageActivity.this);
                }
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4URL = getIntent().getStringExtra("videoURL");
                PagenameLog = "Url Share";
                String url = "https://www.youtube.com/watch?v="+mp4URL;
                getLogEvent(LessonsPageActivity.this);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(shareIntent);
            }
        });
        btnEnrollLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollFunction(LessonsPageActivity.this);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchNext();
            }
        });

        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchPrevious();
            }
        });
        txtPdfTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) player.pause();
                if (pdfURL.equals("")) {
                    txtPdfTitle.setVisibility(View.GONE);
                } else {
                    ActivityLog = "Pdf";PagenameLog = "Lesson Page";
                    getLogEvent(LessonsPageActivity.this);
                    pdfDialogue(LessonsPageActivity.this);
                }
            }
        });
        UserDataConstants.lessonId = "";
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

    }


    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
        if (newConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutHeader.setVisibility(View.GONE);
            txtLessonName.setVisibility(View.GONE);
            txtDescription.setVisibility(View.GONE);
            linearLessonContent.setVisibility(View.GONE);
            stickyWhatsApp.setVisibility(View.GONE);
            navigation.setVisibility(View.GONE);
            imgLesson.setVisibility(View.GONE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        } else {
            layoutHeader.setVisibility(View.VISIBLE);
            txtLessonName.setVisibility(View.VISIBLE);
            txtDescription.setVisibility(View.VISIBLE);
            txtImageText.setVisibility(View.VISIBLE);
            linearLessonContent.setVisibility(View.VISIBLE);
            imgLesson.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.VISIBLE);
            imgLesson.setVisibility(View.VISIBLE);
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
        }
    }
    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player!=null) player.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player!=null) player.pause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (player!=null) player.pause();
    }

    @Override
    protected void onRestart() {
        if (player!=null) player.pause();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void enrollFunction(Context context){
        CategoryName = getIntent().getStringExtra("CategoryName");
        CourseLog = getIntent().getStringExtra("CourseName");
        LessonLog = "";
        ActivityLog = "Enroll now";PagenameLog = "Lessons Page";
        getLogEvent(LessonsPageActivity.this);
        new AppsFlyerEventsHelper(LessonsPageActivity.this).EventEnroll();
        Intent intent = new Intent(LessonsPageActivity.this,EnrolNowActivity.class);
        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
        intent.putExtra("getCourseId",Integer.parseInt(LessonsPageActivity.this.getIntent().getStringExtra("CourseId")));
        startActivity(intent);
    }

    public void pdfDialogue(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.pdf_webview);
        dialog.setCancelable(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        Button btnDownload = dialog.findViewById(R.id.btnDownload);
        WebView webview = dialog.findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAllowFileAccess(true);

        String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+pdfURL+"'"+" width='100%' height='100%' style='border: none;'></iframe>";
        webview.loadData( doc , "text/html", "UTF-8");
        dialog.show();

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppsFlyerEventsHelper(LessonsPageActivity.this).EventEnroll();
                Intent intent = new Intent(LessonsPageActivity.this,EnrolNowActivity.class);
                sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
                intent.putExtra("getCourseId",Integer.parseInt(CourseId));
                startActivity(intent);
            }
        });
    }

    public void getLessonsLikeDislike(final Context context, String postValue, final String poststatus){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","lessons");
            params.put("apikey",context.getResources().getString(R.string.lblApiKey));
            params.put("phone", UserDataConstants.userMobile);
            params.put("postid",postValue);
            params.put("status",poststatus);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.save_like_dislike, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    if (jo.getString("status").equalsIgnoreCase("like")){
                        postStatus = "0";
                        imgLike.setImageResource(R.drawable.ic_like);
                    } else if (jo.getString("status").equals("0")){
                        postStatus = "1";
                        imgLike.setImageResource(R.drawable.ic_unlike);
                    } else if (jo.getString("status").equals("")){
                        postStatus = "1";
                        imgLike.setImageResource(R.drawable.ic_unlike);
                    }

                } catch(Exception e) {
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
        queue.add(sr);
    }

    private void playVideoAtSelection() {
        logLessonWatchedEvent(CourseLog,LessonLog);
        if (player != null){
            youTubePlayerView.setVisibility(View.VISIBLE);
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
            player.loadVideo(mp4URL,0);
        }
    }

    public void watchNext(){
        if (intNext<coursesList.size()){
            intPrevious = intNext;
            intNext = intNext+1;
            fabNext.setVisibility(View.VISIBLE);

            if (intNext == coursesList.size()){
                fabNext.setVisibility(View.GONE);
                intNext = 0;
            } else {
                nextLessonTitle.setText(coursesList.get(intNext).getLesson_description());
            }
            if (intNext-1 == 0){
                fabPrevious.setVisibility(View.GONE);
            } else {
                fabPrevious.setVisibility(View.VISIBLE);
                previousLessonTitle.setText(coursesList.get(intPrevious-1).getLesson_description());
            }
            nextContentLoad(intPrevious);
        } else if (intNext >= coursesList.size()){
            intNext = 0;
            fabNext.setVisibility(View.GONE);
        }
    }

    public void watchPrevious(){
        intPrevious = intPrevious-1;
        if (intPrevious<coursesList.size()-1){
            intNext = intPrevious + 1;
            previousContentLoad(intPrevious);
            if (intPrevious == coursesList.size()-1 || intPrevious == 0){
                fabPrevious.setVisibility(View.GONE);
            }
            if (intPrevious == 0){
                fabPrevious.setVisibility(View.GONE);
                fabNext.setVisibility(View.GONE);
            } else {
                fabPrevious.setVisibility(View.VISIBLE);
                nextLessonTitle.setText(coursesList.get(intNext).getLesson_description());
                previousLessonTitle.setText(coursesList.get(intPrevious-1).getLesson_description());
            }
            if (intNext < coursesList.size()){
                fabNext.setVisibility(View.VISIBLE);
            } else {
                fabNext.setVisibility(View.GONE);
            }
        } else if (intPrevious == (coursesList.size()-1)){
            intPrevious = 0;
            fabPrevious.setVisibility(View.GONE);
        }
    }

    public void nextContentLoad(int listPosition){
        headerTitle.setText(coursesList.get(listPosition).getCourse_title());
        txtDescription.setText(coursesList.get(listPosition).getLesson_description());
        txtLessonName.setText(coursesList.get(listPosition).getLesson_title());
        txtImageText.setText(coursesList.get(listPosition).getTextImage());
        CategoryName = coursesList.get(listPosition).getCategory_name();
        CourseLog = coursesList.get(listPosition).getCourse_title();
        LessonLog = coursesList.get(listPosition).getLesson_title();
        CourseId = coursesList.get(listPosition).getCourseId();
        postId = coursesList.get(listPosition).getLessonId();
        pdfURL = coursesList.get(listPosition).getStudy_material_url();

        if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("1")) {
            imgLike.setImageResource(R.drawable.ic_like);
            imgLike.setEnabled(false);
        } else if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("0")) {
            imgLike.setImageResource(R.drawable.ic_unlike);
            imgLike.setEnabled(true);
        }

        ActivityLog = "Lessons Page";
        PagenameLog = "Watch Next";
        getLogEvent(LessonsPageActivity.this);
        AppsFlyerEvent(listPosition);
        new AppsFlyerEventsHelper(LessonsPageActivity.this).EventLessons(coursesList.get(listPosition).getCategory_name(),
                coursesList.get(listPosition).getCourse_title(),coursesList.get(listPosition).getLesson_title());
        ActivityLog = getIntent().getStringExtra("LessonName");
        linearLessonContent.setVisibility(View.VISIBLE);

        if (coursesList.get(listPosition).getLessonImage().equals("")){
            imgLesson.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(coursesList.get(listPosition).getLessonImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgLesson);

            imgLesson.setVisibility(View.VISIBLE);
        }

        if (coursesList.get(listPosition).getStudy_material_url().equals("")) {
            txtPdfTitle.setVisibility(View.GONE);
        } else {
            txtPdfTitle.setVisibility(View.VISIBLE);
        }
        if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("1")){
            postStatus = "0";
        } else if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("0")) {
            postStatus = "1";
        }
        //getLessonsLikeDislike(this,postId,postStatus);
        mp4URL = coursesList.get(listPosition).getLesson_video_url();
        playVideoAtSelection();
    }

    public void previousContentLoad(int listPosition){
        headerTitle.setText(coursesList.get(listPosition).getCourse_title());
        txtDescription.setText(coursesList.get(listPosition).getLesson_description());
        txtLessonName.setText(coursesList.get(listPosition).getLesson_title());
        txtImageText.setText(coursesList.get(listPosition).getTextImage());
        CategoryName = coursesList.get(listPosition).getCategory_name();
        CourseLog = coursesList.get(listPosition).getCourse_title();
        LessonLog = coursesList.get(listPosition).getLesson_title();
        CourseId = coursesList.get(listPosition).getCourseId();
        postId = coursesList.get(listPosition).getLessonId();
        pdfURL = coursesList.get(listPosition).getStudy_material_url();

        if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("1")) {
            imgLike.setImageResource(R.drawable.ic_like);
            imgLike.setEnabled(false);
        } else if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("0")) {
            imgLike.setImageResource(R.drawable.ic_unlike);
            imgLike.setEnabled(true);
        }

        ActivityLog = "Lessons Page";
        PagenameLog = "Watch Previous";
        getLogEvent(LessonsPageActivity.this);
        AppsFlyerEvent(listPosition);
        new AppsFlyerEventsHelper(LessonsPageActivity.this).EventLessons(coursesList.get(listPosition).getCategory_name(),
                coursesList.get(listPosition).getCourse_title(),coursesList.get(listPosition).getLesson_title());
        ActivityLog = getIntent().getStringExtra("LessonName");
        linearLessonContent.setVisibility(View.VISIBLE);

        if (coursesList.get(listPosition).getLessonImage().equals("")){
            imgLesson.setVisibility(View.GONE);
        } else {
            Glide.with(this)
                    .load(coursesList.get(listPosition).getLessonImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(imgLesson);

            imgLesson.setVisibility(View.VISIBLE);
        }

        if (coursesList.get(listPosition).getStudy_material_url().equals("")) {
            txtPdfTitle.setVisibility(View.GONE);
        } else {
            txtPdfTitle.setVisibility(View.VISIBLE);
        }
        if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("1")){
            postStatus = "0";
        } else if (coursesList.get(listPosition).getLesson_liked().equalsIgnoreCase("0")) {
            postStatus = "1";
        }
        //getLessonsLikeDislike(this,postId,postStatus);

        mp4URL = coursesList.get(listPosition).getLesson_video_url();
        playVideoAtSelection();
    }

    public void logContactusEvent(String eventValue){
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, eventValue);
        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
        params.putString(Params.CONTENT_TYPE, eventValue);
        firebaseAnalytics.logEvent("contact_us", params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(eventValue);
    }
    public void logLessonWatchedEvent (String course, String lessonName) {
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, course);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, lessonName);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL, params);
        params.putString(Params.COURSE_NAME, course);
        params.putString(Params.LESSON_NAME, lessonName);
        firebaseAnalytics.logEvent(Params.LESSONS_WATCHED, params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(course);
    }

    public void AppsFlyerEvent(int position){
        if (position == 0){
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCategory_name(),
                    coursesList.get(position).getCourse_title(),coursesList.get(position).getLesson_title(),"Trial Class 1");
        } else if (position == 1){
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCategory_name(),
                    coursesList.get(position).getCourse_title(),coursesList.get(position).getLesson_title(),"Trial Class 2");
        } else if (position == 2){
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCategory_name(),
                    coursesList.get(position).getCourse_title(),coursesList.get(position).getLesson_title(),"How you learn");
        } else if (position == 3){
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCategory_name(),
                    coursesList.get(position).getCourse_title(),coursesList.get(position).getLesson_title(),"Student testimonial");
        }
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category",CategoryName);
            data.put("course",CourseLog);
            data.put("lesson",txtLessonName.getText().toString());
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
        //startActivity(new Intent(LessonsPageActivity.this, HomePageActivity.class));
        /*if (!UserDataConstants.lessonId.equals("")){

        } else LessonsPageActivity.this.finish();*/
    }
}
