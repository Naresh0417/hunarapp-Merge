package com.hamstechonline.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hamstechonline.R;
import com.hamstechonline.activities.dialogs.BuzzDetailsDialog;
import com.hamstechonline.activities.dialogs.DiscussionDetailsDialog;
import com.hamstechonline.activities.dialogs.ReportBlockDialoge;
import com.hamstechonline.activities.dialogs.ReportDialoge;
import com.hamstechonline.adapters.FacultySliderAdapter;
import com.hamstechonline.adapters.MyCourseOverviewAdapter;
import com.hamstechonline.adapters.MyCoursePagerAdapter;
import com.hamstechonline.adapters.MyCoursesLessonsListAdapter;
import com.hamstechonline.adapters.SimilarCoursesListAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.BuzzDataModel;
import com.hamstechonline.datamodel.CallWithFacultyResponse;
import com.hamstechonline.datamodel.Discussions;
import com.hamstechonline.datamodel.DiscussionsModel;
import com.hamstechonline.datamodel.HocTodayData;
import com.hamstechonline.datamodel.LastLessonDetails;
import com.hamstechonline.datamodel.PayinstallmentRequest;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.UploadPostDisscussions;
import com.hamstechonline.datamodel.UploadPostResponse;
import com.hamstechonline.datamodel.mycources.Lesson;
import com.hamstechonline.datamodel.mycources.MyCoursesResponse;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.DynamicWhatsAppChat;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LikesInterface;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.hamstechonline.utils.WrapContentViewPager;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCoursesPageActivity extends AppCompatActivity implements LikesInterface,
        BottomNavigationView.OnNavigationItemSelectedListener, PaymentResultWithDataListener {

    DrawerLayout drawer;
    TextView txtLessons,txtDiscussion,txtChat,txtCallRequest,txtDescription,txtNextLessons,txtTitle;
    RecyclerView lessonsList,listSimilarCourses,listOverview,discussionList;
    BottomNavigationView navigation;
    ImageView imgKnowHow,imgNextLesson,playButton,imgWhatsApp;
    CheckBox txtSeeAll,overviewExpand;
    ImageButton stickyWhatsApp;
    RelativeLayout submitPost,lessonsExpand,facultyLayput;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    int mMenuId,nextListSize = 0,listSelectedPosition = 0;
    LogEventsActivity logEventsActivity;
    String CategoryName = "",CourseLog = "",LessonLog="",ActivityLog,PagenameLog;
    String courseId,language,langPref = "Language",mobile = "",tracking_id = "",
            fullname = "",email = "",mp4URL,intLike = "100",likevalueCount = "",lessonEvent,order_id;
    AppEventsLogger logger;
    boolean likeStatus = false;
    Bundle params;
    SharedPreferences prefs;
    UserDataBase userDataBase;
    ApiInterface apiService;
    private MyCourseOverviewAdapter overviewAdapter;
    HocLoadingDialog hocLoadingDialog;
    DiscussionsAdapter discussionsAdapter;
    WrapContentViewPager listFaculty;

    MyCoursesLessonsListAdapter myCoursesLessonsListAdapter;
    SimilarCoursesListAdapter similarCoursesListAdapter;
    CheckBox expandLessonsList, checkboxOverview;
    LastLessonDetails lessonsListArray;
    List<Lesson> originalListArray = new ArrayList<>();
    List<Lesson> matchedListArray = new ArrayList<>();
    List<Lesson> nextListArray = new ArrayList<>();
    int matchedPosition = 1001;
    LinearLayout discussionLayout,lessonsLayout;
    ArrayList<DiscussionsModel> dataArrayList = new ArrayList<>();
    private final int PERM_READ_WRITE_STORAGE = 101;
    private static final int FILE_SELECT_CODE = 0;
    Bitmap mBitmap, resized;
    String type_image, picturePath = "", imgString,uploadConentent,uploadFilePath = "",uploadTitle = "",term_id = "",m_strEmail;
    private String imagePathData = "";
    FacultySliderAdapter facultySliderAdapter;
    DiscussionDetailsDialog buzzDetailsDialog;
    DynamicWhatsAppChat dynamicWhatsAppChat;
    ReportBlockDialoge reportDialoge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_course);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        txtLessons = findViewById(R.id.txtLessons);
        txtDiscussion = findViewById(R.id.txtDiscussion);
        lessonsList = findViewById(R.id.lessonsList);
        listSimilarCourses = findViewById(R.id.listSimilarCourses);
        listOverview = findViewById(R.id.listOverview);
        imgKnowHow = findViewById(R.id.imgKnowHow);
        imgNextLesson = findViewById(R.id.imgNextLesson);
        txtNextLessons = findViewById(R.id.txtNextLessons);
        expandLessonsList = findViewById(R.id.expandLessonsList);
        txtChat = findViewById(R.id.txtChat);
        txtCallRequest = findViewById(R.id.txtCallRequest);
        txtSeeAll = findViewById(R.id.txtSeeAll);
        overviewExpand = findViewById(R.id.overviewExpand);
        checkboxOverview = findViewById(R.id.checkboxOverview);
        discussionLayout = findViewById(R.id.discussionLayout);
        lessonsLayout = findViewById(R.id.lessonsLayout);
        discussionList = findViewById(R.id.discussionList);
        playButton = findViewById(R.id.playButton);
        submitPost = findViewById(R.id.submitPost);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        listFaculty = findViewById(R.id.listFaculty);
        facultyLayput = findViewById(R.id.facultyLayput);
        txtDescription = findViewById(R.id.txtDescription);
        lessonsExpand = findViewById(R.id.lessonsExpand);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        txtTitle = findViewById(R.id.txtTitle);

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);

        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();
        logEventsActivity = new LogEventsActivity();
        hocLoadingDialog = new HocLoadingDialog(MyCoursesPageActivity.this);

        txtLessons.setBackground(getResources().getDrawable(R.drawable.border_pink_strok));
        txtLessons.setTextColor(getResources().getColor(R.color.dark_pink));
        txtDiscussion.setBackgroundColor(getResources().getColor(R.color.white));
        txtDiscussion.setTextColor(getResources().getColor(R.color.muted_blue));

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        courseId = getIntent().getStringExtra("CategoryId");
        CourseLog = getIntent().getStringExtra("CourseName");
        order_id = getIntent().getStringExtra("order_id");
        m_strEmail = getIntent().getStringExtra("email");
        language = getIntent().getStringExtra("language");
        langPref = prefs.getString("Language", "en");
        userDataBase = new UserDataBase(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        imgWhatsApp.setVisibility(View.GONE);

        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        getCourseDetails();

        txtLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLessons.setBackground(getResources().getDrawable(R.drawable.border_pink_strok));
                txtLessons.setTextColor(getResources().getColor(R.color.dark_pink));
                txtDiscussion.setBackgroundColor(getResources().getColor(R.color.white));
                txtDiscussion.setTextColor(getResources().getColor(R.color.muted_blue));
                discussionLayout.setVisibility(View.GONE);
                submitPost.setVisibility(View.GONE);
                lessonsLayout.setVisibility(View.VISIBLE);
                ActivityLog = "Lessons";
                PagenameLog = "MyCourse page";
                getLogEvent(MyCoursesPageActivity.this);
            }
        });

        txtDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLessons.setBackgroundColor(getResources().getColor(R.color.white));
                txtLessons.setTextColor(getResources().getColor(R.color.muted_blue));
                txtDiscussion.setBackground(getResources().getDrawable(R.drawable.border_pink_strok));
                txtDiscussion.setTextColor(getResources().getColor(R.color.dark_pink));
                discussionLayout.setVisibility(View.VISIBLE);
                lessonsLayout.setVisibility(View.GONE);
                ActivityLog = "Discussions";
                PagenameLog = "MyCourse page";
                getLogEvent(MyCoursesPageActivity.this);
                getDiscussions();
            }
        });

        similarCoursesListAdapter = new SimilarCoursesListAdapter(this);
        listSimilarCourses.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        listSimilarCourses.setAdapter(similarCoursesListAdapter);

        expandLessonsList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtSeeAll.setVisibility(View.GONE);
                    txtSeeAll.setChecked(true);
                    myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,matchedListArray,
                            matchedListArray.size(),courseId,matchedPosition,true,order_id,m_strEmail,originalListArray);
                    lessonsList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    lessonsList.setAdapter(myCoursesLessonsListAdapter);
                } else {
                    txtSeeAll.setVisibility(View.VISIBLE);
                    txtSeeAll.setChecked(false);
                    myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,matchedListArray,
                            nextListSize,courseId,matchedPosition,false,order_id,m_strEmail,originalListArray);
                    lessonsList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    lessonsList.setAdapter(myCoursesLessonsListAdapter);
                }
            }
        });

        txtSeeAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtSeeAll.setVisibility(View.GONE);
                    expandLessonsList.setChecked(true);
                    myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,matchedListArray,
                            matchedListArray.size(),courseId,matchedPosition,true,order_id,m_strEmail,originalListArray);
                    lessonsList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    lessonsList.setAdapter(myCoursesLessonsListAdapter);
                } else {
                    txtSeeAll.setVisibility(View.VISIBLE);
                    expandLessonsList.setChecked(false);
                    myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,matchedListArray,
                            nextListSize,courseId,matchedPosition,false,order_id,m_strEmail,originalListArray);
                    lessonsList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    lessonsList.setAdapter(myCoursesLessonsListAdapter);
                }
            }
        });

        checkboxOverview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    overviewExpand.setChecked(true);
                    listOverview.setVisibility(View.VISIBLE);
                } else {
                    overviewExpand.setChecked(false);
                    listOverview.setVisibility(View.GONE);
                }
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Chat with student guid";
                PagenameLog = "MyCourse page";
                getLogEvent(MyCoursesPageActivity.this);
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919666664757" +"&text=" +
                            URLEncoder.encode("Hello, I want to talk to my student guide", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                }
                /*dynamicWhatsAppChat = new DynamicWhatsAppChat(MyCoursesPageActivity.this,"MyCourse page");
                dynamicWhatsAppChat.getMyCourseChatNumber(userDataBase.getUserMobileNumber(1),courseId);*/
            }
        });
        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dynamicWhatsAppChat = new DynamicWhatsAppChat(MyCoursesPageActivity.this,"MyCourse page",CourseLog,"");
                dynamicWhatsAppChat.getChatNumber(userDataBase.getUserMobileNumber(1));

            }
        });
        txtCallRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Video Call with Faculty";
                PagenameLog = "MyCourse page";
                getLogEvent(MyCoursesPageActivity.this);
                getCallWithFaculty();
            }
        });

        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Submit your post";
                PagenameLog = "MyCourse page";
                getLogEvent(MyCoursesPageActivity.this);
                DiscussionUploadPopUp();
            }
        });

        imgNextLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lessonsListArray.getLockValue().equalsIgnoreCase("1")) {
                    Intent intent = new Intent(MyCoursesPageActivity.this, MyCoursesLessonsPage.class);
                    intent.putExtra("videoURL", lessonsListArray.getVideoUrl());
                    intent.putExtra("CategoryName", lessonsListArray.getCourseTitle());
                    intent.putExtra("CategoryLog", "");
                    intent.putExtra("CourseName", lessonsListArray.getCourseTitle());
                    intent.putExtra("LessonName", lessonsListArray.getLessonTitle());
                    intent.putExtra("description", lessonsListArray.getLessonDescription());
                    intent.putExtra("pdfURL", lessonsListArray.getStudyMaterialUrl());
                    intent.putExtra("CourseId", courseId);
                    intent.putExtra("LessonId", lessonsListArray.getLessonId());
                    intent.putExtra("LessonImage", lessonsListArray.getLessonImageUrl());
                    intent.putExtra("LessonText", lessonsListArray.getLessonImageUrl());
                    //intent.putParcelableArrayListExtra("LessonData", (ArrayList<Lesson>) coursesList);
                    intent.putExtra("LessonData", (ArrayList<? extends Serializable>) originalListArray);
                    intent.putExtra("intNext", matchedPosition);
                    startActivity(intent);
                } else {
                    lockPopup();
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

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }

    public void lockPopup() {
        final Dialog dialog = new Dialog(MyCoursesPageActivity.this);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.lock_popup);
        dialog.setCancelable(true);

        Button btnNext = dialog.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayInstallmentAPi(order_id);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void PayInstallmentAPi(String order_id) {
        hocLoadingDialog.showLoadingDialog();
        PayinstallmentRequest payinstallmentRequest = new PayinstallmentRequest(order_id,courseId);
        Call<PayinstallmentRequest> call = apiService.getPayinstallment(payinstallmentRequest);
        call.enqueue(new Callback<PayinstallmentRequest>() {
            @Override
            public void onResponse(Call<PayinstallmentRequest> call, retrofit2.Response<PayinstallmentRequest> response) {
                if (response.body().getRazorpayOrderId() != null) {
                    hocLoadingDialog.hideDialog();
                    startPayment(response.body().getRazorpayOrderId(),response.body().getAmount());

                }
            }

            @Override
            public void onFailure(Call<PayinstallmentRequest> call, Throwable t) {

            }
        });
    }

    public void getCourseDetails() {
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",courseId,language,langPref,userDataBase.getUserMobileNumber(1),order_id);
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {

                txtTitle.setText(response.body().getCourseDetails().getCourseTitle());

                txtDescription.setText(response.body().getCourseDetails().getCourseDescription());
                overviewAdapter = new MyCourseOverviewAdapter(MyCoursesPageActivity.this, response.body().getCourseOverview());
                matchedListArray.clear();

                if (response.body().getCourseOverview().size() != 0) {
                    checkboxOverview.setClickable(true);
                    overviewExpand.setVisibility(View.VISIBLE);
                    listOverview.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    listOverview.addItemDecoration(new GridSpacingItemDecoration(1, 5, false));
                    listOverview.setAdapter(overviewAdapter);
                } else {
                    checkboxOverview.setClickable(false);
                    overviewExpand.setVisibility(View.GONE);
                }

                facultySliderAdapter = new FacultySliderAdapter(MyCoursesPageActivity.this,response.body().getExpertFaculty());
                listFaculty.setOffscreenPageLimit(response.body().getExpertFaculty().size());
                listFaculty.setAdapter(facultySliderAdapter);

                Glide.with(MyCoursesPageActivity.this)
                        .load(response.body().getKnowHowImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(imgKnowHow);
                Glide.with(MyCoursesPageActivity.this)
                        .load(response.body().getLastLessonDetails().getLessonImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                        .error(R.mipmap.ic_launcher)
                        .into(imgNextLesson);


                for (int k =0; k<response.body().getLessons().size(); k++) {
                    if (response.body().getLessons().get(k).getType().equalsIgnoreCase("video")
                    || response.body().getLessons().get(k).getType().equalsIgnoreCase("nsdc_exam")
                    || response.body().getLessons().get(k).getType().equalsIgnoreCase("live")){
                        matchedListArray.add(response.body().getLessons().get(k));
                        if (response.body().getLessons().get(k).getLockValue().equalsIgnoreCase("1")) {
                            originalListArray.add(response.body().getLessons().get(k));
                        }

                    }
                }
                lessonsListArray = response.body().getLastLessonDetails();

                //matchedListArray = response.body().getLessons();

                if (response.body().getLastLessonDetails() != null) {
                    response.body().getLastLessonDetails().getLessonId();
                    for (int i = 0; i<matchedListArray.size(); i++) {
                            if (matchedListArray.get(i).getLessonId().equalsIgnoreCase(response.body().getLastLessonDetails().getLessonId())) {
                            matchedPosition = i;
                            txtNextLessons.setText("Lesson "+matchedListArray.get(i).getOrderno()+": "+response.body().getLastLessonDetails().getLessonTitle());
                            if (matchedListArray.size() != 0) {
                                matchedListArray.remove(i);
                            }

                        }
                    }
                }

                getLessons();
            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {

            }
        });
    }

    public void getLessons() {
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",courseId,language,langPref,userDataBase.getUserMobileNumber(1),order_id);
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {

                nextListArray.clear();

                /*for (int j = 0; j< lessonsListArray.size(); j++) {
                    if (matchedPosition < j) {
                        nextListArray.add(lessonsListArray.get(j));
                    }
                }*/

                //nextListArray.add(originalListArray);

                if (matchedListArray.size() > 4) {
                    lessonsExpand.setVisibility(View.VISIBLE);
                    nextListSize = 4;
                } else {
                    nextListSize = matchedListArray.size();
                    lessonsExpand.setVisibility(View.GONE);
                }

                /*if (nextListArray.size() == matchedListArray.size()) {
                    if (nextListArray.size() > 4) {
                        lessonsExpand.setVisibility(View.VISIBLE);
                    }
                } else lessonsExpand.setVisibility(View.VISIBLE);*/

                //myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,nextListArray,nextListArray.size(),courseId, matchedPosition,false);
                myCoursesLessonsListAdapter = new MyCoursesLessonsListAdapter(MyCoursesPageActivity.this,matchedListArray,nextListSize,
                        courseId, matchedPosition,false,order_id,m_strEmail,originalListArray);
                lessonsList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                lessonsList.setAdapter(myCoursesLessonsListAdapter);

            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {

            }
        });
    }

    public void getDiscussions() {
        hocLoadingDialog.showLoadingDialog();
        DiscussionsModel discussionsModel = new DiscussionsModel("Hamstech",getResources().getString(R.string.lblApiKey),courseId, langPref,mobile);
        Call<DiscussionsModel> call = apiService.getDiscussionsData(discussionsModel);
        call.enqueue(new Callback<DiscussionsModel>() {
            @Override
            public void onResponse(Call<DiscussionsModel> call, retrofit2.Response<DiscussionsModel> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getList() != null) {
                    discussionsAdapter = new DiscussionsAdapter(MyCoursesPageActivity.this,response.body().getList());
                    discussionList.setLayoutManager(new LinearLayoutManager(MyCoursesPageActivity.this, RecyclerView.VERTICAL, false));
                    discussionList.setAdapter(discussionsAdapter);
                    submitPost.setVisibility(View.VISIBLE);
                    discussionList.scrollToPosition(listSelectedPosition);
                }
            }

            @Override
            public void onFailure(Call<DiscussionsModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    @Override
    public void setIsLiked(int isLiked,int selectedPosition) {
        if (isLiked == 1) {
            listSelectedPosition = selectedPosition;
            getDiscussions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }
    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
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
                ActivityLog = "";
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(this);
                Intent intentCourses = new Intent(this, HomePageActivity.class);
                startActivity(intentCourses);
                return true;
            case R.id.navigation_chat:
                ActivityLog = "Home Page";
                PagenameLog = "chat with whatsapp";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "chat with whatsapp");
                logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
                getLogEvent(this);
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    /*if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }*/
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.navigation_enrol:
                ActivityLog = "Home page";
                PagenameLog = "Success Story";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(this);
                Intent enrol = new Intent(this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                ActivityLog = "Click";
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(this);
                Intent hamstech = new Intent(this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                ActivityLog = "Home page";
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(this);
                new AppsFlyerEventsHelper(this).EventContactus();
                Intent about = new Intent(this, ContactActivity.class);
                startActivity(about);
                return true;
        }
        return false;
    }

    public void getCallWithFaculty() {
        hocLoadingDialog.showLoadingDialog();
        CallWithFacultyResponse callWithFacultyResponse = new CallWithFacultyResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),courseId);
        Call<CallWithFacultyResponse> call = apiService.getCallWithFacultyResponse(callWithFacultyResponse);
        call.enqueue(new Callback<CallWithFacultyResponse>() {
            @Override
            public void onResponse(Call<CallWithFacultyResponse> call, Response<CallWithFacultyResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    OnlineSuccessfulPopUp(MyCoursesPageActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CallWithFacultyResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.ViewHolder> {

        Context context;
        List<Discussions> dataBuzz;
        ImageView imgHamstech, imgZoom,imgPlayButton, btnChat,imgReport;
        TextView txtTitle, txtDescription,likesCount,imgLikeUnlike, txtComment,txtExternalLink,
                txtUserName,txtUserNameChar;
        LinearLayout btnShare;
        CircleImageView profile_image;

        public DiscussionsAdapter(Context context, List<Discussions> dataBuzz){
            this.context = context;
            this.dataBuzz = dataBuzz;
        }

        @NonNull
        @Override
        public DiscussionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.buzz_item_adapter, parent, false);
            DiscussionsAdapter.ViewHolder vh = new DiscussionsAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final DiscussionsAdapter.ViewHolder holder, int position) {
            try {
                txtDescription.setText(dataBuzz.get(position).getDescription());
                //txtTitle.setVisibility(View.GONE);
                txtTitle.setText(dataBuzz.get(position).getTitle());
                txtUserName.setText(dataBuzz.get(position).getName());
                if (!dataBuzz.get(position).getExternallink().isEmpty()) {
                    txtExternalLink.setText(dataBuzz.get(position).getExternallink());
                    txtExternalLink.setVisibility(View.VISIBLE);
                }
                if (!dataBuzz.get(position).getImage().isEmpty()) {
                    Glide.with(MyCoursesPageActivity.this)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(profile_image);
                    profile_image.setVisibility(View.VISIBLE);
                    txtUserName.setVisibility(View.VISIBLE);
                } else if (!dataBuzz.get(position).getName().isEmpty()) {
                    txtUserName.setText(dataBuzz.get(position).getName());
                    profile_image.setVisibility(View.GONE);
                    txtUserName.setVisibility(View.VISIBLE);
                } else if (!dataBuzz.get(position).getNameFirstCharacter().isEmpty()) {
                    txtUserNameChar.setText(dataBuzz.get(position).getNameFirstCharacter());
                    profile_image.setVisibility(View.GONE);
                    txtUserNameChar.setVisibility(View.VISIBLE);
                }
                if (dataBuzz.get(position).getVideourl().equals("")){
                    imgHamstech.setVisibility(View.VISIBLE);
                    imgPlayButton.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgHamstech);
                } else {
                    imgHamstech.setVisibility(View.VISIBLE);
                    imgPlayButton.setVisibility(View.VISIBLE);
                    mp4URL = dataBuzz.get(position).getVideourl();
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgHamstech);
                }

                if (!intLike.equalsIgnoreCase("100")) {
                    if (intLike.equalsIgnoreCase("1")){
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0,0,0);
                    }
                    likesCount.setText(likevalueCount+" Likes "+
                            dataBuzz.get(position).getComments()+" Comments");
                } else {
                    if (dataBuzz.get(position).getLikedislike() == 1){
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0,0,0);
                    }
                    likesCount.setText(dataBuzz.get(position).getLikes()+" Likes "+
                            dataBuzz.get(position).getComments()+" Comments");
                }
                //likeStatus = false;
                intLike = "100";
                imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dataBuzz.get(position).getLikedislike() == 1){
                            /*LessonLog = dataBuzz.get(position).getTitle();
                            ActivityLog = "UnLike";
                            PagenameLog = "Discussions";
                            CategoryName = "";
                            CourseLog = "";
                            getLogEvent(context);
                            imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            getLikeData(context,dataBuzz.get(position).getPostid());*/

                        } else {
                            LessonLog = dataBuzz.get(position).getTitle();
                            ActivityLog = "Like";
                            PagenameLog = "Discussions";
                            CourseLog = "";
                            getLogEvent(context);
                            imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0,0,0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            //getLogEvent(context);
                            imgLikeUnlike.setText((Integer.parseInt(dataBuzz.get(position).getLikes()) + 1)+" Likes "+
                                    dataBuzz.get(position).getComments()+" Comments");
                            imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                            getLikeData(context,dataBuzz.get(position).getPostid(),position);
                        }

                    }
                });

                txtComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //bundle.putParcelable("buzzdetails",dataArrayList.get(position));
                        /*Intent intent =  new Intent(context,BuzzDetailsActivity.class);
                        intent.putExtra("buzzData",dataArrayList.get(position));
                        context.startActivity(intent);*/
                        //bundle.putSerializable("buzzData",dataBuzz.get(position));
                        //dataBuzz
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Comment";
                        PagenameLog = "MyCourse page";
                        getLogEvent(context);
                        buzzDetailsDialog = new DiscussionDetailsDialog(MyCoursesPageActivity.this, position,dataBuzz,term_id,courseId);
                        buzzDetailsDialog.showLoadingDialog();
                    }
                });
                imgHamstech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //bundle.putParcelable("buzzdetails",dataArrayList.get(position));
                        /*Intent intent =  new Intent(context,BuzzDetailsActivity.class);
                        intent.putExtra("buzzData",dataArrayList.get(position));
                        context.startActivity(intent);*/
                        //bundle.putSerializable("buzzData",dataBuzz.get(position));
                        //dataBuzz
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Clicked on discussions post";
                        PagenameLog = "MyCourse page";
                        getLogEvent(context);
                        buzzDetailsDialog = new DiscussionDetailsDialog(MyCoursesPageActivity.this, position,dataBuzz,term_id,courseId);
                        buzzDetailsDialog.showLoadingDialog();
                    }
                });

                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Share";
                        PagenameLog = "MyCourse page";
                        getLogEvent(context);
                        if (dataBuzz.get(position).getVideourl().equals("")) {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "Text");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, dataBuzz.get(position).getImage());
                            startActivity(Intent.createChooser(shareIntent, dataBuzz.get(position).getTitle()));
                        }else {
                            PagenameLog = "Url Share";
                            String url = "https://www.youtube.com/watch?v="+dataBuzz.get(position).getVideourl();
                            getLogEvent(context);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                            startActivity(Intent.createChooser(shareIntent, dataBuzz.get(position).getTitle()));
                        }
                    }
                });
                txtExternalLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dataBuzz.get(position).getExternallink().equalsIgnoreCase("")) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dataBuzz.get(position).getExternallink()));
                            startActivity(i);
                        }
                    }
                });
                imgReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reportDialoge = new ReportBlockDialoge(MyCoursesPageActivity.this,dataBuzz.get(position).getPostid(),mobile);
                        reportDialoge.showLoadingDialog();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dataBuzz.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View view) {
                super(view);
                imgHamstech = view.findViewById(R.id.imgHamstech);
                txtTitle = view.findViewById(R.id.txtTitle);
                txtDescription = view.findViewById(R.id.txtDescription);
                imgLikeUnlike = view.findViewById(R.id.imgLikeUnlike);
                imgZoom = view.findViewById(R.id.imgZoom);
                imgPlayButton = view.findViewById(R.id.imgPlayButton);
                btnShare = view.findViewById(R.id.btnShare);
                likesCount = view.findViewById(R.id.likesCount);
                txtComment = view.findViewById(R.id.txtComment);
                txtExternalLink = view.findViewById(R.id.txtExternalLink);
                profile_image = view.findViewById(R.id.profile_image);
                txtUserName = view.findViewById(R.id.txtUserName);
                imgReport = view.findViewById(R.id.imgReport);
            }

        }

        public void getLikeData(final Context context,int postValue, int listPosition){

            RequestQueue queue = Volley.newRequestQueue(context);
            hocLoadingDialog.showLoadingDialog();
            JSONObject params = new JSONObject();
            JSONObject metaData = new JSONObject();
            try {
                params.put("appname","Hamstech");
                params.put("page","discussions");
                params.put("apikey",context.getResources().getString(R.string.lblApiKey));
                params.put("postid",postValue);
                params.put("phone", UserDataConstants.userMobile);
                metaData.put("metadata", params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String mRequestBody = metaData.toString();

            StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.save_like_dislike, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jo = new JSONObject(response);
                        dataArrayList.clear();
                        hocLoadingDialog.hideDialog();
                        likeStatus = true;
                        if (jo.getString("status").equalsIgnoreCase("like")){
                            intLike = "1";
                            likevalueCount = jo.getString("likes");
                        } else {
                            intLike = "0";
                            likevalueCount = jo.getString("likes");
                        }

                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                        listSelectedPosition = listPosition;
                        //getHocTodayList();
                        //notifyItemChanged(1,dataBuzz);
                        //notifyDataSetChanged();
                        getDiscussions();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
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

    }

    public void DiscussionUploadPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(R.layout.discussion_upload_popup);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        LinearLayout selectGallery = dialog.findViewById(R.id.selectGallery);
        TextView uploadFileName = dialog.findViewById(R.id.uploadFileName);
        EditText userInputContent = dialog.findViewById(R.id.userInputContent);
        TextView btnSubmit = dialog.findViewById(R.id.btnSubmit);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        selectGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermissions()) {
                    Toast.makeText(MyCoursesPageActivity.this,"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //uploadSuccessPopUp();
                    showFileChooser();
                    uploadFileName.setText(uploadFilePath);

                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadConentent = userInputContent.getText().toString().trim();
                uploadTitle = "";
                if (uploadConentent.equalsIgnoreCase("")){
                    Toast.makeText(MyCoursesPageActivity.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityLog = "Upload Post";
                    userInputContent.setText("");
                    hocLoadingDialog.showLoadingDialog();
                    uploadFile();
                    uploadConentent = ""; uploadFilePath = ""; uploadTitle = "";
                    dialog.dismiss();
                }
            }
        });
    }

    private  boolean checkPermissions() {
        int result;

        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(MyCoursesPageActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MyCoursesPageActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERM_READ_WRITE_STORAGE);
            return false;
        }

        return true;
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(i, FILE_SELECT_CODE);
        } catch (Exception e) {
            Toast.makeText(MyCoursesPageActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    Uri selectedImage = data.getData();
                    try {
                        mBitmap = MediaStore.Images.Media.getBitmap(MyCoursesPageActivity.this.getContentResolver(), selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    resized = Bitmap.createScaledBitmap(mBitmap,(int)(mBitmap.getWidth()*0.3), (int)(mBitmap.getHeight()*0.3), true);
                    //profile_image.setImageBitmap(resized);
                    if (resized != null) {
                        imagePathData = getEncoded64ImageStringFromBitmap(resized);
                        System.out.println(""+imagePathData);
                        //uploadFile();
                        //uploadImage(EditProfileActivity.this);
                    }
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    type_image = getContentResolver().getType(selectedImage);

                    if (type_image.equals(null)) type_image = "images/jpeg";

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    try{
                        File file = new File(picturePath);
                        long length = file.length();
                        length = length/1024;
                        uploadFilePath = file.getPath();
                    } catch(Exception e) {
                        System.out.println("File not found : " + e.getMessage() + e);
                    }

                    if (picturePath.isEmpty() || picturePath == null) {
                        Toast.makeText(MyCoursesPageActivity.this, "Unable to capture the image..Please try again", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(MyCoursesPageActivity.this,
                            "Image Loading Failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(MyCoursesPageActivity.this,
                        "User cancelled file upload", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(MyCoursesPageActivity.this,
                        "Sorry! Failed to load file", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public void uploadFile() {
        UploadPostDisscussions uploadResponse = new UploadPostDisscussions("Hamstech", getResources().getString(R.string.lblApiKey),
                uploadTitle,uploadConentent,userDataBase.getUserMobileNumber(1),courseId,imagePathData,langPref);
        Call<UploadPostDisscussions> call = apiService.getDiscussionUploadPost(uploadResponse);
        call.enqueue(new Callback<UploadPostDisscussions>() {
            @Override
            public void onResponse(Call<UploadPostDisscussions> call, retrofit2.Response<UploadPostDisscussions> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    imagePathData = "";picturePath = "";
                    uploadSuccessPopUp();
                }
            }

            @Override
            public void onFailure(Call<UploadPostDisscussions> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }
    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(MyCoursesPageActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.post_upload_successfull);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);

        Glide.with(MyCoursesPageActivity.this)
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
    public void startPayment(String razorpay_order_id,int InstallmentAmount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
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
            String vAmount = String.valueOf(InstallmentAmount);
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
            options.put("order_id", razorpay_order_id);
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
            co.open(MyCoursesPageActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(MyCoursesPageActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
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
            //rezorOrderID = paymentData.getOrderId();
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
                mobile, order_id, tracking_id);
        Call<PaymentSuccessResponse> call = apiService.getPaymentSuccess(paymentSuccessResponse);
        call.enqueue(new Callback<PaymentSuccessResponse>() {
            @Override
            public void onResponse(Call<PaymentSuccessResponse> call, retrofit2.Response<PaymentSuccessResponse> response) {
                if (response.body().getMesssage().equalsIgnoreCase("Payment updated successfully"))
                    //OnlineSuccessfulPopUp();
                    OnlineSuccessfulPopUp(MyCoursesPageActivity.this);
                else
                    Toast.makeText(MyCoursesPageActivity.this, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {

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

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname", UserDataConstants.userName);
            data.put("email", UserDataConstants.userMail);
            data.put("category","MyCourses page");
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
