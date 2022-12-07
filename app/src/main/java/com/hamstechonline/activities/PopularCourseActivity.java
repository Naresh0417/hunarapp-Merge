package com.hamstechonline.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.Answer;
import com.hamstechonline.datamodel.CourseDetailsResponse;
import com.hamstechonline.datamodel.CourseOverview;
import com.hamstechonline.datamodel.ExpertFaculty;
import com.hamstechonline.datamodel.Faqs;
import com.hamstechonline.datamodel.LessonData;
import com.hamstechonline.datamodel.Question;
import com.hamstechonline.datamodel.Testimonials;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.fragments.SearchFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.DynamicWhatsAppChat;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.SocialMediaEventsHelper;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;

public class PopularCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navSideMenu;
    NavigationFragment navigationFragment;
    BottomNavigationView navigation;
    RelativeLayout layoutHeader;
    CheckBox imgSearch, checkboxOverview,txtOverviewExpand;
    TextView headerTitle, txtCourseName;
    SearchFragment searchFragment;
    UserDataBase userDataBase;
    int mMenuId;
    String CourseId, CategoryName, CourseLog, LessonLog = "", ActivityLog, PagenameLog, langPref = "Language", language, mobile = "",
            fullname = "", email = "";
    SharedPreferences prefs;
    private Locale myLocale;
    YouTubePlayer player;
    String mp4URL = "";
    LogEventsActivity logEventsActivity;
    YouTubePlayerView youTubePlayerView;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;
    private ImageView imgPrevious, imgNext;

    private LinearLayout mainLayout,bottomView,playerLayout;
    private ApiInterface apiService;
    private HocLoadingDialog hocLoadingDialog;

    private TextView textCertStudents;
    private ImageView imgRibbon;
    private ImageView imgFaculty;
    private TextView textFacultyName;
    private TextView textFacultyDesc;

    private ImageView imageHunar;

    private RecyclerView listCourses;
    private OverviewAdapter overviewAdapter;

    private YouTubePlayer topicsPlayer;
    private RecyclerView listTopics;
    private TopicsAdapter topicsAdapter;

    private ViewPager testimonialViewPager;
    private Runnable updateBanner;
    private Timer timerBanner;
    private Handler handlerBanner;
    private int currentPageBanner;

    private int playerPos = 0;
    private boolean playerIsPlay = false;
    private YouTubePlayer testimonialPlayer;
    private TestimonialsPagerAdapter testimonialsPagerAdapter;

    private TextView textAmount, textInstAmount;
    private StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    private DecimalFormat decimalFormat = new DecimalFormat("##,##,##,###");

    private List<Faqs> faqsList = new ArrayList<>();
    private RecyclerView faqRecycleView;
    private FaqsAdapter faqsAdapter;

    private String m_strEmail = "";
    private SharedPrefsUtils sharedPrefsUtils;
    DynamicWhatsAppChat dynamicWhatsAppChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.popular_course_details_activity);

        timerBanner = new Timer();
        handlerBanner = new Handler();

        navSideMenu = findViewById(R.id.navSideMenu);
        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        imgSearch = findViewById(R.id.imgSearch);
        headerTitle = findViewById(R.id.headerTitle);
        txtCourseName = findViewById(R.id.txtCourseName);
        layoutHeader = findViewById(R.id.layoutHeader);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        textCertStudents = findViewById(R.id.textCount);
        imageHunar = findViewById(R.id.imageHunar);
        listCourses = findViewById(R.id.listCourses);
        listTopics = findViewById(R.id.listTopics);
        imgRibbon = findViewById(R.id.imgRibbon);
        imgFaculty = findViewById(R.id.imgFaculty);
        textFacultyDesc = findViewById(R.id.textFacultyDesc);
        textFacultyName = findViewById(R.id.textFacultyName);
        imgPrevious = findViewById(R.id.imgPrevious);
        imgNext = findViewById(R.id.imgNext);
        testimonialViewPager = findViewById(R.id.imageSlider);
        mainLayout = findViewById(R.id.mainLayout);
        bottomView = findViewById(R.id.bottomView);
        checkboxOverview = findViewById(R.id.checkboxOverview);
        txtOverviewExpand = findViewById(R.id.txtOverviewExpand);
        playerLayout = findViewById(R.id.playerLayout);

        textAmount = findViewById(R.id.textAmount);
        textInstAmount = findViewById(R.id.textInstAmount);
        faqRecycleView = findViewById(R.id.faqRecycleView);

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        hocLoadingDialog = new HocLoadingDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        userDataBase = new UserDataBase(this);
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);
        ActivityLog = "Course Page";
        sharedPrefsUtils = new SharedPrefsUtils(this, getString(R.string.app_name));

        if (langPref.equals("en")) {
            language = "english";
        } else {
            language = "hindi";
        }

        logEventsActivity = new LogEventsActivity();

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

        if (getIntent().getStringExtra("CategoryName") != null) {
            headerTitle.setText(getIntent().getStringExtra("CourseName"));
            txtCourseName.setText(getIntent().getStringExtra("description"));
            CategoryName = getIntent().getStringExtra("CategoryName");
            CourseLog = getIntent().getStringExtra("CourseName");
            CourseId = getIntent().getStringExtra("CategoryId");
        }
        if (getIntent().getStringExtra("notificationId") != null) {
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
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
                    player.loadVideo(mp4URL, 0);
                }

                @Override
                public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    if (state.toString().equals("PLAYING")) {
                        PagenameLog = "Video start";
                        getLogEvent(PopularCourseActivity.this);
                    } else if (state.toString().equals("PAUSED")) {
                        PagenameLog = "Video paused";
                        getLogEvent(PopularCourseActivity.this);
                    } else if (state.toString().equals("STOPPED")) {
                        PagenameLog = "Video stopped";
                        getLogEvent(PopularCourseActivity.this);
                    } else if (state.toString().equals("ENDED")){
                        PagenameLog = "Video Ended";
                        getLogEvent(PopularCourseActivity.this);
                    }
                }
            });
        }
        checkboxOverview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listCourses.setVisibility(View.VISIBLE);
                } else {
                    listCourses.setVisibility(View.GONE);
                }
            }
        });
        txtOverviewExpand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxOverview.setChecked(true);
                    listCourses.setVisibility(View.VISIBLE);
                } else {
                    checkboxOverview.setChecked(false);
                    listCourses.setVisibility(View.GONE);
                }
            }
        });
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mainLayout.setVisibility(View.GONE);
                bottomView.setVisibility(View.GONE);
                stickyWhatsApp.setVisibility(View.GONE);
                navigation.setVisibility(View.GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                mainLayout.setVisibility(View.VISIBLE);
                bottomView.setVisibility(View.VISIBLE);
                stickyWhatsApp.setVisibility(View.VISIBLE);
                navigation.setVisibility(View.VISIBLE);
            }
        });

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWhatsAppChat = new DynamicWhatsAppChat(PopularCourseActivity.this,"Popular lessons",CourseLog,"");
                dynamicWhatsAppChat.getChatNumber(userDataBase.getUserMobileNumber(1));
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
                    searchFragment = SearchFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.searchLayout, searchFragment, "")
                            .commit();
                } else {
                    headerTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgNextData();
            }
        });
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPreviousData();
            }
        });

        getResponse();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
        if (newConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navigation.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.GONE);
            txtCourseName.setVisibility(View.GONE);
            stickyWhatsApp.setVisibility(View.GONE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        } else {
            navigation.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
            stickyWhatsApp.setVisibility(View.VISIBLE);
            txtCourseName.setVisibility(View.GONE);
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
        }
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mMenuId = item.getItemId();
        for (int i = 0; i < navigation.getMenu().size(); i++) {
            MenuItem menuItem = navigation.getMenu().getItem(i);
            boolean isChecked = menuItem.getItemId() == item.getItemId();
            menuItem.setChecked(isChecked);
        }

        switch (item.getItemId()) {
            case R.id.navigation_home:
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                Intent intentCourses = new Intent(PopularCourseActivity.this, HomePageActivity.class);
                startActivity(intentCourses);
                PopularCourseActivity.this.finish();
                return true;
            case R.id.navigation_chat:
                PagenameLog = "chat with whatsapp";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);

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
                Intent enrol = new Intent(PopularCourseActivity.this, SuccessStoryActivity.class);
                startActivity(enrol);
                PopularCourseActivity.this.finish();
                return true;
            case R.id.navigation_today:
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                Intent hamstech = new Intent(PopularCourseActivity.this, BuzzActivity.class);
                startActivity(hamstech);
                PopularCourseActivity.this.finish();
                return true;
            case R.id.navigation_aboutus:
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
                new AppsFlyerEventsHelper(this).EventContactus();
                Intent about = new Intent(PopularCourseActivity.this, ContactActivity.class);
                startActivity(about);
                PopularCourseActivity.this.finish();
                return true;
        }
        return false;
    }

    public void sideMenu(View view) {
        drawer.openDrawer(Gravity.LEFT);
    }

    public void EnrolNow(View view) {

        CategoryName = getIntent().getStringExtra("CategoryName");
        CourseLog = getIntent().getStringExtra("description");
        LessonLog = "";
        ActivityLog = "Watch now";
        PagenameLog = "Popular lessons";
        new SocialMediaEventsHelper(PopularCourseActivity.this).EventAccordion(CourseLog);
        //logAccordionEvent(CourseLog,ActivityLog);
        getLogEvent(PopularCourseActivity.this);
        new AppsFlyerEventsHelper(PopularCourseActivity.this).EventEnroll();
        Intent intent = new Intent(PopularCourseActivity.this, MiniLessonsEnrolNowActivity.class);
        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
        intent.putExtra("getCourseId", Integer.parseInt(CourseId));
        intent.putExtra("CategoryName", CategoryName);
        intent.putExtra("Email", m_strEmail);
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
        if (player != null) player.play();
        if (testimonialPlayer != null) testimonialPlayer.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) player.pause();
        if (topicsPlayer != null) topicsPlayer.pause();
        if (testimonialPlayer != null) testimonialPlayer.pause();
    }

    @Override
    protected void onStop() {
        timerBanner.cancel();
        super.onStop();
        if (player != null) player.pause();
        if (topicsPlayer != null) topicsPlayer.pause();
        if (testimonialPlayer != null) testimonialPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (player != null) player.play();
        if (testimonialPlayer != null) testimonialPlayer.play();
    }

    @Override
    protected void onDestroy() {
        timerBanner.cancel();
        super.onDestroy();
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
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    public void getLogEvent(Context context) {
        JSONObject data = new JSONObject();
        try {
            data.put("apikey", getResources().getString(R.string.lblApiKey));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PopularCourseActivity.this.finish();
    }

    public void getResponse() {
        hocLoadingDialog.showLoadingDialog();
        CourseDetailsResponse courseDetailsResponse = new CourseDetailsResponse("Hamstech", "course",
                getResources().getString(R.string.lblApiKey), CourseId, language, langPref, userDataBase.getUserMobileNumber(1), "no");
        Call<CourseDetailsResponse> call = apiService.getCourseDetails(courseDetailsResponse);
        call.enqueue(new Callback<CourseDetailsResponse>() {
            @Override
            public void onResponse(Call<CourseDetailsResponse> call, retrofit2.Response<CourseDetailsResponse> response) {

                if (response.body().getStatus() != null) {

                    textCertStudents.setText(response.body().getCertifiedStudents() + "+");

                    Glide.with(PopularCourseActivity.this)
                            .asGif()
                            .load(response.body().getHunarStudents())
                            .error(R.mipmap.ic_launcher)
                            .into(imageHunar);

                    String stike = getResources().getString(R.string.strike_line);
                    textAmount.setText("\u20B9" + stike);
                    textAmount.setPaintFlags(textAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    textInstAmount.setText("\u20B9" + response.body().getCourseDetails().getAmount());

                    if (response.body().getCourseOverview() != null) {
                        overviewAdapter = new OverviewAdapter(PopularCourseActivity.this, response.body().getCourseOverview());

                        listCourses.setLayoutManager(new LinearLayoutManager(PopularCourseActivity.this, RecyclerView.VERTICAL, false));
                        listCourses.addItemDecoration(new GridSpacingItemDecoration(1, 5, false));
                        listCourses.setAdapter(overviewAdapter);
                    }

                    mainLayout.setVisibility(View.VISIBLE);

                    if (response.body().getLessonData() != null) {
                        topicsAdapter = new TopicsAdapter(PopularCourseActivity.this, response.body().getLessonData());

                        listTopics.setLayoutManager(new LinearLayoutManager(PopularCourseActivity.this, RecyclerView.VERTICAL, false));
                        listTopics.addItemDecoration(new GridSpacingItemDecoration(1, 5, false));
                        listTopics.setAdapter(topicsAdapter);
                    }

                    ExpertFaculty expertFaculty = response.body().getExpertFaculty();
                    if (expertFaculty != null) {
                        textFacultyName.setText(expertFaculty.getFacultyName());
                        textFacultyDesc.setText(expertFaculty.getFacultyDescription());

                        Glide.with(PopularCourseActivity.this)
                                .load(expertFaculty.getFacultyImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.mipmap.ic_launcher)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))
                                .into(imgFaculty);
                    }

                    /*storiesSliderCardPagerAdapter = new StoriesSliderCardPagerAdapter(PopularCourseActivity.this, response.body().getSuccessStories());
                    sliderView.setAdapter(storiesSliderCardPagerAdapter);*/

                    testimonialsPagerAdapter = new TestimonialsPagerAdapter(PopularCourseActivity.this, response.body().getTestimonials());
                    testimonialViewPager.setAdapter(testimonialsPagerAdapter);
                    playerPos = 0;
                    playerIsPlay = true;
                    testimonialViewPager.setCurrentItem(playerPos, true);

                    imgRibbon.setVisibility(View.VISIBLE);
                    Glide.with(PopularCourseActivity.this)
                            .load(response.body().getFooterRibbonImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(imgRibbon);

                    /*updateBanner = new Runnable() {
                        public void run() {
                            if (storiesSliderCardPagerAdapter.getCount() == currentPageBanner) {
                                currentPageBanner = 0;
                            }
                            sliderView.setCurrentItem(currentPageBanner++, true);
                        }
                    };
                    timerBanner.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            handlerBanner.post(updateBanner);
                        }
                    }, 2000, 3000);*/
                }

                faqsList.clear();
                Faqs faqs = null;
                if (response.body().getQuestions() != null &&
                        response.body().getAnswers() != null) {
                    for (Question question : response.body().getQuestions()) {
                        for (Answer answer : response.body().getAnswers()) {
                            if (question.getFid().equalsIgnoreCase(answer.getFid())) {
                                faqs = new Faqs();
                                faqs.setFaqId(question.getFid());
                                faqs.setFaqTitle(question.getFaqTitle());
                                faqs.setFaqContent(answer.getFaqContent());
                                faqsList.add(faqs);
                                break;
                            }
                        }
                    }
                }

                if (faqsList.size() > 0) {
                    faqsAdapter = new FaqsAdapter(PopularCourseActivity.this, faqsList);

                    faqRecycleView.setLayoutManager(new LinearLayoutManager(PopularCourseActivity.this, RecyclerView.VERTICAL, false));
                    faqRecycleView.addItemDecoration(new GridSpacingItemDecoration(1, 5, false));
                    faqRecycleView.setAdapter(faqsAdapter);
                }

                m_strEmail = response.body().getEmail();

                hocLoadingDialog.hideDialog();
            }

            @Override
            public void onFailure(Call<CourseDetailsResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
                finish();
            }
        });
    }

    public void imgNextData() {
        if (testimonialsPagerAdapter.getCount() == currentPageBanner) {
            currentPageBanner = 0;
        }
        testimonialViewPager.setCurrentItem(currentPageBanner++, true);
    }

    public void imgPreviousData() {
        if (testimonialsPagerAdapter.getCount() == currentPageBanner) {
            currentPageBanner = 0;
        }
        testimonialViewPager.setCurrentItem(currentPageBanner--, true);
    }

    public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

        Context context;
        List<CourseOverview> coursesList;

        public OverviewAdapter(Context context, List<CourseOverview> coursesList) {
            this.context = context;
            this.coursesList = coursesList;
            logEventsActivity = new LogEventsActivity();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.course_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtTitle.setText(coursesList.get(position).getContent());
                Glide.with(context)
                        .load(coursesList.get(position).getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            TextView txtTitle;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.img_item);
                txtTitle = view.findViewById(R.id.tv_name);
            }
        }
    }

    public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {

        Context context;
        List<LessonData> coursesList;

        public TopicsAdapter(Context context, List<LessonData> coursesList) {
            this.context = context;
            this.coursesList = coursesList;
            logEventsActivity = new LogEventsActivity();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.course_topics_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtTitle.setText(coursesList.get(position).getLessonTitle());

                Glide.with(context)
                        .load(coursesList.get(position).getLessonImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_lock_video)
                        .into(holder.imgCategory);

                if (holder.getAdapterPosition() == 0) {
                    holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                    if (coursesList.get(position).getLockValue().equalsIgnoreCase("0")) {
                        holder.imgCategory.setVisibility(View.GONE);
                        holder.viewLine.setVisibility(View.GONE);
                        holder.playerView.setVisibility(View.VISIBLE);
                        holder.cardTopicVideo.setVisibility(View.VISIBLE);
                        playVideo(holder);
                    } else {
                        holder.playerView.setVisibility(View.GONE);
                        holder.cardTopicVideo.setVisibility(View.VISIBLE);
                        holder.imgCategory.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(coursesList.get(position).getLessonImageUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.ic_lock_video)
                                .into(holder.imgCategory);
                    }
                } else
                    holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);

                holder.titleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (holder.imgCategory.getVisibility() == View.VISIBLE || holder.playerView.getVisibility() == View.VISIBLE) {
                                holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                                holder.imgCategory.setVisibility(View.GONE);
                                holder.playerView.setVisibility(View.GONE);
                                holder.cardTopicVideo.setVisibility(View.GONE);
                                return;
                            } else {
                                holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                            }

                            if (coursesList.get(holder.getAdapterPosition()).getLockValue().equalsIgnoreCase("0")) {
                                holder.imgCategory.setVisibility(View.GONE);
                                holder.playerView.setVisibility(View.VISIBLE);
                                holder.cardTopicVideo.setVisibility(View.VISIBLE);
                                playVideo(holder);
                            } else {
                                holder.imgCategory.setVisibility(View.VISIBLE);
                                holder.playerView.setVisibility(View.GONE);
                                holder.cardTopicVideo.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.cardTopicVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryName = getIntent().getStringExtra("CategoryName");
                        CourseLog = coursesList.get(position).getLessonTitle();
                        LessonLog = "Topics";
                        ActivityLog = "Enrol now";
                        PagenameLog = "Popular lessons";
                        getLogEvent(PopularCourseActivity.this);
                        new AppsFlyerEventsHelper(context);
                        Intent intent = new Intent(context, MiniLessonsEnrolNowActivity.class);
                        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
                        intent.putExtra("getCourseId",Integer.parseInt(getIntent().getStringExtra("CategoryId")));
                        intent.putExtra("CategoryName",CategoryName);
                        startActivity(intent);
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            TextView txtTitle;
            ImageView imgArrow;
            RelativeLayout titleLayout;
            YouTubePlayerView playerView;
            CardView cardTopicVideo;
            View viewLine;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.img_item);
                txtTitle = view.findViewById(R.id.tv_name);
                imgArrow = view.findViewById(R.id.imgArrow);
                titleLayout = view.findViewById(R.id.titleLayout);
                playerView = view.findViewById(R.id.playerView);
                viewLine = view.findViewById(R.id.viewLine);
                cardTopicVideo = view.findViewById(R.id.cardTopicVideo);
            }
        }

        private void playVideo(ViewHolder holder) {
            try {
                getLifecycle().addObserver(holder.playerView);
                String videoUrl = coursesList.get(holder.getAdapterPosition()).getVideoUrl();
                holder.playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        topicsPlayer = youTubePlayer;
                        topicsPlayer.cueVideo(videoUrl, 0);
                    }

                    @Override
                    public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                        super.onStateChange(youTubePlayer, state);
                        if (state.toString().equals("PLAYING")) {
                            PagenameLog = "Video start";
                            getLogEvent(PopularCourseActivity.this);
                        } else if (state.toString().equals("PAUSED")) {
                            PagenameLog = "Video paused";
                            getLogEvent(PopularCourseActivity.this);
                        } else if (state.toString().equals("STOPPED")) {
                            PagenameLog = "Video stopped";
                            getLogEvent(PopularCourseActivity.this);
                        } else if (state.toString().equals("ENDED")){
                            PagenameLog = "Video Ended";
                            getLogEvent(PopularCourseActivity.this);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ViewHolder> {

        Context context;
        List<Faqs> coursesList;

        public FaqsAdapter(Context context, List<Faqs> coursesList) {
            this.context = context;
            this.coursesList = coursesList;
            logEventsActivity = new LogEventsActivity();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.course_faqs_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtTitle.setText(""+(position+1)+"."+coursesList.get(position).getFaqTitle());
                holder.txtContent.setText(coursesList.get(position).getFaqContent());
                holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);

                holder.titleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (holder.txtContent.getVisibility() == View.VISIBLE) {
                                holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_down);
                                holder.txtContent.setVisibility(View.GONE);
                            } else {
                                holder.imgArrow.setBackgroundResource(R.drawable.ic_arrow_up);
                                holder.txtContent.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            TextView txtContent;
            ImageView imgArrow;
            RelativeLayout titleLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                txtTitle = view.findViewById(R.id.tv_name);
                txtContent = view.findViewById(R.id.textAnswer);
                imgArrow = view.findViewById(R.id.imgArrow);
                titleLayout = view.findViewById(R.id.titleLayout);
            }
        }
    }


    public class TestimonialsPagerAdapter extends PagerAdapter {

        private Context context;
        private List<Testimonials> testimonialsList;

        public TestimonialsPagerAdapter(Context context, List<Testimonials> testimonialsList) {
            this.context = context;
            this.testimonialsList = testimonialsList;
        }

        @Override
        public int getCount() {
            return testimonialsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.course_testimonials_item, container, false);
            container.addView(view);
            bind(view, position);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void bind(View view, final int position) {
            ImageView imageView = view.findViewById(R.id.imageView);
            YouTubePlayerView playerView = view.findViewById(R.id.playerView);

            if (testimonialsList.get(position).getType().equalsIgnoreCase("image")) {
                playerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .asBitmap()
                        .load(testimonialsList.get(position).getSrc())
                        //.placeholder(R.drawable.duser1)
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);

                try {
                    getLifecycle().addObserver(playerView);
                    String videoUrl = testimonialsList.get(position).getSrc();
                    playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            testimonialPlayer = youTubePlayer;
                            testimonialPlayer.cueVideo(videoUrl, 0);
                        }

                        @Override
                        public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                            super.onStateChange(youTubePlayer, state);
                            LessonLog = "PL Testimonial";
                            if (state.toString().equals("PLAYING")) {
                                PagenameLog = "Video start";
                                getLogEvent(PopularCourseActivity.this);
                            } else if (state.toString().equals("PAUSED")) {
                                PagenameLog = "Video paused";
                                getLogEvent(PopularCourseActivity.this);
                            } else if (state.toString().equals("STOPPED")) {
                                PagenameLog = "Video stopped";
                                getLogEvent(PopularCourseActivity.this);
                            } else if (state.toString().equals("ENDED")){
                                PagenameLog = "Video Ended";
                                getLogEvent(PopularCourseActivity.this);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}