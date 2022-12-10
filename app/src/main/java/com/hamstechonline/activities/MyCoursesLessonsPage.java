package com.hamstechonline.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hamstechonline.R;
import com.hamstechonline.adapters.StoriesSliderCardPagerAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CallWithFacultyResponse;
import com.hamstechonline.datamodel.StudentsWork;
import com.hamstechonline.datamodel.homepage.SuccessStory;
import com.hamstechonline.datamodel.mycources.Lesson;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.fragments.VideoDialogue;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.FacebookEventsHelper;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.HowtoUseAppDialogue;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.Params;
import com.hamstechonline.utils.PercentView;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.hamstechonline.utils.WrapContentViewPager;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.VerticalPdfViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfScale;
import retrofit2.Call;
import retrofit2.Callback;

public class MyCoursesLessonsPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final int REQUEST_GALLERY = 101;
    private final int REQUEST_MULTI_GALLERY = 201;
    private final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 102;

    BottomNavigationView navigation;
    DrawerLayout drawer;
    NavigationView navSideMenu;
    LinearLayout linearLessonContent, layoutUpload,lessonsContentLayout,playerLayout;
    TextView headerTitle, txtDescription, txtLessonName, txtImageText, textPercentage, textAssignFile;
    RelativeLayout layoutHeader,assignmentDown,studyDown,feedbackDown;
    CardView cardAssignmentPdf,cardStudyPdf,cardFacultyFeedback;
    UserDataBase userDataBase;
    ApiInterface apiService;
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String CategoryName, CourseLog, LessonLog, ActivityLog, PagenameLog, CourseId, CategoryLog;
    ImageView imgLike, btnShare, imgLesson, videoThumbnail, imageLike, imageDisLike,assignmentDownArrow,
            studyDownArrow,feedbackDownArrow,imgVideoCompleted;
    TextView nextLessonTitle, previousLessonTitle,feedbackByFaculty,txtAssignment;
    LinearLayout fabPrevious, fabNext,layoutFeedback,layoutPercent,layoutStudentWorks;
    PercentView percentView;
    VerticalPdfViewPager pdfViewStudy, pdfViewAssign;
    private String mp4URL = "", postStatus = "", postId, pdfURL, assignURL,facultyFeedBack,assignmentText = "",videoThumbnailURL = "";
    List<Lesson> coursesList = new ArrayList<>();
    List<String> imageUrls = new ArrayList<>();
    ArrayList<StudentsWork> arrayListStudentWork = new ArrayList<>();
    int intNext = 0, intPrevious = 0, mMenuId;
    WrapContentViewPager imageSlider;

    NavigationFragment navigationFragment;
    Button btnEnrolNow;
    RelativeLayout enrolLayout;
    CardView btnEnrollLesson;
    CircleImageView profile_image;
    LinearLayout linProfile;
    TextView txtUserMobile, txtUserName;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    Bundle params;
    AppEventsLogger logger;
    FirebaseAnalytics firebaseAnalytics;
    SharedPrefsUtils sharedPrefsUtils;
    Bundle dataBundle;
    ImageButton stickyWhatsApp;

    String courseId, lessonId;

    DownloadManager downloadManager;

    String strFilePath = "";
    File assignmentFile = null;

    TextView txtCallRequest, txtChat;
    LinearLayout floatBtns;
    StudentWorkSliderAdapter studentWorkSliderAdapter;
    VideoDialogue howtoUseApp;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.lessons_page_activity);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
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
        nextLessonTitle = findViewById(R.id.nextLessonTitle);
        previousLessonTitle = findViewById(R.id.previousLessonTitle);
        fabPrevious = findViewById(R.id.fabPrevious);
        fabNext = findViewById(R.id.fabNext);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        enrolLayout = findViewById(R.id.enrolLayout);
        videoThumbnail = findViewById(R.id.videoThumbnail);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        cardAssignmentPdf = findViewById(R.id.cardAssignmentPdf);
        studyDown = findViewById(R.id.studyDown);
        cardStudyPdf = findViewById(R.id.cardStudyPdf);
        layoutFeedback = findViewById(R.id.layoutFeedback);
        feedbackByFaculty = findViewById(R.id.feedbackByFaculty);
        cardFacultyFeedback = findViewById(R.id.cardFacultyFeedback);
        feedbackDown = findViewById(R.id.feedbackDown);
        studyDownArrow = findViewById(R.id.studyDownArrow);
        assignmentDownArrow = findViewById(R.id.assignmentDownArrow);
        feedbackDownArrow = findViewById(R.id.feedbackDownArrow);
        layoutUpload = findViewById(R.id.layoutUpload);
        txtAssignment = findViewById(R.id.txtAssignment);
        imgVideoCompleted = findViewById(R.id.imgVideoCompleted);
        layoutPercent = findViewById(R.id.layoutPercent);
        layoutStudentWorks = findViewById(R.id.layoutStudentWorks);
        lessonsContentLayout = findViewById(R.id.lessonsContentLayout);
        playerLayout = findViewById(R.id.playerLayout);

        percentView = findViewById(R.id.percentView);
        textPercentage = findViewById(R.id.textPercentage);
        textAssignFile = findViewById(R.id.textFile);
        pdfViewStudy = findViewById(R.id.pdfViewStudy);
        pdfViewAssign = findViewById(R.id.pdfViewAssign);
        assignmentDown = findViewById(R.id.assignmentDown);

        txtCallRequest = findViewById(R.id.txtCallRequest);
        txtChat = findViewById(R.id.txtChat);
        floatBtns = findViewById(R.id.floatBtns);
        imageLike = findViewById(R.id.imageLike);
        imageDisLike = findViewById(R.id.imageDisLike);
        imageSlider = findViewById(R.id.imageSlider);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        hocLoadingDialog = new HocLoadingDialog(this);
        logEventsActivity = new LogEventsActivity();
        dataBundle = new Bundle();

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        userDataBase = new UserDataBase(this);
        params = new Bundle();
        logger = AppEventsLogger.newLogger(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        layoutUpload.setVisibility(View.GONE);

        sharedPrefsUtils = new SharedPrefsUtils(this, getString(R.string.app_name));

        CategoryLog = getIntent().getStringExtra("CategoryLog");
        coursesList = (List<Lesson>) getIntent().getSerializableExtra("LessonData");
        CourseLog = getIntent().getStringExtra("CourseName");

        courseId = getIntent().getStringExtra("CourseId");
        lessonId = getIntent().getStringExtra("LessonId");

        intNext = getIntent().getIntExtra("intNext", 0);
        enrolLayout.setVisibility(View.GONE);

        ActivityLog = "Lesson Page";

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postStatus.equals("1")) {
                    PagenameLog = "Lesson unlike";
                    getLogEvent(MyCoursesLessonsPage.this);
                    //getLessonsLikeDislike(MyCoursesLessonsPage.this, postId, postStatus);
                } else if (postStatus.equals("0")) {
                    PagenameLog = "Lesson like";
                    //getLessonsLikeDislike(MyCoursesLessonsPage.this, postId, postStatus);
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (postStatus.equals("")) {
                    PagenameLog = "Lesson unlike";
                    //getLessonsLikeDislike(MyCoursesLessonsPage.this, postId, postStatus);
                    getLogEvent(MyCoursesLessonsPage.this);
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4URL = getIntent().getStringExtra("videoURL");
                PagenameLog = "Url Share";
                String url = "https://www.youtube.com/watch?v=" + mp4URL;
                getLogEvent(MyCoursesLessonsPage.this);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(shareIntent);
            }
        });

        btnEnrollLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollFunction(MyCoursesLessonsPage.this);
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4URL = "";
                watchNext();
            }
        });

        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp4URL = "";
                watchPrevious();
            }
        });

        UserDataConstants.lessonId = "";
        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                LessonLog =
                ActivityLog = "Sticky whatsapp";
                PagenameLog = "MyCourses Lessons";
                getLogEvent(MyCoursesLessonsPage.this);
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

        txtCallRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Video Call with Faculty";
                LessonLog = "";
                getLogEvent(MyCoursesLessonsPage.this);
                getCallWithFaculty();
            }
        });

        txtAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!assignmentText.isEmpty()) AssignmentPopUp(assignmentText);
            }
        });

        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Chat with Student Guide";
                LessonLog = "";
                getLogEvent(MyCoursesLessonsPage.this);
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "919010100240" + "&text=" +
                            URLEncoder.encode(getResources().getString(R.string.chat_student_guide), "UTF-8");
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
        //loadPlayer();
        //getEnrolledLessonDetails(this, courseId, lessonId);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                CategoryName = coursesList.get(intNext).getCourseTitle();
                CourseLog = coursesList.get(intNext).getCourseTitle();
                LessonLog = coursesList.get(intNext).getLessonTitle();
                ActivityLog = "Lesson Page";
                //player.loadVideo(mp4URL, 0);
                watchNext();
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "Lesson Page";
                if (state.toString().equals("PLAYING")) {
                    PagenameLog = "Video start";
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (state.toString().equals("PAUSED")) {
                    PagenameLog = "Video paused";
                    getLogEvent(MyCoursesLessonsPage.this);
                    getLessonDetails(MyCoursesLessonsPage.this, courseId, coursesList.get(intPrevious).getLessonId());
                } else if (state.toString().equals("STOPPED")) {
                    PagenameLog = "Video stopped";
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (state.toString().equals("ENDED")){
                    PagenameLog = "Video Ended";
                    imgVideoCompleted.setVisibility(View.VISIBLE);
                    layoutPercent.setVisibility(View.GONE);
                    getLogEvent(MyCoursesLessonsPage.this);
                }
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                layoutHeader.setVisibility(View.GONE);
                navigation.setVisibility(View.GONE);
                layoutHeader.setVisibility(View.GONE);
                txtLessonName.setVisibility(View.GONE);
                txtDescription.setVisibility(View.GONE);
                linearLessonContent.setVisibility(View.GONE);
                imgLesson.setVisibility(View.GONE);
                stickyWhatsApp.setVisibility(View.GONE);
                lessonsContentLayout.setVisibility(View.GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                navigation.setVisibility(View.VISIBLE);
                layoutHeader.setVisibility(View.VISIBLE);
                txtLessonName.setVisibility(View.VISIBLE);
                lessonsContentLayout.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.GONE);
                txtImageText.setVisibility(View.GONE);
                linearLessonContent.setVisibility(View.GONE);
                imgLesson.setVisibility(View.GONE);
                stickyWhatsApp.setVisibility(View.VISIBLE);
            }
        });
        assignmentDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardAssignmentPdf.getVisibility() == View.VISIBLE) {
                    assignmentDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                    cardAssignmentPdf.setVisibility(View.GONE);
                } else {
                    assignmentDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    cardAssignmentPdf.setVisibility(View.VISIBLE);
                }
            }
        });
        feedbackDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardFacultyFeedback.getVisibility() == View.VISIBLE) {
                    feedbackDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                    cardFacultyFeedback.setVisibility(View.GONE);
                } else {
                    feedbackDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    cardFacultyFeedback.setVisibility(View.VISIBLE);
                }
            }
        });

        studyDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardStudyPdf.getVisibility() == View.VISIBLE) {
                    studyDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                    cardStudyPdf.setVisibility(View.GONE);
                } else {
                    studyDownArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    cardStudyPdf.setVisibility(View.VISIBLE);
                }
            }
        });
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
                logContactusEvent(PagenameLog);
                Intent intentCourses = new Intent(MyCoursesLessonsPage.this, HomePageActivity.class);
                startActivity(intentCourses);
                return true;
            case R.id.navigation_chat:
                CategoryName = "";
                CourseLog = "";
                LessonLog = "";
                ActivityLog = "";
                PagenameLog = "chat with whatsapp";
                getLogEvent(MyCoursesLessonsPage.this);

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
                logContactusEvent(PagenameLog);
                Intent enrol = new Intent(MyCoursesLessonsPage.this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                PagenameLog = "Hunar Club";
                logContactusEvent(PagenameLog);
                Intent hamstech = new Intent(MyCoursesLessonsPage.this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                PagenameLog = "Contact Page";
                logContactusEvent(PagenameLog);
                new AppsFlyerEventsHelper(this).EventContactus();
                Intent about = new Intent(MyCoursesLessonsPage.this, ContactActivity.class);
                startActivity(about);
                return true;
        }
        return false;
    }

    public void sideMenu(View view) {
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        if (newConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navigation.setVisibility(View.GONE);
            layoutHeader.setVisibility(View.GONE);
            txtLessonName.setVisibility(View.GONE);
            txtDescription.setVisibility(View.GONE);
            linearLessonContent.setVisibility(View.GONE);
            imgLesson.setVisibility(View.GONE);
            stickyWhatsApp.setVisibility(View.GONE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        } else {
            navigation.setVisibility(View.VISIBLE);
            layoutHeader.setVisibility(View.VISIBLE);
            txtLessonName.setVisibility(View.VISIBLE);
            txtDescription.setVisibility(View.GONE);
            txtImageText.setVisibility(View.GONE);
            linearLessonContent.setVisibility(View.VISIBLE);
            imgLesson.setVisibility(View.GONE);
            stickyWhatsApp.setVisibility(View.VISIBLE);
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
        if (player != null) player.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) player.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) player.pause();
    }

    @Override
    protected void onRestart() {
        if (player != null) player.pause();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();

        if (pdfViewStudy.getAdapter() != null)
            ((PDFPagerAdapter) pdfViewStudy.getAdapter()).close();

        /*if (pdfViewAssign.getAdapter() != null)
            ((PDFPagerAdapter) pdfViewAssign.getAdapter()).close();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(resultCode == Activity.RESULT_OK) {
                /*if(data.getClipData() != null) {
                    imageUrls.clear();
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        Log.e("imageUri","670    "+imageUri);
                        Bitmap bitmap= null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // initialize byte stream
                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        // compress Bitmap
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        // Initialize byte array
                        byte[] bytes=stream.toByteArray();
                        // get base64 encoded string
                        String sImage = Base64.encodeToString(bytes,Base64.DEFAULT);
                        //imageToBase64(imageUri.toString());
                        imageUrls.add(sImage);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                    if (count > 1) {
                        textAssignFile.setText("You have selected multiple images.");
                    } else if (count == 1){
                        textAssignFile.setText(""+data.getClipData().getItemAt(0).getUri());
                    }
                    Log.e("imageUri","676    "+imageUrls.size());
                }*/
                if (requestCode == REQUEST_GALLERY && null != data) {

                    try {
                        assignmentFile = createImageFile();

                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(assignmentFile);
                        // Copying
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();

                        if (strFilePath != null && !strFilePath.isEmpty())
                            textAssignFile.setText(strFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Selected items failed to load", Toast.LENGTH_SHORT).show();
                }
            } else if(data.getData() != null) {
                String imagePath = data.getData().getPath();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }

        }
    }

    private void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        strFilePath = image.getAbsolutePath();
        return image;
    }

    private File getRootDir() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    public void saveLike(View view) {
        imageLike.setImageResource(R.drawable.ic_up_like_yes);
        imageDisLike.setImageResource(R.drawable.ic_down_dislike);
        saveLessonLikeDislike(this, ApiConstants.saveLessonLike);
    }

    public void saveDisLike(View view) {
        imageLike.setImageResource(R.drawable.ic_up_like);
        imageDisLike.setImageResource(R.drawable.ic_down_dislike_yes);
        saveLessonLikeDislike(this, ApiConstants.saveLessonDislike);
    }

    public void downloadStudyMaterial(View view) {
        if (pdfURL != null && !pdfURL.isEmpty()) {
            startDownload(pdfURL, "_" + coursesList.get(intNext).getLessonTitle());
        }
    }

    public void downloadAssignment(View view) {
        if (assignURL != null && !assignURL.isEmpty()) {
            startDownload(assignURL, "_" + coursesList.get(intNext).getLessonTitle());
        }
    }

    public void startDownload(String url, String file_name) {
        Toast.makeText(this, "Please wait until the file is downloaded", Toast.LENGTH_LONG).show();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        //Toast.makeText(getActivity(), ""+url.substring(url.length()-4), Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("" + CourseLog + file_name + url.substring(url.length() - 4));
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
        //request.setDestinationInExternalFilesDir(Environment.DIRECTORY_DOWNLOADS, "/"+ "filename");
        //refid = downloadManager.enqueue(request);
    }

    public void chooseFile() {
        strFilePath = "";
        assignmentFile = null;
        textAssignFile.setText(getString(R.string.select_files));
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Ask specifically for something that can be opened:
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_GALLERY);

        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);*/
    }

    public void onClickUpload(View view) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            chooseFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                chooseFile();
            }
        }
    }

    public void onSubmit(View view) {
        if (strFilePath != null && !strFilePath.isEmpty()) {
            hocLoadingDialog.showLoadingDialog();
            uploadAssignment(this);
        } else
            Toast.makeText(this, "Please select assignment file", Toast.LENGTH_SHORT).show();
    }

    public void enrollFunction(Context context) {
        CategoryName = getIntent().getStringExtra("CategoryName");
        CourseLog = getIntent().getStringExtra("CourseName");
        LessonLog = "";
        ActivityLog = "Enroll now";
        PagenameLog = "Lessons Page";
        getLogEvent(MyCoursesLessonsPage.this);
        new AppsFlyerEventsHelper(MyCoursesLessonsPage.this).EventEnroll();
        Intent intent = new Intent(MyCoursesLessonsPage.this, EnrolNowActivity.class);
        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, true);
        intent.putExtra("getCourseId", Integer.parseInt(MyCoursesLessonsPage.this.getIntent().getStringExtra("CourseId")));
        startActivity(intent);
    }

    public void getCallWithFaculty() {
        hocLoadingDialog.showLoadingDialog();
        CallWithFacultyResponse callWithFacultyResponse = new CallWithFacultyResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1), courseId);
        Call<CallWithFacultyResponse> call = apiService.getCallWithFacultyResponse(callWithFacultyResponse);
        call.enqueue(new Callback<CallWithFacultyResponse>() {
            @Override
            public void onResponse(Call<CallWithFacultyResponse> call, retrofit2.Response<CallWithFacultyResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    OnlineSuccessfulPopUp(MyCoursesLessonsPage.this);
                }
            }

            @Override
            public void onFailure(Call<CallWithFacultyResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public void OnlineSuccessfulPopUp(Context context) {
        final Dialog dialog = new Dialog(context);
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

        Glide.with(context)
                .load(R.drawable.ic_sucess_payment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_payment)
                .into(progressBar);
        paymentComment.setText(getResources().getString(R.string.call_request_accepted));

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                dialog.dismiss();
                //startActivity(intent);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    Intent intent = new Intent(context, HomePageActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public void pdfDialogue(String strTitle, String strFileName) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pdf_view_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        TextView textTitle = dialog.findViewById(R.id.textTitle);
        VerticalPdfViewPager pdfViewPager = dialog.findViewById(R.id.pdfView);
        BasePDFPagerAdapter basePDFPagerAdapter = null;
        textTitle.setText(strTitle);

        try {
            File file = new File(getExternalFilesDir("pdf"), strFileName + ".pdf");
            if (file.exists()) {
                basePDFPagerAdapter = new PDFPagerAdapter(this, file.getAbsolutePath());
                if (basePDFPagerAdapter != null)
                    pdfViewPager.setAdapter(basePDFPagerAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void getEnrolledLessonDetails(final Context context, String courseId, String lessonId) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "lessons");
            params.put("apikey", context.getResources().getString(R.string.lblApiKey));
            params.put("phone", UserDataConstants.userMobile);
            params.put("course_id", courseId);
            params.put("lesson_id", lessonId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        hocLoadingDialog.showLoadingDialog();
        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getEnrolledLessonDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hocLoadingDialog.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null && jsonObject.length() > 0) {
                        if (jsonObject.optString("status").equalsIgnoreCase("ok")) {

                            JSONArray studentWorkArray = jsonObject.getJSONArray("students_work");
                            if (studentWorkArray != null && studentWorkArray.length() > 0) {
                                JSONObject lessonObj = null;
                                arrayListStudentWork.clear();
                                for (int i = 0; i < studentWorkArray.length(); i++) {
                                    lessonObj = studentWorkArray.getJSONObject(i);
                                    StudentsWork studentsWork = new StudentsWork();
                                    studentsWork.setType(lessonObj.getString("type"));
                                    studentsWork.setSrc(lessonObj.getString("src"));
                                    studentsWork.setThumbnail(lessonObj.getString("thumbnail"));
                                    arrayListStudentWork.add(studentsWork);
                                }
                                studentWorkSliderAdapter = new StudentWorkSliderAdapter(MyCoursesLessonsPage.this,arrayListStudentWork);
                                imageSlider.setAdapter(studentWorkSliderAdapter);
                                layoutStudentWorks.setVisibility(View.VISIBLE);
                            } else {layoutStudentWorks.setVisibility(View.GONE);}

                            findViewById(R.id.layoutMaterial).setVisibility(View.GONE);
                            if (!jsonObject.getJSONObject("lesson_details").getString("study_material").isEmpty()) {
                                findViewById(R.id.layoutMaterial).setVisibility(View.VISIBLE);
                                pdfURL = jsonObject.getJSONObject("lesson_details").getString("study_material");

                                new RetrievePdfFromUrl().execute(pdfURL, "Study");
                            }

                            findViewById(R.id.layoutAssignment).setVisibility(View.GONE);
                            if (!jsonObject.getJSONObject("lesson_details").getString("assignment").isEmpty()) {
                                findViewById(R.id.layoutAssignment).setVisibility(View.VISIBLE);
                                txtAssignment.setText(jsonObject.getJSONObject("lesson_details").getString("assignment"));
                                assignmentText = jsonObject.getJSONObject("lesson_details").getString("assignment");
                            }

                            if (!jsonObject.getJSONObject("lesson_details").getString("assignment").isEmpty()) {
                                layoutUpload.setVisibility(View.VISIBLE);
                            } else {
                                layoutUpload.setVisibility(View.VISIBLE);
                            }

                            facultyFeedBack = jsonObject.getString("faculty_feedback");
                            if (!facultyFeedBack.isEmpty()) {
                                layoutFeedback.setVisibility(View.VISIBLE);
                                feedbackByFaculty.setText(facultyFeedBack);
                            }

                            if (coursesList.size() > 0) {
                                getLifecycle().addObserver(youTubePlayerView);
                                playVideoAtSelection();
                            }
                            fabNext.setClickable(true);
                            fabPrevious.setClickable(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fabNext.setClickable(true);
                fabPrevious.setClickable(true);
                hocLoadingDialog.hideDialog();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                hocLoadingDialog.hideDialog();
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

    public void getLessonDetails(final Context context, String courseId, String lessonId) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "lessons");
            params.put("apikey", context.getResources().getString(R.string.lblApiKey));
            params.put("phone", UserDataConstants.userMobile);
            params.put("course_id", courseId);
            params.put("lesson_id", lessonId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        hocLoadingDialog.showLoadingDialog();
        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getEnrolledLessonDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hocLoadingDialog.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null && jsonObject.length() > 0) {
                        if (jsonObject.optString("status").equalsIgnoreCase("ok")) {

                            if (jsonObject.getJSONObject("lesson_details").getInt("watched_percentage")!=0) {
                                int nPercent = jsonObject.getJSONObject("lesson_details").getInt("watched_percentage");
                                textPercentage.setText(String.valueOf(jsonObject.getJSONObject("lesson_details").getInt("watched_percentage")) + "%");
                                if (nPercent == 100) {
                                    imgVideoCompleted.setVisibility(View.VISIBLE);
                                    layoutPercent.setVisibility(View.GONE);
                                }

                                percentView.setPercentage(nPercent);
                            }
                        }
                    }
                } catch (Exception e) {
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
                hocLoadingDialog.hideDialog();
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

    // create an async task class for loading pdf file from URL.
    class RetrievePdfFromUrl extends AsyncTask<String, Void, InputStream> {

        private String strView = "";

        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputStream
            // for getting out PDF.
            strView = strings[1];
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    savePdf(inputStream, strView + ".pdf");
                }
            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.

            File file = new File(getExternalFilesDir("pdf"), strView + ".pdf");
            if (file != null && file.exists()) {
                if (strView.equalsIgnoreCase("Study")) {

                    pdfViewStudy.setAdapter(new PDFPagerAdapter.Builder(MyCoursesLessonsPage.this)
                            .setPdfPath(file.getAbsolutePath())
                            .setScale(getPdfScale())
                            .setOnPageClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    player.pause();
                                    pdfDialogue(getString(R.string.study_material), "Study");
                                }
                            })
                            .create());
                    pdfViewStudy.setNestedScrollingEnabled(true);
                } else {

                    /*pdfViewAssign.setAdapter(new PDFPagerAdapter.Builder(MyCoursesLessonsPage.this)
                            .setPdfPath(file.getAbsolutePath())
                            .setScale(getPdfScale())
                            .setOnPageClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    pdfDialogue(getString(R.string.assignment), "Assign");
                                }
                            })
                            .create());
                    pdfViewAssign.setNestedScrollingEnabled(true);*/
                }
            }
        }
    }

    private PdfScale getPdfScale() {
        PdfScale scale = new PdfScale();
        scale.setScale(3.0f);
        scale.setCenterX(getScreenWidth(this) / 2);
        scale.setCenterY(0f);
        return scale;
    }

    private int getScreenWidth(Context ctx) {
        int width = 0;
        if (ctx instanceof Activity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            width = displaymetrics.widthPixels;
        }
        return width;
    }

    private void savePdf(InputStream inputStream, String strFileName) throws IOException {
        try {
            File file = new File(getExternalFilesDir("pdf"), strFileName);
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
    }

    public void saveLessonLikeDislike(final Context context, String strAPI) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "lessons");
            params.put("apikey", context.getResources().getString(R.string.lblApiKey));
            params.put("phone", UserDataConstants.userMobile);
            params.put("lesson_id", postId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        hocLoadingDialog.showLoadingDialog();
        StringRequest sr = new StringRequest(Request.Method.POST, strAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hocLoadingDialog.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("like")) {
                        imageLike.setImageResource(R.drawable.ic_up_like_yes);
                        imageDisLike.setImageResource(R.drawable.ic_down_dislike);
                    } else {
                        imageLike.setImageResource(R.drawable.ic_up_like);
                        imageDisLike.setImageResource(R.drawable.ic_down_dislike_yes);
                    }
                } catch (Exception e) {
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
                hocLoadingDialog.hideDialog();
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

    private String imageToBase64(String UriPath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(UriPath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            //imageUrls.add(Base64.encodeToString(bytes, Base64.DEFAULT));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void uploadAssignment(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("appname", "Hamstech");
            params.put("page", "lessons");
            params.put("apikey", context.getResources().getString(R.string.lblApiKey));
            params.put("phone", UserDataConstants.userMobile);
            params.put("course_id", courseId);
            params.put("lesson_id", lessonId);
            //params.put("assignment", imageUrls);
            params.put("assignment", imageToBase64(strFilePath));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.uploadAssignment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hocLoadingDialog.hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null && jsonObject.length() > 0) {

                        strFilePath = "";
                        assignmentFile = null;
                        textAssignFile.setText(getString(R.string.select_files));
                        uploadSuccessPopUp();
                    }
                } catch (Exception e) {
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
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }
        };
        queue.add(sr);
    }

    public void loadPlayer() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                CategoryName = coursesList.get(intNext).getCourseTitle();
                CourseLog = coursesList.get(intNext).getCourseTitle();
                LessonLog = coursesList.get(intNext).getLessonTitle();
                ActivityLog = "Lesson Page";
                player.loadVideo(mp4URL, 0);
                watchNext();
            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "Lesson Page";
                if (state.toString().equals("PLAYING")) {
                    PagenameLog = "Video start";
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (state.toString().equals("PAUSED")) {
                    PagenameLog = "Video paused";
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (state.toString().equals("STOPPED")) {
                    PagenameLog = "Video stopped";
                    getLogEvent(MyCoursesLessonsPage.this);
                } else if (state.toString().equals("ENDED")){
                    PagenameLog = "Video Ended";
                    getLogEvent(MyCoursesLessonsPage.this);
                }
            }
        });

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                layoutHeader.setVisibility(View.GONE);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
    }

    private void playVideoAtSelection() {

        if (mp4URL.equalsIgnoreCase("")){
            playerLayout.setVisibility(View.GONE);
            videoThumbnail.setVisibility(View.VISIBLE);
            Glide.with(MyCoursesLessonsPage.this)
                    .load(videoThumbnailURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(videoThumbnail);

            if (player != null) {
                player.pause();
            }

        } else {
            if (player != null) {
                CourseLog = coursesList.get(intNext).getCourseTitle();
                videoThumbnail.setVisibility(View.GONE);
                playerLayout.setVisibility(View.VISIBLE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                playerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelSize(R.dimen.dimen_entry_in_dp)));
                player.loadVideo(mp4URL, 0);
                logLessonWatchedEvent(CourseLog, LessonLog);
            } else {
                loadPlayer();
            }
        }


    }

    public void watchNext() {
        try {
            fabNext.setClickable(false);
            fabPrevious.setClickable(false);
            hocLoadingDialog.showLoadingDialog();
            imageLike.setImageResource(R.drawable.ic_up_like);
            imageDisLike.setImageResource(R.drawable.ic_down_dislike);
            if (intNext < coursesList.size()) {
                intPrevious = intNext;
                intNext = intNext + 1;
                fabNext.setVisibility(View.VISIBLE);

                if (intNext == coursesList.size()) {
                    fabNext.setVisibility(View.GONE);
                    intNext = 0;
                } else {
                    if (coursesList.get(intNext).getLockValue().equalsIgnoreCase("0")){
                        intNext = intNext + 1;
                        watchNext();
                        return;
                    } else if (coursesList.get(intNext).getLockValue().equalsIgnoreCase("1")) {
                        nextLessonTitle.setText(coursesList.get(intNext).getLessonTitle());
                    }

                }
                if (intNext - 1 == 0) {
                    fabPrevious.setVisibility(View.INVISIBLE);
                } else {
                    fabPrevious.setVisibility(View.VISIBLE);
                    previousLessonTitle.setText(coursesList.get(intPrevious - 1).getLessonTitle());
                }
                nextContentLoad(intPrevious);
            } else if (intNext >= coursesList.size()) {
                intNext = 0;
                fabNext.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watchPrevious() {
        try {
            fabNext.setClickable(false);
            fabPrevious.setClickable(false);
            hocLoadingDialog.showLoadingDialog();
            imageLike.setImageResource(R.drawable.ic_up_like);
            imageDisLike.setImageResource(R.drawable.ic_down_dislike);

            intPrevious = intPrevious - 1;
            if (intPrevious < coursesList.size() - 1) {
                intNext = intPrevious + 1;
                previousContentLoad(intPrevious);
                if (intPrevious == coursesList.size() - 1 || intPrevious == 0) {
                    fabPrevious.setVisibility(View.INVISIBLE);
                }
                if (intPrevious == 0) {
                    fabPrevious.setVisibility(View.INVISIBLE);
                    fabNext.setVisibility(View.GONE);
                } else {
                    fabPrevious.setVisibility(View.VISIBLE);
                    nextLessonTitle.setText(coursesList.get(intNext).getLessonTitle());
                    previousLessonTitle.setText(coursesList.get(intPrevious - 1).getLessonTitle());
                }
                if (intNext < coursesList.size()) {
                    fabNext.setVisibility(View.VISIBLE);
                } else {
                    fabNext.setVisibility(View.GONE);
                }
            } else if (intPrevious == (coursesList.size() - 1)) {
                intPrevious = 0;
                fabPrevious.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextContentLoad(int listPosition) {
        if (coursesList.get(listPosition).getLockValue().equalsIgnoreCase("1")) {
            fabNext.setClickable(true);
        } else fabNext.setClickable(false);
        headerTitle.setText(coursesList.get(listPosition).getCourseTitle());
        //txtDescription.setText(coursesList.get(listPosition).getLessonDescription());
        txtLessonName.setText(coursesList.get(listPosition).getLessonTitle());
        lessonId = coursesList.get(listPosition).getLessonId();
        //txtImageText.setText(coursesList.get(listPosition).getLessonPageText());
        CategoryName = coursesList.get(listPosition).getCourseTitle();
        CourseLog = coursesList.get(listPosition).getCourseTitle();
        LessonLog = coursesList.get(listPosition).getLessonTitle();
        CourseId = courseId;
        postId = coursesList.get(listPosition).getLessonId();
        //pdfURL = coursesList.get(listPosition).getStudyMaterialUrl();

        ActivityLog = "Click";
        PagenameLog = "Lesson Page";
        getLogEvent(MyCoursesLessonsPage.this);
        AppsFlyerEvent(listPosition);
        new AppsFlyerEventsHelper(MyCoursesLessonsPage.this).EventLessons(coursesList.get(listPosition).getCourseTitle(),
                coursesList.get(listPosition).getCourseTitle(), coursesList.get(listPosition).getLessonTitle());
        //ActivityLog = getIntent().getStringExtra("LessonName");
        //linearLessonContent.setVisibility(View.VISIBLE);

        textPercentage.setText(coursesList.get(listPosition).getWatchedPercentage() + "%");
        int nPercent = Integer.parseInt(coursesList.get(listPosition).getWatchedPercentage());
        if (nPercent == 100) {
            imgVideoCompleted.setVisibility(View.VISIBLE);
            layoutPercent.setVisibility(View.GONE);
        }
        percentView.setPercentage(nPercent);
        if (coursesList.get(listPosition).getVideoUrl().equalsIgnoreCase("")){
            videoThumbnailURL = coursesList.get(listPosition).getVideo_thumbnail();
            playerLayout.setVisibility(View.GONE);
        } else {
            mp4URL = coursesList.get(listPosition).getVideoUrl();
            playerLayout.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.layoutMaterial).setVisibility(View.GONE);
        if (!coursesList.get(listPosition).getStudyMaterialUrl().isEmpty()) {
            findViewById(R.id.layoutMaterial).setVisibility(View.VISIBLE);
            pdfURL = coursesList.get(listPosition).getStudyMaterialUrl();

            new RetrievePdfFromUrl().execute(pdfURL, "Study");
        }

        findViewById(R.id.layoutAssignment).setVisibility(View.GONE);
        if (!coursesList.get(listPosition).getAssignment_url().isEmpty()) {
            findViewById(R.id.layoutAssignment).setVisibility(View.VISIBLE);
            assignURL = coursesList.get(listPosition).getAssignment_url();

            new RetrievePdfFromUrl().execute(assignURL, "Assign");
        }

        if (!coursesList.get(listPosition).getVideoUrl().isEmpty()) {
            playVideoAtSelection();
        }
        hocLoadingDialog.hideDialog();
        getEnrolledLessonDetails(this, courseId, coursesList.get(listPosition).getLessonId());
    }

    public void previousContentLoad(int listPosition) {
        if (coursesList.get(listPosition).getLockValue().equalsIgnoreCase("1")) {
            fabPrevious.setClickable(true);
        } else fabPrevious.setClickable(false);
        headerTitle.setText(coursesList.get(listPosition).getCourseTitle());
        //txtDescription.setText(coursesList.get(listPosition).getLessonDescription());
        txtLessonName.setText(coursesList.get(listPosition).getLessonTitle());
        //txtImageText.setText(coursesList.get(listPosition).getLessonPageText());
        CategoryName = coursesList.get(listPosition).getCourseTitle();
        CourseLog = coursesList.get(listPosition).getCourseTitle();
        LessonLog = coursesList.get(listPosition).getLessonTitle();
        CourseId = courseId;
        postId = coursesList.get(listPosition).getLessonId();
        //pdfURL = coursesList.get(listPosition).getStudyMaterialUrl();

        ActivityLog = "Click";
        PagenameLog = "Lesson Page";
        getLogEvent(MyCoursesLessonsPage.this);
        AppsFlyerEvent(listPosition);
        new AppsFlyerEventsHelper(MyCoursesLessonsPage.this).EventLessons(coursesList.get(listPosition).getCourseTitle(),
                coursesList.get(listPosition).getCourseTitle(), coursesList.get(listPosition).getLessonTitle());
        //ActivityLog = getIntent().getStringExtra("LessonName");
        //linearLessonContent.setVisibility(View.VISIBLE);

        //getEnrolledLessonDetails(this, courseId, postId);

        textPercentage.setText(coursesList.get(listPosition).getWatchedPercentage() + "%");
        int nPercent = Integer.parseInt(coursesList.get(listPosition).getWatchedPercentage());
        percentView.setPercentage(nPercent);
        if (coursesList.get(listPosition).getVideoUrl().equalsIgnoreCase("")){
            videoThumbnailURL = coursesList.get(listPosition).getVideo_thumbnail();
            playerLayout.setVisibility(View.GONE);
        } else {
            mp4URL = coursesList.get(listPosition).getVideoUrl();

        }

        findViewById(R.id.layoutMaterial).setVisibility(View.GONE);
        if (!coursesList.get(listPosition).getStudyMaterialUrl().isEmpty()) {
            findViewById(R.id.layoutMaterial).setVisibility(View.VISIBLE);
            pdfURL = coursesList.get(listPosition).getStudyMaterialUrl();

            new RetrievePdfFromUrl().execute(pdfURL, "Study");
        }

        findViewById(R.id.layoutAssignment).setVisibility(View.GONE);
        if (!coursesList.get(listPosition).getAssignment_url().isEmpty()) {
            findViewById(R.id.layoutAssignment).setVisibility(View.VISIBLE);
            assignURL = coursesList.get(listPosition).getAssignment_url();

            new RetrievePdfFromUrl().execute(assignURL, "Assign");
        }

        if (!coursesList.get(listPosition).getVideoUrl().isEmpty()) {
            playVideoAtSelection();
        }
        hocLoadingDialog.hideDialog();
        getEnrolledLessonDetails(this, courseId, coursesList.get(listPosition).getLessonId());
    }

    public void logContactusEvent(String eventValue) {
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, eventValue);
        logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, params);
        params.putString(Params.CONTENT_TYPE, eventValue);
        firebaseAnalytics.logEvent("contact_us", params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(eventValue);
    }

    public void logLessonWatchedEvent(String course, String lessonName) {
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, course);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, lessonName);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL, params);
        params.putString(Params.COURSE_NAME, course);
        params.putString(Params.LESSON_NAME, lessonName);
        firebaseAnalytics.logEvent(Params.LESSONS_WATCHED, params);
        new FacebookEventsHelper(this).logSpendCreditsEvent(course);
    }

    public void AppsFlyerEvent(int position) {
        if (position == 0) {
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCourseTitle(),
                    coursesList.get(position).getCourseTitle(), coursesList.get(position).getLessonTitle(), "Trial Class 1");
        } else if (position == 1) {
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCourseTitle(),
                    coursesList.get(position).getCourseTitle(), coursesList.get(position).getLessonTitle(), "Trial Class 2");
        } else if (position == 2) {
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCourseTitle(),
                    coursesList.get(position).getCourseTitle(), coursesList.get(position).getLessonTitle(), "How you learn");
        } else if (position == 3) {
            new AppsFlyerEventsHelper(this).EventLessonsDetails(coursesList.get(position).getCourseTitle(),
                    coursesList.get(position).getCourseTitle(), coursesList.get(position).getLessonTitle(), "Student testimonial");
        }
    }

    public class StudentWorkSliderAdapter extends PagerAdapter {

        Context context;
        ArrayList<StudentsWork> studentsWorks;

        public StudentWorkSliderAdapter(Context context, ArrayList<StudentsWork> studentsWorks) {
            this.context = context;
            this.studentsWorks = studentsWorks;
        }

        @Override
        public int getCount() {
            return studentsWorks.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.student_work_slider_adapter, container, false);
            container.addView(view);
            bind(view,position);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        private void bind(View view, final int position) {
            ImageView imgStudentWork = view.findViewById(R.id.imgStudentWork);
            ImageView imgPlayButton = view.findViewById(R.id.imgPlayButton);

            if (studentsWorks.get(position).getType().equalsIgnoreCase("image")) {
                Glide.with(context)
                        .asGif()
                        .load(studentsWorks.get(position).getSrc())
                        //.placeholder(R.drawable.duser1)
                        .into(imgStudentWork);
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(studentsWorks.get(position).getThumbnail())
                        //.placeholder(R.drawable.duser1)
                        .into(imgStudentWork);
            }

            if (studentsWorks.get(position).getType().equalsIgnoreCase("video")) {
                imgPlayButton.setVisibility(View.VISIBLE);
            }
            imgPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        howtoUseApp = new VideoDialogue(context,studentsWorks.get(position).getSrc());
                        howtoUseApp.showLoadingDialog();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(MyCoursesLessonsPage.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.sucess_upload_assignment_lessons);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);

        Glide.with(MyCoursesLessonsPage.this)
                .load(R.drawable.ic_sucess_assignment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_assignment)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void AssignmentPopUp(String assignment) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.assignment_popup);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        TextView txtAssignment = dialog.findViewById(R.id.txtAssignment);

        onlinePaymentLayout.setVisibility(View.VISIBLE);

        txtAssignment.setText(assignment);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {

                    return true;
                }
                return false;
            }
        });
    }

    public void getLogEvent(Context context) {
        JSONObject data = new JSONObject();
        try {
            data.put("apikey", context.getResources().getString(R.string.lblApiKey));
            data.put("appname", "Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname", UserDataConstants.userName);
            data.put("email", UserDataConstants.userMail);
            data.put("category", CategoryLog);
            data.put("course", CourseLog);
            data.put("lesson", LessonLog);
            data.put("activity", ActivityLog);
            data.put("pagename", PagenameLog);
            Log.e("log","1786   "+data);
            logEventsActivity.LogEventsActivity(context, data);
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