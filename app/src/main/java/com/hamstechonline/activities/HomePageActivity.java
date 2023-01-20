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

import com.android.volley.DefaultRetryPolicy;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.adapters.CategoryCoursesAdapter;
import com.hamstechonline.adapters.MyCoursePagerAdapter;
import com.hamstechonline.adapters.MyCoursesAdapter;
import com.hamstechonline.adapters.StoriesSliderCardPagerAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.favourite.FavouriteCategories;
import com.hamstechonline.datamodel.favourite.FavouriteCourse;
import com.hamstechonline.datamodel.homepage.EnglishCategory;
import com.hamstechonline.datamodel.homepage.HomepageResponse;
import com.hamstechonline.datamodel.homepage.MainVideo;
import com.hamstechonline.datamodel.homepage.MiniLesson;
import com.hamstechonline.datamodel.homepage.MoreTrialClass;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.datamodel.homepage.SuccessStory;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.fragments.SearchFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.DynamicWhatsAppChat;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.HowtoUseAppDialogue;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.Params;
import com.hamstechonline.utils.RatingDialogue;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.hamstechonline.utils.WrapContentViewPager;
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

import retrofit2.Call;
import retrofit2.Callback;

public class HomePageActivity extends AppCompatActivity {

    RecyclerView listRecommended,listCategory,listHindi,listMiniLessons,listPopularCourses,listCourse;
    SubListAdapter subListAdapter;
    PopularCoursesAdapter popularCoursesAdapter;
    CategoryCoursesAdapter categoryCoursesAdapter;
    MiniCoursesListAdapter miniCoursesListAdapter;
    DrawerLayout drawer;
    //BottomNavigationView navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    ImageView imgMentorImage,imgChooseLang,imgWhatsApp,footer_ribbon,
            imgPrevious,imgNext,mycoursePrevious,mycourseNext,gifSuccessStory,imgExpand;
    TextView txtTitle,subListTitle;
    ArrayList<CategoryDatamodel> subCatList = new ArrayList<>();
    List<MainVideo> mainiVideo = new ArrayList<>();
    List<MoreTrialClass> moreClasses = new ArrayList<>();
    List<MiniLesson> miniCoursesArrayList = new ArrayList<>();
    List<SuccessStory> successStoryList = new ArrayList<>();
    ArrayList<MyCourse> myCourseList = new ArrayList<>();
    CheckBox imgSearch, txtSeeMore;
    SearchFragment searchFragment;
    FooterNavigationUnPaid footerNavigationUnPaid;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog;
    View view;
    LinearLayout recommendedLayout,otherEnglishListLayout,otherHindiListLayout;
    RelativeLayout layoutHeader;
    NestedScrollView mainLayout;
    FirebaseAnalytics mFirebaseAnalytics;
    ViewPager sliderView;
    WrapContentViewPager listTopicsRecommended;
    int mMenuId, arraySize,currentPageBanner,mycoursePageBanner, selectedItem = 509;
    Animation animation;
    UserDataBase userDataBase;
    LogEventsActivity logEventsActivity;
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language",mp4URL,typeCat = "",footerMenuStatus;
    SharedPreferences prefs,footerStatus,typeCateSharedPreferences;
    private Locale myLocale;
    RatingDialogue howtoUseAppDialogue;
    HowtoUseAppDialogue howtoUseApp;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;

