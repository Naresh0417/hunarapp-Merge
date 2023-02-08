package com.hamstechonline.activities;

import android.app.Activity;
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

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.adapters.CourseViewPagerAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.fragments.SearchFragment;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.NonSwipeableViewPager;
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

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CoursePageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List tabs;
    TabLayout tabLayout;
    NonSwipeableViewPager viewPager_home;
    CourseViewPagerAdapter courseViewPagerAdapter;
    DrawerLayout drawer;
    NavigationView navSideMenu;
    NavigationFragment navigationFragment;
    RelativeLayout layoutHeader;
    CheckBox imgSearch;
    TextView headerTitle,txtCourseName, txtLessons, txtDetails,btnEnrollNow;
    View view;
    SearchFragment searchFragment;
    UserDataBase userDataBase;
    int mMenuId;
    String CategoryName,CatId = "",CourseLog="",LessonLog = "",ActivityLog="",PagenameLog="",langPref = "Language", mobile = "",
            fullname = "",email = "",footerMenuStatus;
    SharedPreferences prefs,footerStatus;
    private Locale myLocale;
    YouTubePlayer player;
    String mp4URL = "";
    LogEventsActivity logEventsActivity;
    YouTubePlayerView youTubePlayerView;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;
    SharedPrefsUtils sharedPrefsUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.course_activity);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager_home = findViewById(R.id.view_pager);
        navSideMenu = findViewById(R.id.navSideMenu);
        drawer = findViewById(R.id.drawer_layout);
        imgSearch = findViewById(R.id.imgSearch);
        headerTitle = findViewById(R.id.headerTitle);
        view = findViewById(R.id.searchLayout);
        txtCourseName = findViewById(R.id.txtCourseName);
        layoutHeader = findViewById(R.id.layoutHeader);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        txtLessons = findViewById(R.id.txtLessons);
        txtDetails = findViewById(R.id.txtDetails);
        btnEnrollNow = findViewById(R.id.btnEnrollNow);

        userDataBase = new UserDataBase(this);
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);
        tabs = Arrays.asList(getResources().getStringArray(R.array.courseTabsNames));
        ActivityLog = "Course Page";

        logEventsActivity = new LogEventsActivity();

        sharedPrefsUtils = new SharedPrefsUtils(CoursePageActivity.this, getString(R.string.app_name));

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
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        if (getIntent().getStringExtra("CategoryName") != null) {
            headerTitle.setText(getIntent().getStringExtra("CourseName"));
            txtCourseName.setText(getIntent().getStringExtra("description"));
            CategoryName = getIntent().getStringExtra("CategoryName");
            CourseLog = getIntent().getStringExtra("CourseName");
            CatId = getIntent().getStringExtra("CategoryId");
        }
        if (getIntent().getStringExtra("notificationId")!= null){
            PagenameLog = "Course Page";
            LessonLog = UserDataConstants.notificationTitle;
            ActivityLog = "Notification Clicked";
            CourseLog = "";
            CategoryName = "";
            getLogEvent(this);
            ActivityLog = "Course Page";
        }
        getLifecycle().addObserver(youTubePlayerView);
        if (getIntent().getStringExtra("VideoUrl") != null) {
            mp4URL = getIntent().getStringExtra("VideoUrl");
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    player = youTubePlayer;
                    player.loadVideo(mp4URL, 0);
                }
                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state.toString().equals("PLAYING")){
                        PagenameLog = "Video start";
                        getLogEvent(CoursePageActivity.this);
                    } else if (state.toString().equals("PAUSED")){
                        PagenameLog = "Video paused";
                        getLogEvent(CoursePageActivity.this);
                    } else if (state.toString().equals("STOPPED")){
                        PagenameLog = "Video stopped";
                        getLogEvent(CoursePageActivity.this);
                    } else if (state.toString().equals("ENDED")){
                        PagenameLog = "Video Ended";
                        getLogEvent(CoursePageActivity.this);
                    }
                }
            });
        }
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

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                ActivityLog = "Sticky whatsapp";
                PagenameLog = "Course page";
                getLogEvent(CoursePageActivity.this);
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

        btnEnrollNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Enroll now for full course";
                PagenameLog = "Course page";
                getLogEvent(CoursePageActivity.this);
                EnrolNow(Integer.parseInt(CatId));
            }
        });


        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

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
        imgSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    headerTitle.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    searchFragment = SearchFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.searchLayout, searchFragment, "")
                            .commit();
                } else {
                    headerTitle.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                }
            }
        });

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Course page").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Course page")
                    .commit();
        }

        for (int i=0;i<tabs.size();i++) {
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tabs, tabLayout, false);
            TextView tabTextView = relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tabs.get(i).toString());

            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout));
        }

        courseViewPagerAdapter=new CourseViewPagerAdapter(getSupportFragmentManager());
        viewPager_home.setAdapter(courseViewPagerAdapter);

        viewPager_home.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager_home.setCurrentItem(tab.getPosition());
                ActivityLog = tabs.get(tab.getPosition()).toString();
                PagenameLog = "Tab selected";
                getLogEvent(CoursePageActivity.this);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        txtLessons.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
        txtLessons.setTextColor(getResources().getColor(R.color.dark_pink));
        txtDetails.setBackgroundResource(0);
        txtDetails.setTextColor(getResources().getColor(R.color.muted_blue));
        viewPager_home.setCurrentItem(0);

        txtLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtLessons.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
                txtLessons.setTextColor(getResources().getColor(R.color.dark_pink));
                txtDetails.setBackgroundResource(0);
                txtDetails.setTextColor(getResources().getColor(R.color.muted_blue));
                ActivityLog = "Trial Lessons";
                PagenameLog = "Tab selected";
                viewPager_home.setCurrentItem(0);
                getLogEvent(CoursePageActivity.this);
            }
        });
        txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDetails.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
                txtDetails.setTextColor(getResources().getColor(R.color.dark_pink));
                txtLessons.setBackgroundResource(0);
                txtLessons.setTextColor(getResources().getColor(R.color.muted_blue));
                ActivityLog = "Course Details";
                PagenameLog = "Tab selected";
                viewPager_home.setCurrentItem(1);
                getLogEvent(CoursePageActivity.this);
            }
        });

    }

    public void EnrolNow(int catId){
        new AppsFlyerEventsHelper(CoursePageActivity.this).EventEnroll();
        Intent intent = new Intent(CoursePageActivity.this,EnrolNowActivity.class);
        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
        intent.putExtra("getCourseId",catId);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
        if (newConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutHeader.setVisibility(View.GONE);
            txtCourseName.setVisibility(View.GONE);
        } else {
            layoutHeader.setVisibility(View.VISIBLE);
            txtCourseName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void EnrolNow(View view){
        new AppsFlyerEventsHelper(CoursePageActivity.this).EventEnroll();
        Intent intent = new Intent(CoursePageActivity.this,EnrolNowActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        if (player!=null) player.play();
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
        super.onRestart();
        if (player!=null) player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",email);
            data.put("category",CategoryName);
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
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
        CoursePageActivity.this.finish();
    }
}
