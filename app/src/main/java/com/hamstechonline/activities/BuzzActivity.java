package com.hamstechonline.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.backup.BackupAgent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.navigation.NavigationView;
import com.hamstechonline.R;
import com.hamstechonline.activities.dialogs.BuzzDetailsDialog;
import com.hamstechonline.activities.dialogs.ReportBlockDialoge;
import com.hamstechonline.activities.dialogs.ReportDialoge;
import com.hamstechonline.activities.dialogs.YourPostDialog;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.BuzzDataModel;
import com.hamstechonline.datamodel.CommentsCountData;
import com.hamstechonline.datamodel.HocOptionsData;
import com.hamstechonline.datamodel.HocResponse;
import com.hamstechonline.datamodel.HocTodayData;
import com.hamstechonline.datamodel.HocTodayResponse;
import com.hamstechonline.datamodel.LikesCountData;
import com.hamstechonline.datamodel.UploadPostResponse;
import com.hamstechonline.datamodel.mycources.UploadResponse;
import com.hamstechonline.fragments.BuzzDetailsFragment;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LikesInterface;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UploadAssignmentPopup;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class BuzzActivity extends AppCompatActivity implements LikesInterface,BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    BottomNavigationView navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    RecyclerView listItems, hocItemsList;
    FrameLayout fragment;
    BuzzAdapter buzzAdapter;
    YourPostAdapter yourPostAdapter;
    HocOptionsAdapter hocOptionsAdapter;
    ArrayList<BuzzDataModel> dataArrayList = new ArrayList<>();
    UserDataBase userDataBase;
    int mMenuId;
    String langPref,term_id = "",mobile,fullname,email = "";
    SharedPreferences prefs;
    HocLoadingDialog hocLoadingDialog;
    String PagenameLog,ActivityLog = "",postId = "",mp4URL = "",lessonEvent="",resultLikesCount,resultCommentCount;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    Bundle params;
    ApiInterface apiService;
    public Bundle bundle, dataBundle;
    BuzzDetailsDialog buzzDetailsDialog;
    ReportBlockDialoge reportDialoge;
    YourPostDialog yourPostDialog;
    TextView btnHunarPosts, btnYourPosts;
    ArrayList<Integer> courseIds = new ArrayList<>();
    ImageButton stickyWhatsApp;
    RelativeLayout submitPost;
    UploadAssignmentPopup uploadAssignmentPopup;
    private final int PERM_READ_WRITE_STORAGE = 101;
    private static final int FILE_SELECT_CODE = 0;
    Bitmap mBitmap, resized;
    String type_image, picturePath = "", imgString,uploadConentent,uploadFilePath = "",uploadTitle = "";
    private String imagePathData = "",intLike = "100",likevalueCount = "";
    boolean likeStatus = false;
    int listSelectedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.buzz_activity);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        listItems = findViewById(R.id.listItems);
        fragment = findViewById(R.id.fragment);
        btnHunarPosts = findViewById(R.id.btnHunarPosts);
        btnYourPosts = findViewById(R.id.btnYourPosts);
        hocItemsList = findViewById(R.id.hocItemsList);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        submitPost = findViewById(R.id.submitPost);

        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_today).setChecked(true);

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
        uploadAssignmentPopup = new UploadAssignmentPopup(this);
        bundle = new Bundle();
        dataBundle = new Bundle();

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        apiService = ApiClient.getClient().create(ApiInterface.class);

        uploadFilePath = getResources().getString(R.string.upload_image);

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

        btnHunarPosts.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
        btnHunarPosts.setTextColor(getResources().getColor(R.color.dark_pink));
        btnYourPosts.setBackgroundResource(0);
        btnYourPosts.setTextColor(getResources().getColor(R.color.muted_blue));
        getOptionsData();

        btnHunarPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHunarPosts.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
                btnHunarPosts.setTextColor(getResources().getColor(R.color.dark_pink));
                btnYourPosts.setBackgroundResource(0);
                btnYourPosts.setTextColor(getResources().getColor(R.color.muted_blue));
                lessonEvent = "";
                ActivityLog = "Click";
                PagenameLog = "Hunar Posts";
                getLogEvent(BuzzActivity.this);
                hocItemsList.setVisibility(View.VISIBLE);
                getOptionsData();
            }
        });

        btnYourPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lessonEvent = "";
                ActivityLog = "Click";
                PagenameLog = "My Post";
                getLogEvent(BuzzActivity.this);
                btnYourPosts.setBackground(getResources().getDrawable(R.drawable.shadow_pink_strok));
                btnYourPosts.setTextColor(getResources().getColor(R.color.dark_pink));
                btnHunarPosts.setBackgroundResource(0);
                btnHunarPosts.setTextColor(getResources().getColor(R.color.muted_blue));
                ActivityLog = "Course Details";
                PagenameLog = "Tab selected";
                hocItemsList.setVisibility(View.GONE);
                getUserPosts();
            }
        });

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                lessonEvent = "";
                ActivityLog = "Sticky whatsapp";
                PagenameLog = "Hunar Club";
                getLogEvent(BuzzActivity.this);
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

        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonEvent = "";
                ActivityLog = "Submit your post";
                PagenameLog = "Hunar Club";
                getLogEvent(BuzzActivity.this);
                OnlineSuccessfulPopUp();
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

        ViewCompat.setNestedScrollingEnabled(listItems, false);

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
                lessonEvent = "";
                ActivityLog = "Hunar Club";
                PagenameLog = "Home Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(BuzzActivity.this);
                Intent intentCourses = new Intent(BuzzActivity.this, HomePageActivity.class);
                startActivity(intentCourses);
                return true;
            case R.id.navigation_chat:
                lessonEvent = "";
                ActivityLog = "";
                PagenameLog = "chat with whatsapp";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "chat with whatsapp");
                logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
                getLogEvent(BuzzActivity.this);
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
                PagenameLog = "Success Story";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                Intent enrol = new Intent(BuzzActivity.this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                lessonEvent = "";
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                Intent hamstech = new Intent(BuzzActivity.this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                lessonEvent = "";
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                getLogEvent(BuzzActivity.this);
                new AppsFlyerEventsHelper(this).EventContactus();
                Intent about = new Intent(BuzzActivity.this, ContactActivity.class);
                startActivity(about);
                return true;
        }
        return false;
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    public void getHocTodayList() {
        hocLoadingDialog.showLoadingDialog();
        HocTodayResponse hocTodayResponse = new HocTodayResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                UserDataConstants.userMobile,langPref,"hamstechtoday",term_id);
        Call<HocTodayResponse> call = apiService.getHocTodayResponse(hocTodayResponse);
        call.enqueue(new Callback<HocTodayResponse>() {
            @Override
            public void onResponse(Call<HocTodayResponse> call, retrofit2.Response<HocTodayResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getHocTodayData() != null) {
                    buzzAdapter = new BuzzAdapter(BuzzActivity.this,response.body().getHocTodayData());
                    listItems.setLayoutManager(new LinearLayoutManager(BuzzActivity.this, RecyclerView.VERTICAL, false));
                    listItems.setAdapter(buzzAdapter);
                    listItems.scrollToPosition(listSelectedPosition);
                }
                if (getIntent().getStringExtra("notificationId")!= null){
                    PagenameLog = "Hunar Club";
                    postId = "";
                    ActivityLog = "Notification Clicked";
                    lessonEvent = UserDataConstants.notificationTitle;
                    getLogEvent(BuzzActivity.this);
                }
            }

            @Override
            public void onFailure(Call<HocTodayResponse> call, Throwable t) {

            }
        });
    }

    public void getUserPosts() {
        hocLoadingDialog.showLoadingDialog();
        HocTodayResponse hocTodayResponse = new HocTodayResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                UserDataConstants.userMobile,langPref,"userposts","");
        Call<HocTodayResponse> call = apiService.getHocTodayResponse(hocTodayResponse);
        call.enqueue(new Callback<HocTodayResponse>() {
            @Override
            public void onResponse(Call<HocTodayResponse> call, retrofit2.Response<HocTodayResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getHocTodayData() != null) {
                    yourPostAdapter = new YourPostAdapter(BuzzActivity.this,response.body().getHocTodayData());
                    listItems.setLayoutManager(new LinearLayoutManager(BuzzActivity.this, RecyclerView.VERTICAL, false));
                    listItems.setAdapter(yourPostAdapter);
                }
            }

            @Override
            public void onFailure(Call<HocTodayResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void setIsLiked(int isLiked, int selectedPosition) {
        if (isLiked == 1) {
            //buzzAdapter.notifyItemChanged(1);
            listSelectedPosition = selectedPosition;
            getOptionsData();
        }
    }

    public class BuzzAdapter extends RecyclerView.Adapter<BuzzAdapter.ViewHolder> {

        Context context;
        List<HocTodayData> dataBuzz;

        public BuzzAdapter(Context context,List<HocTodayData> dataBuzz){
            this.context = context;
            this.dataBuzz = dataBuzz;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.buzz_item_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.txtTitle.setText(dataBuzz.get(position).getTitle());
                holder.txtDescription.setText(dataBuzz.get(position).getDescription());
                if (!dataBuzz.get(position).getExternallink().isEmpty()) {
                    holder.txtExternalLink.setText(dataBuzz.get(position).getExternallink());
                    holder.txtExternalLink.setVisibility(View.VISIBLE);
                }
                if (!dataBuzz.get(position).getProfile_pic().isEmpty()) {
                    Glide.with(BuzzActivity.this)
                            .load(dataBuzz.get(position).getProfile_pic())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(holder.profile_image);
                    holder.profile_image.setVisibility(View.VISIBLE);
                    if (!dataBuzz.get(position).getName().isEmpty()) {
                        holder.txtUserName.setText(dataBuzz.get(position).getName());
                        holder.txtUserName.setVisibility(View.VISIBLE);
                    }
                } else if (!dataBuzz.get(position).getName().isEmpty()) {
                    holder.txtUserName.setText(dataBuzz.get(position).getName());
                    holder.profile_image.setVisibility(View.GONE);
                    holder.txtUserName.setVisibility(View.VISIBLE);
                } else if (!dataBuzz.get(position).getName_first_character().isEmpty()) {
                    holder.txtUserNameChar.setText(dataBuzz.get(position).getName_first_character());
                    holder.profile_image.setVisibility(View.GONE);
                    holder.txtUserNameChar.setVisibility(View.VISIBLE);
                }
                if (dataBuzz.get(position).getVideourl().equals("")){
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgHamstech);
                } else {
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.VISIBLE);
                    mp4URL = dataBuzz.get(position).getVideourl();
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgHamstech);
                }

                if (!intLike.equalsIgnoreCase("100")) {
                    if (intLike.equalsIgnoreCase("1")){
                        holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0,0,0);
                    }
                    holder.likesCount.setText(likevalueCount+" "+context.getResources().getString(R.string.like)+" "+
                            dataBuzz.get(position).getComments()+" "+context.getResources().getString(R.string.comment));
                } else {
                    if (dataBuzz.get(position).getLikedislike() == 1){
                        holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0,0,0);
                    }
                    holder.likesCount.setText(dataBuzz.get(position).getLikes()+" "+context.getResources().getString(R.string.like)+" "+
                            dataBuzz.get(position).getComments()+" "+context.getResources().getString(R.string.comment));
                }
                //likeStatus = false;
                intLike = "100";
                holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBuzz.get(position).getLikedislike() == 1){
                            /*lessonEvent = dataBuzz.get(position).getTitle();
                            ActivityLog = "UnLike";
                            PagenameLog = "Hunar Posts";
                            getLogEvent(context);
                            holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            getLikeData(context,dataBuzz.get(position).getPostid(),position);*/

                        } else {
                            lessonEvent = dataBuzz.get(position).getTitle();
                            ActivityLog = "Like";
                            PagenameLog = "Hunar Posts";
                            getLogEvent(context);
                            holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0,0,0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            //getLogEvent(context);
                            holder.likesCount.setText((Integer.parseInt(dataBuzz.get(position).getLikes()) + 1)+" Likes "+
                                    dataBuzz.get(position).getComments()+" Comments");
                            getLikeData(context,dataBuzz.get(position).getPostid(),position);
                        }

                    }
                });
                holder.imgHamstech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //bundle.putParcelable("buzzdetails",dataArrayList.get(position));
                        /*Intent intent =  new Intent(context,BuzzDetailsActivity.class);
                        intent.putExtra("buzzData",dataArrayList.get(position));
                        context.startActivity(intent);*/
                        //bundle.putParcelable("buzzData",dataBuzz.get(position));
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Post Click";
                        PagenameLog = "Hunar Posts";
                        getLogEvent(context);
                        buzzDetailsDialog = new BuzzDetailsDialog(BuzzActivity.this, position,dataBuzz,term_id);
                        buzzDetailsDialog.showLoadingDialog();
                    }
                });
                holder.txtComment.setOnClickListener(new View.OnClickListener() {
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
                        PagenameLog = "Hunar Posts";
                        getLogEvent(context);
                        buzzDetailsDialog = new BuzzDetailsDialog(BuzzActivity.this, position,dataBuzz,term_id);
                        buzzDetailsDialog.showLoadingDialog();
                    }
                });
                holder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Share";
                        PagenameLog = "Hunar Posts";
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
                holder.txtExternalLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dataBuzz.get(position).getExternallink().equalsIgnoreCase("")) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dataBuzz.get(position).getExternallink()));
                            startActivity(i);
                        }
                    }
                });
                holder.imgReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reportDialoge = new ReportBlockDialoge(BuzzActivity.this,dataBuzz.get(position).getPostid(),mobile);
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

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgHamstech, imgZoom,imgPlayButton, btnChat,imgReport;
            TextView txtTitle, txtDescription,likesCount,imgLikeUnlike, txtComment,txtExternalLink,
                    txtUserName,txtUserNameChar;
            LinearLayout btnShare;
            CircleImageView profile_image;

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

        public void getLikeData(final Context context,int postValue,int itemPosition){

            RequestQueue queue = Volley.newRequestQueue(context);
            hocLoadingDialog.showLoadingDialog();
            JSONObject params = new JSONObject();
            JSONObject metaData = new JSONObject();
            try {
                params.put("appname","Hamstech");
                params.put("page","buzz");
                params.put("apikey",context.getResources().getString(R.string.lblApiKey));
                params.put("postid",postValue);
                params.put("phone", UserDataConstants.userMobile);
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
                        listSelectedPosition = itemPosition;
                        //getHocTodayList();
                            //getHocTodayList();
                        //buzzAdapter.notifyItemRangeChanged(itemPosition,dataBuzz.size());
                        //buzzAdapter.notifyDataSetChanged();
                        getHocTodayList();
                        //buzzAdapter.notifyItemChanged(itemPosition);
                        //notifyDataSetChanged();
                        //(itemPosition);
                        //notifyItemChanged(1,dataBuzz);

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

    }


    public String getCommentsCount(int postid) {
        resultCommentCount = "";
        CommentsCountData commentsCountData = new CommentsCountData("Hamstech",getResources().getString(R.string.lblApiKey),postid,"buzz");
        Call<CommentsCountData> call = apiService.getCommentsCountData(commentsCountData);
        call.enqueue(new Callback<CommentsCountData>() {
            @Override
            public void onResponse(Call<CommentsCountData> call, retrofit2.Response<CommentsCountData> response) {
                try {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        resultCommentCount = response.body().getComments();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CommentsCountData> call, Throwable t) {

            }
        });
        return resultCommentCount;
    }

    public void getOptionsData() {
        HocResponse hocResponse = new HocResponse("Hamstech", getResources().getString(R.string.lblApiKey), langPref, "hamstechtoday");
        Call<HocResponse> call = apiService.getHocResponse(hocResponse);
        call.enqueue(new Callback<HocResponse>() {
            @Override
            public void onResponse(Call<HocResponse> call, retrofit2.Response<HocResponse> response) {
                if (response.body().getHocOptionsData() != null) {
                    hocOptionsAdapter = new HocOptionsAdapter(BuzzActivity.this,response.body().getHocOptionsData());
                    hocItemsList.setLayoutManager(new LinearLayoutManager(BuzzActivity.this, RecyclerView.HORIZONTAL, false));
                    hocItemsList.setAdapter(hocOptionsAdapter);
                    getHocTodayList();
                }
            }

            @Override
            public void onFailure(Call<HocResponse> call, Throwable t) {

            }
        });
    }

    public class HocOptionsAdapter extends RecyclerView.Adapter<HocOptionsAdapter.ViewHolder> {

        Context context;
        List<HocOptionsData> hocOptions;

        public HocOptionsAdapter(Context context, List<HocOptionsData> hocOptions) {
            this.context = context;
            this.hocOptions = hocOptions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.hoc_options_list, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.txtOption.setText(hocOptions.get(position).getName());

                holder.txtOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ActivityLog = hocOptions.get(position).getName();
                            PagenameLog = "Hunar Posts";
                            getLogEvent(context);
                            holder.txtOption.setTextColor(getResources().getColor(R.color.white));
                            holder.txtOption.setBackground(getDrawable(R.drawable.blue_button_bg));
                            holder.txtOption.setPadding(20, 0, 20, 0);
                            courseIds.add(hocOptions.get(position).getTermId());
                        } else {
                            holder.txtOption.setTextColor(getResources().getColor(R.color.muted_blue));
                            holder.txtOption.setBackground(getDrawable(R.drawable.border_bg));
                            holder.txtOption.setPadding(20, 0, 20, 0);
                            courseIds.remove(hocOptions.get(position).getTermId());
                        }
                        term_id = courseIds.toString().substring(1, (courseIds.toString().length())-1);
                        getHocTodayList();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return hocOptions.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox txtOption;

            public ViewHolder(@NonNull View view) {
                super(view);
                txtOption = view.findViewById(R.id.txtOption);
            }

        }
    }

    public class YourPostAdapter extends RecyclerView.Adapter<YourPostAdapter.ViewHolder> {

        Context context;
        List<HocTodayData> dataBuzz;

        public YourPostAdapter(Context context,List<HocTodayData> dataBuzz){
            this.context = context;
            this.dataBuzz = dataBuzz;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.buzz_item_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.txtTitle.setText(dataBuzz.get(position).getTitle());
                holder.txtDescription.setText(dataBuzz.get(position).getDescription());
                if (dataBuzz.get(position).getVideourl().equals("")){
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgHamstech);
                } else {
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.VISIBLE);
                    mp4URL = dataBuzz.get(position).getVideourl();
                    Glide.with(context)
                            .load(dataBuzz.get(position).getImage())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgHamstech);
                }
                if (dataBuzz.get(position).getLikedislike() == 1){
                    holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                } else {
                    holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0,0,0);
                }
                holder.likesCount.setText(dataBuzz.get(position).getLikes()+" Likes"+" "+
                        dataBuzz.get(position).getComments()+" Comments");
                holder.imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataBuzz.get(position).getLikedislike() == 1){
                            /*lessonEvent = dataBuzz.get(position).getTitle();
                            ActivityLog = "UnLike";
                            PagenameLog = "Your Posts";
                            getLogEvent(context);
                            holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            getLikeData(context,dataBuzz.get(position).getPostid());*/

                        } else {
                            lessonEvent = dataBuzz.get(position).getTitle();
                            ActivityLog = "Like";
                            PagenameLog = "My Post";
                            getLogEvent(context);
                            holder.imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0,0,0);
                            ActivityLog = ""+dataBuzz.get(position).getPostid();
                            //getLogEvent(context);
                            getLikeData(context,dataBuzz.get(position).getPostid(),position);
                        }
                    }
                });
                holder.imgHamstech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //bundle.putParcelable("buzzdetails",dataArrayList.get(position));
                        /*Intent intent =  new Intent(context,BuzzDetailsActivity.class);
                        intent.putExtra("buzzData",dataArrayList.get(position));
                        context.startActivity(intent);*/
                        //bundle.putParcelable("buzzData",dataBuzz.get(position));
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Post Click";
                        PagenameLog = "My Post";
                        getLogEvent(context);
                        yourPostDialog = new YourPostDialog(BuzzActivity.this, position,dataBuzz,term_id);
                        yourPostDialog.showLoadingDialog();
                    }
                });
                holder.txtComment.setOnClickListener(new View.OnClickListener() {
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
                        PagenameLog = "My Post";
                        getLogEvent(context);
                        yourPostDialog = new YourPostDialog(BuzzActivity.this, position,dataBuzz,term_id);
                        yourPostDialog.showLoadingDialog();
                    }
                });
                holder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Share";
                        PagenameLog = "My Post";
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
                holder.imgReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reportDialoge = new ReportBlockDialoge(BuzzActivity.this,dataBuzz.get(position).getPostid(),mobile);
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

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgHamstech, imgZoom,imgPlayButton, btnChat,imgReport;
            TextView txtTitle, txtDescription,likesCount,imgLikeUnlike, txtComment;
            LinearLayout btnShare;

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
                imgReport = view.findViewById(R.id.imgReport);
            }

        }

        public void getLikeData(final Context context,int postValue,int positionValue){

            RequestQueue queue = Volley.newRequestQueue(context);
            hocLoadingDialog.showLoadingDialog();
            JSONObject params = new JSONObject();
            JSONObject metaData = new JSONObject();
            try {
                params.put("appname","Hamstech");
                params.put("page","buzz");
                params.put("apikey",context.getResources().getString(R.string.lblApiKey));
                params.put("postid",postValue);
                params.put("phone", UserDataConstants.userMobile);
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
                        dataArrayList.clear();
                        hocLoadingDialog.hideDialog();
                        //notifyItemChanged(positionValue);
                        term_id = "";
                        listSelectedPosition = positionValue;
                        getUserPosts();

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

    }

    public void OnlineSuccessfulPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setContentView(R.layout.upload_assignment_popup);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        LinearLayout selectGallery = dialog.findViewById(R.id.selectGallery);
        TextView uploadFileName = dialog.findViewById(R.id.uploadFileName);
        EditText userInputTitle = dialog.findViewById(R.id.userInputTitle);
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
                    Toast.makeText(BuzzActivity.this,"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
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
                uploadTitle = userInputTitle.getText().toString().trim();
                if (picturePath.equalsIgnoreCase("") || uploadTitle.equalsIgnoreCase("") ||
                        uploadConentent.equalsIgnoreCase("")){
                    Toast.makeText(BuzzActivity.this, "Fields should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityLog = "Upload Post";
                    userInputContent.setText("");
                    userInputTitle.setText("");
                    hocLoadingDialog.showLoadingDialog();
                    uploadFile();
                    uploadConentent = ""; uploadFilePath = ""; uploadTitle = "";
                    dialog.dismiss();
                }
            }
        });
    }

    public void uploadFile() {
        UploadPostResponse uploadResponse = new UploadPostResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                uploadTitle,uploadConentent,userDataBase.getUserMobileNumber(1),"image",imagePathData,langPref);
        Call<UploadPostResponse> call = apiService.getUploadPost(uploadResponse);
        call.enqueue(new Callback<UploadPostResponse>() {
            @Override
            public void onResponse(Call<UploadPostResponse> call, retrofit2.Response<UploadPostResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    //Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Assignment_Submission_Liner), Toast.LENGTH_SHORT).show();
                    imagePathData = "";picturePath = "";
                    uploadSuccessPopUp();
                    /*uploadFileName.setText("Select Your files");
                    uploadSuccessPopUp();*/
                }
            }

            @Override
            public void onFailure(Call<UploadPostResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(i, FILE_SELECT_CODE);
        } catch (Exception e) {
            Toast.makeText(BuzzActivity.this, e.toString(), Toast.LENGTH_LONG).show();
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
                        mBitmap = MediaStore.Images.Media.getBitmap(BuzzActivity.this.getContentResolver(), selectedImage);

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
                        //uploadFileName.setText(file.getPath());
                    } catch(Exception e) {
                        System.out.println("File not found : " + e.getMessage() + e);
                    }

                    if (picturePath.isEmpty() || picturePath == null) {
                        Toast.makeText(BuzzActivity.this, "Unable to capture the image..Please try again", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(BuzzActivity.this,
                            "Image Loading Failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(BuzzActivity.this,
                        "User cancelled file upload", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(BuzzActivity.this,
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


    private  boolean checkPermissions() {
        int result;

        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(BuzzActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(BuzzActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERM_READ_WRITE_STORAGE);
            return false;
        }

        return true;
    }

    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(BuzzActivity.this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.post_upload_successfull);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);

        Glide.with(BuzzActivity.this)
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


    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course","");
            data.put("lesson",lessonEvent);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intentCourses = new Intent(BuzzActivity.this, HomePageActivity.class);
        startActivity(intentCourses);
        finish();
    }
}
