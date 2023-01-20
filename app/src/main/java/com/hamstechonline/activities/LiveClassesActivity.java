package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.LiveClass;
import com.hamstechonline.datamodel.LiveClassRegistrationResponse;
import com.hamstechonline.datamodel.LiveClassesResponse;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.homepage.EnglishCategory;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LiveClassesActivity extends AppCompatActivity {

    UserDataBase userDataBase;
    ImageButton stickyWhatsApp;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog,footerMenuStatus,
            langPref = "Language",mp4URL = "";
    LogEventsActivity logEventsActivity;
    RecyclerView listYourLiveClassesList,listAllLiveClassesList;
    YourLiveClassListAdapter yourLiveClassListAdapter;
    TextView txtYourClassesType,txtAllClassesType;
    AllLiveClassListAdapter allLiveClassListAdapter;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    SharedPreferences prefs,footerStatus;
    DrawerLayout drawer;
    ApiInterface apiService;
    Dialog dialog;
    TextView txtTitle, txtDescription;
    ImageView imageView;
    YouTubePlayer player;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_live_classes);

        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);
        listYourLiveClassesList = findViewById(R.id.listYourLiveClassesList);
        listAllLiveClassesList = findViewById(R.id.listAllLiveClassesList);
        txtYourClassesType = findViewById(R.id.txtYourClassesType);
        txtAllClassesType = findViewById(R.id.txtAllClassesType);
        navSideMenu = findViewById(R.id.navSideMenu);
        drawer = findViewById(R.id.drawer_layout);

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();

        apiService = ApiClient.getClient().create(ApiInterface.class);

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        getYourLiveClassesList("upcoming");
        getAllLiveClassesList("upcoming");

        txtYourClassesType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtYourClassesType.getText().toString().equalsIgnoreCase("Upcoming Classes")) {
                    txtYourClassesType.setBackground(getResources().getDrawable(R.drawable.btn_blue_stroke_white));
                    txtYourClassesType.setTextColor(getResources().getColor(R.color.muted_blue));
                    txtYourClassesType.setText(getResources().getString(R.string.previous_classes));
                    getYourLiveClassesList("upcoming");

                } else {
                    txtYourClassesType.setText(R.string.upcoming_classes);
                    txtYourClassesType.setBackground(getResources().getDrawable(R.drawable.pink_stroke_white));
                    txtYourClassesType.setTextColor(getResources().getColor(R.color.pink));
                    getYourLiveClassesList("previous");
                }
            }
        });

        txtAllClassesType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAllClassesType.getText().toString().equalsIgnoreCase("Upcoming Classes")) {
                    txtAllClassesType.setBackground(getResources().getDrawable(R.drawable.btn_blue_stroke_white));
                    txtAllClassesType.setTextColor(getResources().getColor(R.color.muted_blue));
                    txtAllClassesType.setText("Previous Classes");
                    getAllLiveClassesList("upcoming");

                } else {
                    txtAllClassesType.setText(R.string.upcoming_classes);
                    txtAllClassesType.setBackground(getResources().getDrawable(R.drawable.pink_stroke_white));
                    txtAllClassesType.setTextColor(getResources().getColor(R.color.pink));
                    getAllLiveClassesList("previous");
                }
            }
        });

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Live Class").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Live Class")
                    .commit();
        }

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

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void getYourLiveClassesList(String type){

        LiveClassesResponse liveClassesResponse = new LiveClassesResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                langPref,userDataBase.getUserMobileNumber(1), type);
        Call<LiveClassesResponse> call = apiService.getYourLiveClasses(liveClassesResponse);
        call.enqueue(new Callback<LiveClassesResponse>() {
            @Override
            public void onResponse(Call<LiveClassesResponse> call, retrofit2.Response<LiveClassesResponse> response) {
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    yourLiveClassListAdapter = new YourLiveClassListAdapter(LiveClassesActivity.this,
                            txtYourClassesType.getText().toString(),response.body().getLiveClasses());
                    listYourLiveClassesList.setLayoutManager(new LinearLayoutManager(LiveClassesActivity.this, RecyclerView.HORIZONTAL, false));
                    listYourLiveClassesList.setAdapter(yourLiveClassListAdapter);
                } else
                    Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LiveClassesResponse> call, Throwable t) {
                Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        /*yourLiveClassListAdapter = new YourLiveClassListAdapter(LiveClassesActivity.this,txtYourClassesType.getText().toString());
        listYourLiveClassesList.setLayoutManager(new LinearLayoutManager(LiveClassesActivity.this, RecyclerView.HORIZONTAL, false));
        listYourLiveClassesList.setAdapter(yourLiveClassListAdapter);*/
    }

    public void getAllLiveClassesList(String type){

        LiveClassesResponse liveClassesResponse = new LiveClassesResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                langPref,userDataBase.getUserMobileNumber(1), type);
        Call<LiveClassesResponse> call = apiService.getYourAllLiveClasses(liveClassesResponse);
        call.enqueue(new Callback<LiveClassesResponse>() {
            @Override
            public void onResponse(Call<LiveClassesResponse> call, retrofit2.Response<LiveClassesResponse> response) {
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    allLiveClassListAdapter = new AllLiveClassListAdapter(LiveClassesActivity.this,
                            txtAllClassesType.getText().toString(),response.body().getLiveClasses());
                    listAllLiveClassesList.setLayoutManager(new LinearLayoutManager(LiveClassesActivity.this, RecyclerView.HORIZONTAL, false));
                    listAllLiveClassesList.setAdapter(allLiveClassListAdapter);
                } else
                    Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LiveClassesResponse> call, Throwable t) {
                Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        /*allLiveClassListAdapter = new AllLiveClassListAdapter(LiveClassesActivity.this,txtAllClassesType.getText().toString());
        listAllLiveClassesList.setLayoutManager(new LinearLayoutManager(LiveClassesActivity.this, RecyclerView.HORIZONTAL, false));
        listAllLiveClassesList.setAdapter(allLiveClassListAdapter);*/
    }

    public class YourLiveClassListAdapter extends RecyclerView.Adapter<YourLiveClassListAdapter.ViewHolder> {
        Context context;
        List<LiveClass> datamodels;
        String yourLiveClassType;

        public YourLiveClassListAdapter(Context context,String yourLiveClassType,List<LiveClass> datamodels){
            this.context=context;
            this.datamodels = datamodels;
            this.yourLiveClassType = yourLiveClassType;
        }

        @NonNull
        @Override
        public YourLiveClassListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.your_live_classes_adapter, parent, false);
            YourLiveClassListAdapter.ViewHolder vh = new YourLiveClassListAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final YourLiveClassListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {

                Glide.with(context)
                        .load(datamodels.get(position).getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.courseImage);
                holder.txtCourseName.setText(datamodels.get(position).getTitle());
                holder.txtDaysLeft.setText(datamodels.get(position).getTimeLeft());
                holder.txtClassDate.setText(datamodels.get(position).getDate());

                if (datamodels.get(position).getTimeLeft().equalsIgnoreCase("")){
                    holder.txtDaysLeft.setVisibility(View.GONE);
                } else holder.txtDaysLeft.setVisibility(View.VISIBLE);

                if (yourLiveClassType.equalsIgnoreCase("Upcoming Classes")) {
                    holder.txtDaysLeft.setVisibility(View.GONE);
                    holder.txtWatchNow.setText(R.string.watch_now);
                } else if (yourLiveClassType.equalsIgnoreCase("Previous Classes")){
                    holder.txtDaysLeft.setVisibility(View.VISIBLE);
                    holder.txtWatchNow.setText(R.string.register_now);
                }

                holder.txtWatchNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.txtWatchNow.getText().toString().equalsIgnoreCase("REGISTER NOW")){
                            getLiveClassRegistration(datamodels.get(position),1);
                        } else if (holder.txtWatchNow.getText().toString().equalsIgnoreCase("WATCH NOW")){
                            imageViewPop(datamodels.get(position));
                        }
                    }
                });
                holder.listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


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
            TextView txtCourseName,txtDaysLeft,txtWatchNow,txtClassDate;
            RelativeLayout listLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                courseImage = view.findViewById(R.id.courseImage);
                txtCourseName = view.findViewById(R.id.txtCourseName);
                txtDaysLeft = view.findViewById(R.id.txtDaysLeft);
                txtWatchNow = view.findViewById(R.id.txtWatchNow);
                listLayout = view.findViewById(R.id.listLayout);
                txtClassDate = view.findViewById(R.id.txtClassDate);
            }
        }
    }

    public class AllLiveClassListAdapter extends RecyclerView.Adapter<AllLiveClassListAdapter.ViewHolder> {
        Context context;
        List<LiveClass> datamodels;
        String allLiveClassType;

        public AllLiveClassListAdapter(Context context,String allLiveClassType,List<LiveClass> datamodels){
            this.context=context;
            this.datamodels = datamodels;
            this.allLiveClassType = allLiveClassType;
        }

        @NonNull
        @Override
        public AllLiveClassListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.your_live_classes_adapter, parent, false);
            AllLiveClassListAdapter.ViewHolder vh = new AllLiveClassListAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                Glide.with(context)
                        .load(datamodels.get(position).getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.courseImage);

                holder.imgLock.setVisibility(View.VISIBLE);
                holder.txtCourseName.setText(datamodels.get(position).getTitle());
                holder.txtDaysLeft.setText(datamodels.get(position).getTimeLeft());
                holder.txtClassDate.setText(datamodels.get(position).getDate());

                if (datamodels.get(position).getTimeLeft().equalsIgnoreCase("")){
                    holder.txtDaysLeft.setVisibility(View.GONE);
                } else holder.txtDaysLeft.setVisibility(View.VISIBLE);

                if (allLiveClassType.equalsIgnoreCase("Upcoming Classes")) {
                    holder.txtDaysLeft.setVisibility(View.GONE);
                    holder.txtWatchNow.setText(R.string.watch_now);
                } else if (allLiveClassType.equalsIgnoreCase("Previous Classes")){
                    holder.txtDaysLeft.setVisibility(View.VISIBLE);
                    holder.txtWatchNow.setText(getResources().getString(R.string.register_now));
                }

                holder.listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.txtWatchNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.txtWatchNow.getText().toString().equalsIgnoreCase("REGISTER NOW")) {
                            getLiveClassRegistration(datamodels.get(position),2);
                        } else if (holder.txtWatchNow.getText().toString().equalsIgnoreCase("WATCH NOW")){
                            imageViewPop(datamodels.get(position));
                        }

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
            ImageView courseImage,imgLock;
            TextView txtCourseName,txtDaysLeft,txtWatchNow,txtClassDate;
            RelativeLayout listLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgLock = view.findViewById(R.id.imgLock);
                courseImage = view.findViewById(R.id.courseImage);
                txtCourseName = view.findViewById(R.id.txtCourseName);
                txtDaysLeft = view.findViewById(R.id.txtDaysLeft);
                txtWatchNow = view.findViewById(R.id.txtWatchNow);
                listLayout = view.findViewById(R.id.listLayout);
                txtClassDate = view.findViewById(R.id.txtClassDate);
            }
        }
    }

    public void getLiveClassRegistration(LiveClass liveClass, int classType){

        LiveClassRegistrationResponse liveClassRegistrationResponse = new LiveClassRegistrationResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                langPref,userDataBase.getUserMobileNumber(1), liveClass.getLiveClassId());
        Call<LiveClassRegistrationResponse> call = apiService.getLiveClassRegistration(liveClassRegistrationResponse);
        call.enqueue(new Callback<LiveClassRegistrationResponse>() {
            @Override
            public void onResponse(Call<LiveClassRegistrationResponse> call, retrofit2.Response<LiveClassRegistrationResponse> response) {
                if (response.body().getStatus().equalsIgnoreCase("success")) {
                    if (classType == 1) {
                        OnlineSuccessfulPopUp(LiveClassesActivity.this);
                    } else {
                        liveClassLockPopUp(LiveClassesActivity.this);
                    }

                } else
                    Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<LiveClassRegistrationResponse> call, Throwable t) {
                Toast.makeText(LiveClassesActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        /*allLiveClassListAdapter = new AllLiveClassListAdapter(LiveClassesActivity.this,txtAllClassesType.getText().toString());
        listAllLiveClassesList.setLayoutManager(new LinearLayoutManager(LiveClassesActivity.this, RecyclerView.HORIZONTAL, false));
        listAllLiveClassesList.setAdapter(allLiveClassListAdapter);*/
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
                .load(R.drawable.ic_sucess_assignment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_assignment)
                .into(progressBar);
        paymentComment.setText("You have successfully registered for the Live Faculty Session!");

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

    public void liveClassLockPopUp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.register_live);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

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

    public void imageViewPop(LiveClass liveClass){

        dialog  = new Dialog(LiveClassesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.hoc_video_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        imageView = dialog.findViewById(R.id.imageView);
        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtDescription = dialog.findViewById(R.id.txtDescription);
        logEventsActivity = new LogEventsActivity();
        youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);

        txtTitle.setText(liveClass.getTitle());
        txtDescription.setVisibility(View.GONE);
        ActivityLog = liveClass.getTitle();
        //notiTitle = dataArrayList.get(position).getLessonId();
        PagenameLog = "Success Story Today";
        getLogEvent(LiveClassesActivity.this);

        if (liveClass.getVideoUrl().equals("")){
            Glide.with(LiveClassesActivity.this)

                    .load(liveClass.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
            youTubePlayerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            mp4URL = liveClass.getVideoUrl();
            youTubePlayerView.setVisibility(View.VISIBLE);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    player = youTubePlayer;
                    player.loadVideo(mp4URL,0);
                }
                @Override
                public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                    super.onStateChange(youTubePlayer, state);
                    ActivityLog = "Story of success";
                    if (state.toString().equals("PLAYING")){
                        PagenameLog = "Video start";
                        getLogEvent(LiveClassesActivity.this);
                    } else if (state.toString().equals("PAUSED")){
                        PagenameLog = "Video paused";
                        getLogEvent(LiveClassesActivity.this);
                    } else if (state.toString().equals("STOPPED")){
                        PagenameLog = "Video stopped";
                        getLogEvent(LiveClassesActivity.this);
                    }
                }

            });
        }
        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
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