    ApiInterface apiService;

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player,playerMentor;
    private StringBuilder logString;
    StoriesSliderCardPagerAdapter storiesSliderCardPagerAdapter;
    MyCoursePagerAdapter myCoursePagerAdapter;
    Handler handlerBanner;
    Timer timerBanner;
    Runnable updateBanner;
    DynamicWhatsAppChat dynamicWhatsAppChat;
    LinearLayout layoutContact,layoutHome,imgHunarClub;
    FragmentTransaction ft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.homepage_activity);

        listRecommended = findViewById(R.id.listRecommended);
        listCategory = findViewById(R.id.listCategory);
        listHindi = findViewById(R.id.listHindi);
        drawer = findViewById(R.id.drawer_layout);
        //navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        imgSearch = findViewById(R.id.imgSearch);
        txtTitle = findViewById(R.id.txtTitle);
        view = findViewById(R.id.searchLayout);
        mainLayout = findViewById(R.id.mainLayout);
        listTopicsRecommended = findViewById(R.id.listTopicsRecommended);
        recommendedLayout = findViewById(R.id.recommendedLayout);
        imgChooseLang = findViewById(R.id.imgChooseLang);
        otherEnglishListLayout = findViewById(R.id.otherEnglishListLayout);
        otherHindiListLayout = findViewById(R.id.otherHindiListLayout);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        layoutHeader = findViewById(R.id.layoutHeader);
        txtSeeMore = findViewById(R.id.txtSeeMore);
        listMiniLessons = findViewById(R.id.listMiniLessons);
        footer_ribbon = findViewById(R.id.footer_ribbon);
        sliderView = findViewById(R.id.imageSlider);
        imgPrevious = findViewById(R.id.imgPrevious);
        imgNext = findViewById(R.id.imgNext);
        mycourseNext = findViewById(R.id.mycourseNext);
        mycoursePrevious = findViewById(R.id.mycoursePrevious);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        gifSuccessStory = findViewById(R.id.gifSuccessStory);
        listPopularCourses = findViewById(R.id.listPopularCourses);
        listCourse = findViewById(R.id.listCourse);
        imgExpand = findViewById(R.id.imgExpand);
        layoutHome = findViewById(R.id.layoutHome);
        layoutContact = findViewById(R.id.layoutContact);
        imgHunarClub = findViewById(R.id.imgHunarClub);
        imgMentorImage = findViewById(R.id.imgMentorImage);

        /*navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);*/

        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);



        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        howtoUseAppDialogue = new RatingDialogue(this);
        howtoUseApp = new HowtoUseAppDialogue(this);
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        //reviewManager = ReviewManagerFactory.create(this);
        //reviewManager = new FakeReviewManager(this);

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();

        ft = getSupportFragmentManager().beginTransaction();

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        //setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(this);

        logString = new StringBuilder();

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        //getReviewInfo();
        getLifecycle().addObserver(youTubePlayerView);
        hocLoadingDialog = new HocLoadingDialog(this);
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        typeCateSharedPreferences = getSharedPreferences("Category", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        langPref = prefs.getString("Language", "en");

        typeCat = typeCateSharedPreferences.getString("Category", "");
        Log.e("typeCat","282   "+typeCat);
        changeLang(langPref);

        getResponse();

        imgChooseLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryName = "";
                CourseLog = "";
                LessonLog = "";
                ActivityLog = "Home Page";
                PagenameLog = "Language Selection";
                getLogEvent(HomePageActivity.this);
                Intent intent = new Intent(HomePageActivity.this, ChooseLanguage.class);
                startActivity(intent);
            }
        });

        imgExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = 999;
                subCatList.clear();
                subListAdapter.notifyDataSetChanged();
                listCourse.setVisibility(View.GONE);
                imgExpand.setVisibility(View.GONE);
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
        if (savedInstanceState == null) {
            if (footerMenuStatus.equalsIgnoreCase("paid")) {
                //footerNavigationPaid = FooterNavigationPaid.newInstance();
                ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Home Page").commit();
            } else {
                //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
                ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Home Page")
                        .commit();
            }
        }
        imgSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtTitle.setVisibility(View.GONE);
                    imgWhatsApp.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                    searchFragment = SearchFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.searchLayout, searchFragment, "")
                            .commit();
                } else {
                    txtTitle.setVisibility(View.VISIBLE);
                    imgWhatsApp.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    hideKeyboard(HomePageActivity.this);
                }
            }
        });
        imgWhatsApp.setOnClickListener(new View.OnClickListener() {
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

        layoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "";
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(HomePageActivity.this);
                Intent intentCourses = new Intent(HomePageActivity.this, HomePageActivity.class);
                startActivity(intentCourses);
            }
        });

        layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Home page";
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(HomePageActivity.this);
                new AppsFlyerEventsHelper(HomePageActivity.this).EventContactus();
                Intent about = new Intent(HomePageActivity.this, ContactActivity.class);
                startActivity(about);
            }
        });
        imgHunarClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityLog = "Click";
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(HomePageActivity.this);
                Intent hamstech = new Intent(HomePageActivity.this, BuzzActivity.class);
                startActivity(hamstech);
            }
        });

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dynamicWhatsAppChat = new DynamicWhatsAppChat(HomePageActivity.this,"Home page","","");
                dynamicWhatsAppChat.getChatNumber(userDataBase.getUserMobileNumber(1));
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

        mycourseNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycourseNextData();
            }
        });
        mycoursePrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycoursePreviousData();
            }
        });

        txtSeeMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    arraySize = miniCoursesArrayList.size();
                    miniCoursesListAdapter = new MiniCoursesListAdapter(HomePageActivity.this,miniCoursesArrayList, arraySize);
                    listMiniLessons.setLayoutManager(new GridLayoutManager(HomePageActivity.this, 2));
                    //listMiniLessons.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
                    listMiniLessons.setAdapter(miniCoursesListAdapter);
                    txtSeeMore.setText(R.string.view_less);
                } else {
                    arraySize = 4;
                    miniCoursesListAdapter = new MiniCoursesListAdapter(HomePageActivity.this,miniCoursesArrayList, arraySize);
                    listMiniLessons.setLayoutManager(new GridLayoutManager(HomePageActivity.this, 2));
                    //listMiniLessons.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
                    listMiniLessons.setAdapter(miniCoursesListAdapter);
                    txtSeeMore.setText(getResources().getString(R.string.see_more));
                }
            }
        });

        gifSuccessStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryName = "";CourseLog = "";
                LessonLog = ""; ActivityLog = "HomePage"; PagenameLog = "Hunar Club gif";
                getLogEvent(HomePageActivity.this);
                Intent intentBuzz = new Intent(HomePageActivity.this, BuzzActivity.class);
                startActivity(intentBuzz);

            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    @Override
    protected void onStop() {
        try {
            if (timerBanner != null) timerBanner.cancel();
        } catch (Exception r) {
            r.printStackTrace();
        }
        super.onStop();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onDestroy() {
        try {
            if (timerBanner !=null)
            timerBanner.cancel();
        } catch (Exception r) {
            r.printStackTrace();
        }
        super.onDestroy();
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void getResponse() {
        hocLoadingDialog.showLoadingDialog();
        HomepageResponse homepageResponse = new HomepageResponse("Hamstech","category",
                getResources().getString(R.string.lblApiKey),langPref,userDataBase.getUserMobileNumber(1),typeCat);
        Call<HomepageResponse> call = apiService.getHomepageResponse(homepageResponse);
        call.enqueue(new Callback<HomepageResponse>() {
            @Override
            public void onResponse(Call<HomepageResponse> call, retrofit2.Response<HomepageResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getEnglish() != null) {
                    moreClasses.clear();
                    mainiVideo.clear();
                    myCourseList.clear();
                    handlerBanner = new Handler();
                    timerBanner = new Timer();
                    otherEnglishListLayout.setVisibility(View.VISIBLE);
                    listCourse.setVisibility(View.GONE);

                    subListAdapter = new SubListAdapter(HomePageActivity.this,response.body().getEnglish(),selectedItem);
                    listRecommended.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, RecyclerView.HORIZONTAL, false));
                    listRecommended.setAdapter(subListAdapter);

                    popularCoursesAdapter = new PopularCoursesAdapter(HomePageActivity.this,response.body().getFavouriteCourse());
                    listPopularCourses.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, RecyclerView.VERTICAL, false));
                    listPopularCourses.setAdapter(popularCoursesAdapter);

                    /*subListAdapter = new SubListAdapter(HomePageActivity.this,catListHindi);
                    listCategory.setLayoutManager(new GridLayoutManager(HomePageActivity.this, 2));
                    listCategory.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
                    listCategory.setAdapter(subListAdapter);*/

                    if (response.body().getMyCourses().size() > 0) {
                        recommendedLayout.setVisibility(View.VISIBLE);
                        myCourseList = response.body().getMyCourses();
                        footerMenuStatus = "paid";
                        myCoursePagerAdapter = new MyCoursePagerAdapter(HomePageActivity.this,myCourseList);
                        listTopicsRecommended.setOffscreenPageLimit(myCourseList.size());
                        listTopicsRecommended.setAdapter(myCoursePagerAdapter);
                    } else {
                        SharedPreferences.Editor editor = footerStatus.edit();
                        editor.putString("footerStatus", "unpaid");
                        editor.commit();

                    }


                    if (response.body().getMiniLessons().size() > 4) {
                        arraySize = 4;
                        txtSeeMore.setVisibility(View.GONE);
                    } else {
                        arraySize = response.body().getMiniLessons().size();
                        txtSeeMore.setVisibility(View.GONE);
                    }
                    miniCoursesArrayList = response.body().getMiniLessons();
                    miniCoursesListAdapter = new MiniCoursesListAdapter(HomePageActivity.this,response.body().getMiniLessons(), arraySize);
                    listMiniLessons.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, RecyclerView.HORIZONTAL, false));
                    listMiniLessons.setAdapter(miniCoursesListAdapter);

                    storiesSliderCardPagerAdapter = new StoriesSliderCardPagerAdapter(HomePageActivity.this,response.body().getSuccessStories());
                    sliderView.setAdapter(storiesSliderCardPagerAdapter);

                    successStoryList = response.body().getSuccessStories();

                    Glide.with(HomePageActivity.this)
                            .load(response.body().getFooterRibbonImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(footer_ribbon);

                    Glide.with(HomePageActivity.this)
                            .load(response.body().getCelebrity_mentor_image())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(imgMentorImage);

                    if (UserDataConstants.notificationID!= null){
                        if(UserDataConstants.notificationID.equals("2570")){
                            RadingDialogue();
                            LessonLog = UserDataConstants.notificationTitle;
                            CourseLog = "";
                            CategoryName = "";
                            PagenameLog = "Ratings";
                            ActivityLog = "Notification Clicked";
                            getLogEvent(HomePageActivity.this);
                        } else {
                            LessonLog = UserDataConstants.notificationTitle;
                            CourseLog = "";CategoryName = "";
                            PagenameLog = "Home page";
                            ActivityLog = "Notification Clicked";
                            getLogEvent(HomePageActivity.this);
                        }
                    }

                    mainLayout.setVisibility(View.VISIBLE);

                    UserDataConstants.howtouseTitle = response.body().getHowToUseApp().get(0).getTitle();
                    UserDataConstants.videoUrl = response.body().getHowToUseApp().get(0).getVideo();
                    UserDataConstants.howtouseDesc = response.body().getHowToUseApp().get(0).getDesc();
                    hocLoadingDialog.hideDialog();
                    if (response.body().getMyCourses().size() == 0) {
                        mp4URL = response.body().getPromotional_video();
                        youTubePlayerView.setVisibility(View.VISIBLE);
                        getLifecycle().addObserver(youTubePlayerView);
                        installYouTube(response.body().getPromotional_video());
                    }
                    /*if (getIntent().getBooleanExtra("isNewRegister",false) == true){
                        HowtoUseAppOpen();
                    }*/
                    CourseLog = response.body().getMainVideo().getCourseTitle();

                    mainiVideo.add(response.body().getMainVideo());

                    moreClasses = response.body().getMoreTrialClasses();
                    //getTopicsList(HomePageActivity.this);

                    updateBanner = new Runnable() {
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
                    }, 2000, 3000);
                }
            }

            @Override
            public void onFailure(Call<HomepageResponse> call, Throwable t) {

            }
        });
    }


    public void imgNextData(){
        if (storiesSliderCardPagerAdapter.getCount() == currentPageBanner) {
            currentPageBanner = 0;
        }
        sliderView.setCurrentItem(currentPageBanner++, true);
    }

    public void imgPreviousData(){
        if (storiesSliderCardPagerAdapter.getCount() == currentPageBanner) {
            currentPageBanner = 0;
        }
        sliderView.setCurrentItem(currentPageBanner--, true);
    }

    public void mycourseNextData(){
        if (myCoursePagerAdapter.getCount() == mycoursePageBanner) {
            mycoursePageBanner = 0;
        }
        listTopicsRecommended.setCurrentItem(mycoursePageBanner++, true);
    }

    public void mycoursePreviousData(){
        if (myCoursePagerAdapter.getCount() == mycoursePageBanner) {
            mycoursePageBanner = 0;
        }
        listTopicsRecommended.setCurrentItem(mycoursePageBanner--, true);
    }
    public void lessonPageLeft(int current){
        Intent intent = new Intent(this, LessonsPageNotifications.class);
        intent.putExtra("CourseId", mainiVideo.get(current).getCategoryId());
        intent.putExtra("lessonId",UserDataConstants.lessonIdleft);
        startActivity(intent);
    }
    public void lessonPageRight(int current){
        Intent intent = new Intent(this, LessonsPageNotifications.class);
        intent.putExtra("CourseId", mainiVideo.get(current).getCategoryId());
        intent.putExtra("lessonId",UserDataConstants.lessonIdRight);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
        if (newConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutHeader.setVisibility(View.GONE);
            recommendedLayout.setVisibility(View.GONE);
            otherEnglishListLayout.setVisibility(View.GONE);
            //navigation.setVisibility(View.GONE);
            stickyWhatsApp.setVisibility(View.GONE);
        } else {
            layoutHeader.setVisibility(View.VISIBLE);
            recommendedLayout.setVisibility(View.VISIBLE);
            otherEnglishListLayout.setVisibility(View.VISIBLE);
            //navigation.setVisibility(View.VISIBLE);
            stickyWhatsApp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            timerBanner.cancel();
        } catch (Exception r) {
            r.printStackTrace();
        }
        finishAffinity();
    }


    public void RadingDialogue(){
        try {
            howtoUseAppDialogue.showLoadingDialog();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void HowtoUseAppOpen(){
        try {
            howtoUseApp.showLoadingDialog();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public class PopularCoursesAdapter extends RecyclerView.Adapter<PopularCoursesAdapter.ViewHolder> {
        Context context;
        List<FavouriteCategories> datamodels;

        public PopularCoursesAdapter(Context context,List<FavouriteCategories> datamodels){
            this.context=context;
            this.datamodels = datamodels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.popular_courses_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                Glide.with(context)
                        .load(datamodels.get(position).getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.courseImage);

                holder.txtCourseName.setText(datamodels.get(position).getCourseTitle());
                holder.txtDescription.setText(datamodels.get(position).getCourseDescription());
                holder.txtStartLearning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryName = datamodels.get(position).getCategoryname()+" "+datamodels.get(position).getLanguage();
                        CourseLog = "";LessonLog = "";ActivityLog = "Click";PagenameLog = "Dashboard";
                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, datamodels.get(position).getCategoryname());
                        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                        getLogEvent(HomePageActivity.this);
                        new AppsFlyerEventsHelper(context).EventCategory(CategoryName);
                        Intent intent = new Intent(context, CoursePageActivity.class);
                        intent.putExtra("CategoryId",datamodels.get(position).getCourseId());
                        intent.putExtra("CategoryName",datamodels.get(position).getCategoryname());
                        intent.putExtra("CourseName",datamodels.get(position).getCourseTitle());
                        intent.putExtra("description",datamodels.get(position).getCourseDescription());
                        intent.putExtra("language",datamodels.get(position).getLanguage());
                        intent.putExtra("VideoUrl",datamodels.get(position).getVideoUrl());
                        intent.putExtra("statusNSDC",datamodels.get(position).getNsdcStatus());
                        context.startActivity(intent);

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datamodels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView courseImage;
            TextView txtCourseName,txtDescription,txtStartLearning;
            RelativeLayout listLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                courseImage = view.findViewById(R.id.courseImage);
                txtCourseName = view.findViewById(R.id.txtCourseName);
                txtDescription = view.findViewById(R.id.txtDescription);
                txtStartLearning = view.findViewById(R.id.txtStartLearning);
            }
        }
    }

    public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.ViewHolder> {
        Context context;
        List<EnglishCategory> datamodels;
        int selectedItemCourse;

        public SubListAdapter(Context context,List<EnglishCategory> datamodels, int selectedItem){
            this.context=context;
            this.datamodels = datamodels;
            this.selectedItemCourse = selectedItem;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.main_courses_list_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                Glide.with(context)
                        .load(datamodels.get(position).getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);

                if (selectedItem == position) {
                    holder.cardMainLayout.setBackground(context.getResources().getDrawable(R.drawable.cat_selct_pink));
                } else {
                    holder.cardMainLayout.setBackgroundResource(0);
                }

                holder.txtTopTitle.setText(datamodels.get(position).getCategoryname());
                holder.txtBottomTitle.setText(datamodels.get(position).getCategoryDescription());
                holder.listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //holder.cardMainLayout.setBackground(context.getResources().getDrawable(R.drawable.cat_selct_pink));
                        selectedItem = position;
                        CategoryName = datamodels.get(position).getCategoryname()+" "+datamodels.get(position).getLanguage();
                        CourseLog = "";LessonLog = "";ActivityLog = "Click";PagenameLog = "Dashboard";
                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, datamodels.get(position).getCategoryname());
                        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                        hocLoadingDialog.showLoadingDialog();
                        getLogEvent(HomePageActivity.this);
                        new AppsFlyerEventsHelper(context).EventCategory(CategoryName);
                        getSubCategoriesList(HomePageActivity.this,datamodels.get(position).getCategoryId(),datamodels.get(position).getLanguage());
                        notifyDataSetChanged();

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return datamodels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            TextView txtTopTitle,txtBottomTitle;
            RelativeLayout listLayout;
            LinearLayout cardMainLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.imgCategory);
                txtTopTitle = view.findViewById(R.id.txtTopTitle);
                txtBottomTitle = view.findViewById(R.id.txtBottomTitle);
                listLayout = view.findViewById(R.id.listLayout);
                subListTitle = view.findViewById(R.id.subListTitle);
                cardMainLayout = view.findViewById(R.id.cardMainLayout);
            }
        }
    }

    public class MiniCoursesListAdapter extends RecyclerView.Adapter<MiniCoursesListAdapter.ViewHolder> {
        Context context;
        List<MiniLesson> datamodels;
        int arraySize;

        public MiniCoursesListAdapter(Context context,List<MiniLesson> datamodels, int arraySize){
            this.context=context;
            this.datamodels = datamodels;
            this.arraySize = arraySize;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.minilessons_list_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                Glide.with(context)
                        .load(datamodels.get(position).getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);

                holder.txtTopTitle.setText(datamodels.get(position).getCourseTitle());
                holder.txtBottomTitle.setText(datamodels.get(position).getCourseDescription());
                holder.listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryName = datamodels.get(position).getCategoryname();
                        CourseLog = datamodels.get(position).getCourseTitle();LessonLog = "";
                        ActivityLog = "Click";PagenameLog = "Dashboard";
                        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, datamodels.get(position).getCourseTitle());
                        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                        getLogEvent(HomePageActivity.this);
                        new AppsFlyerEventsHelper(context).EventCategory(CategoryName);

                        Intent intent = new Intent(context, PopularCourseActivity.class);
                        intent.putExtra("CategoryId",datamodels.get(position).getCourseId());
                        intent.putExtra("CategoryName",datamodels.get(position).getCategoryname());
                        intent.putExtra("CourseName",datamodels.get(position).getCourseTitle());
                        intent.putExtra("description",datamodels.get(position).getCourseDescription());
                        intent.putExtra("language",datamodels.get(position).getLanguage());
                        intent.putExtra("VideoUrl",datamodels.get(position).getVideoUrl());
                        intent.putExtra("amount",datamodels.get(position).getAmount());
                        startActivity(intent);

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arraySize;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            TextView txtTopTitle,txtBottomTitle;
            RelativeLayout listLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.imgCategory);
                txtTopTitle = view.findViewById(R.id.txtTopTitle);
                txtBottomTitle = view.findViewById(R.id.txtBottomTitle);
                listLayout = view.findViewById(R.id.listLayout);
                subListTitle = view.findViewById(R.id.subListTitle);
            }
        }
    }

    public void getSubCategoriesList(Context context, String catID,String language){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("categoryId",catID);
            params.put("language",language);
            params.put("phone",userDataBase.getUserMobileNumber(1));
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.get_courses_bycat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    subCatList.clear();
                    if (jo.isNull("list")){
                        Toast.makeText(HomePageActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                        hocLoadingDialog.hideDialog();
                    } else {
                        JSONArray jsonArray = jo.getJSONArray("list");

                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            CategoryDatamodel dataModel = new CategoryDatamodel();

                            dataModel.setCategoryId(object.getString("courseId"));
                            dataModel.setCategoryname(object.getString("categoryname"));
                            dataModel.setCategory_Title(object.getString("course_title"));
                            dataModel.setCat_image_url(object.getString("image_url"));
                            dataModel.setCategory_description(object.getString("course_description"));
                            dataModel.setCategory_language(object.getString("language"));
                            dataModel.setCatVideoUrl(object.getString("video_url"));
                            dataModel.setStatus(object.getString("status"));
                            dataModel.setStatusNSDC(object.getString("nsdc_status"));
                            subCatList.add(dataModel);
                        }
                        hocLoadingDialog.hideDialog();
                        categoryCoursesAdapter = new CategoryCoursesAdapter(HomePageActivity.this,subCatList);
                        listCourse.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, RecyclerView.HORIZONTAL, false));
                        listCourse.setAdapter(categoryCoursesAdapter);

                        if (subCatList.size() > 0) {
                            imgExpand.setVisibility(View.VISIBLE);
                            listCourse.setVisibility(View.VISIBLE);
                        } else imgExpand.setVisibility(View.GONE);

                        //ViewAllCources(subCatList);
                    }
                } catch(JSONException e){
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
                    Toast.makeText(HomePageActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public void ViewAllCources(ArrayList<CategoryDatamodel> subCatList){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.courses_dialogue);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RecyclerView listItems = dialog.findViewById(R.id.listItems);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        if (subCatList.size() >1 ){
            categoryCoursesAdapter = new CategoryCoursesAdapter(HomePageActivity.this,subCatList);
            listItems.setLayoutManager(new GridLayoutManager(this, 2));
            listItems.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
            listItems.setAdapter(categoryCoursesAdapter);
        } else if (subCatList.size() == 1){
            dialog.getWindow().setLayout(500, ViewGroup.LayoutParams.WRAP_CONTENT);
            categoryCoursesAdapter = new CategoryCoursesAdapter(HomePageActivity.this,subCatList);
            listItems.setLayoutManager(new LinearLayoutManager(HomePageActivity.this, LinearLayoutManager.HORIZONTAL, false));
            listItems.setAdapter(categoryCoursesAdapter);
        }

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void installYouTube(String promotionVideo) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                player.loadVideo(promotionVideo, 0);
                if (!getIntent().getBooleanExtra("isNewRegister",false) == true){
                    player.pause();
                }

            }
            @Override
            public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "Home Page";
                CategoryName = "";
                LessonLog = "";
                CourseLog = "counseling video";
                if (state.toString().equals("PLAYING")){
                    PagenameLog = "Video start";
                    getLogEvent(HomePageActivity.this);
                } else if (state.toString().equals("PAUSED")){
                    PagenameLog = "Video paused";
                    getLogEvent(HomePageActivity.this);
                } else if (state.toString().equals("STOPPED")){
                    PagenameLog = "Video stopped";
                    getLogEvent(HomePageActivity.this);
                }
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                layoutHeader.setVisibility(View.GONE);
                recommendedLayout.setVisibility(View.GONE);
                otherEnglishListLayout.setVisibility(View.GONE);
                //navigation.setVisibility(View.GONE);
                stickyWhatsApp.setVisibility(View.GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                layoutHeader.setVisibility(View.VISIBLE);
                recommendedLayout.setVisibility(View.VISIBLE);
                otherEnglishListLayout.setVisibility(View.VISIBLE);
                //navigation.setVisibility(View.VISIBLE);
                stickyWhatsApp.setVisibility(View.VISIBLE);
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
    }

    public void logContactusEvent(String eventValue){
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, eventValue);
        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
        params.putString(Params.CONTENT_TYPE, eventValue);
        mFirebaseAnalytics.logEvent("contact_us", params);
        new FacebookEventsHelper(HomePageActivity.this).logSpendCreditsEvent(eventValue);
    }

    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile",UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category",CategoryName);
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
