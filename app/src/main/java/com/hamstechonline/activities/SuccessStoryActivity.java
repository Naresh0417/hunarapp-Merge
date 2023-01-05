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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class SuccessStoryActivity extends AppCompatActivity {

    DrawerLayout drawer;
    RelativeLayout navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    RecyclerView listItems;
    HamstechTodayAdapter hamstechTodayAdapter;
    ArrayList<LessonsDataModel> dataArrayList = new ArrayList<>();
    TextView headerTitle;
    int mMenuId, listSelectedPosition = 0;
    UserDataBase userDataBase;
    HocLoadingDialog hocLoadingDialog;
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog;
    Dialog dialog;
    TextView txtTitle, txtDescription;
    ImageView imageView;
    YouTubePlayer player;
    YouTubePlayerView youTubePlayerView;

    private String mp4URL = "",notiTitle = "",mobile = "",fullname = "",email = "",footerMenuStatus;
    String langPref = "Language";
    SharedPreferences prefs,footerStatus;
    private Locale myLocale;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hamtesh_today);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        listItems = findViewById(R.id.listItems);
        headerTitle = findViewById(R.id.headerTitle);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        hocLoadingDialog = new HocLoadingDialog(this);
        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        headerTitle.setText(getResources().getString(R.string.successStories));

        hocLoadingDialog = new HocLoadingDialog(this);
        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        changeLang(langPref);

        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getHamstechTodayList(this);

        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = "";
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        if (getIntent().getStringExtra("notificationId")!= null){
            PagenameLog = "Success stories";
            notiTitle = UserDataConstants.notificationTitle;
            ActivityLog = "Notification Clicked";
            getLogEvent(this);
        }

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "Success stories").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "Success stories")
                    .commit();
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHamstech, imgZoom, btnShare,imgPlayButton, btnChat;
        TextView txtTitle, txtDescription,txtExternalLink;
        ImageView imgLike;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgHamstech = view.findViewById(R.id.imgHamstech);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
            imgLike = view.findViewById(R.id.imgLike);
            imgZoom = view.findViewById(R.id.imgZoom);
            btnChat = view.findViewById(R.id.btnChat);
            txtExternalLink = view.findViewById(R.id.txtExternalLink);
            imgPlayButton = view.findViewById(R.id.imgPlayButton);
            btnShare = view.findViewById(R.id.btnShare);
        }

    }

    public class HamstechTodayAdapter extends RecyclerView.Adapter<ViewHolder> {

        Context context;
        ArrayList<LessonsDataModel> dataArrayList = new ArrayList<>();

        public HamstechTodayAdapter(Context context,ArrayList<LessonsDataModel> dataArrayList){
            this.context=context;
            this.dataArrayList = dataArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.hamstech_today_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                holder.txtTitle.setText(dataArrayList.get(position).getLesson_title());
                holder.txtDescription.setText(dataArrayList.get(position).getLesson_description());
                ActivityLog = dataArrayList.get(position).getLessonId();
                if (dataArrayList.get(position).getLesson_video_url().equals("")){
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.GONE);
                    Glide.with(context)

                            .load(dataArrayList.get(position).getLesson_image_url())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.imgHamstech);
                    if (dataArrayList.get(position).getExternalLink().equals("")){
                        holder.txtExternalLink.setVisibility(View.GONE);
                    } else {
                        holder.txtExternalLink.setText(dataArrayList.get(position).getExternalLink());
                        holder.txtExternalLink.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.imgHamstech.setVisibility(View.VISIBLE);
                    holder.imgPlayButton.setVisibility(View.VISIBLE);
                    mp4URL = dataArrayList.get(position).getLesson_video_url();
                    Glide.with(context)

                            .load(dataArrayList.get(position).getLesson_image_url())
                            //.placeholder(R.drawable.duser1)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            //.diskCacheStrategy(DiskCacheStrategy.NONE)
                            //.skipMemoryCache(true)
                            .into(holder.imgHamstech);
                    if (dataArrayList.get(position).getExternalLink().equals("")){
                        holder.txtExternalLink.setVisibility(View.GONE);
                    } else {
                        holder.txtExternalLink.setText(dataArrayList.get(position).getExternalLink());
                        holder.txtExternalLink.setVisibility(View.VISIBLE);
                    }
                    //mp4URL = "https://8d421a47ae9942a7ad8becf358b83882.azureedge.net/92717c7f-2aca-403f-af3b-079e426da6ca/Trial Course Fashion Styling Wed_960x540_1500.mp4";
                }
                holder.imgHamstech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewPop(position);
                    }
                });

                if (dataArrayList.get(position).getLikedislike() == 1){
                    holder.imgLike.setImageResource(R.drawable.ic_like);
                } else {
                    holder.imgLike.setImageResource(R.drawable.ic_unlike);
                }
                holder.imgLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataArrayList.get(position).getLikedislike() == 1){
                            PagenameLog = "Success Story unlike";
                            notiTitle = "";
                            holder.imgLike.setImageResource(R.drawable.ic_unlike);
                            ActivityLog = dataArrayList.get(position).getLessonId();
                            getLogEvent(context);
                            getHamstechToday(context,Integer.parseInt(dataArrayList.get(position).getLessonId()),position);

                        } else {
                            notiTitle = "";
                            PagenameLog = "Success Story like";
                            holder.imgLike.setImageResource(R.drawable.ic_like);
                            ActivityLog = dataArrayList.get(position).getLessonId();
                            getLogEvent(context);
                            getHamstechToday(context,Integer.parseInt(dataArrayList.get(position).getLessonId()),position);
                        }
                    }
                });
                holder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityLog = dataArrayList.get(position).getLessonId();
                        notiTitle = "";
                        if (dataArrayList.get(position).getLesson_video_url().equals("")) {
                            PagenameLog = "Image Share";
                            getLogEvent(context);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "Text");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, dataArrayList.get(position).getLesson_image_url());
                            startActivity(Intent.createChooser(shareIntent, dataArrayList.get(position).getLesson_title()));
                        }else {
                            PagenameLog = "Url Share";
                            String url = "https://www.youtube.com/watch?v="+dataArrayList.get(position).getLesson_video_url();
                            getLogEvent(context);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                            startActivity(Intent.createChooser(shareIntent, dataArrayList.get(position).getLesson_title()));
                        }
                    }
                });
                holder.btnChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notiTitle = "";
                        ActivityLog = "";
                        PagenameLog = "chat with whatsapp";
                        getLogEvent(SuccessStoryActivity.this);
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
                    }
                });
                holder.txtExternalLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataArrayList.get(position).getExternalLink()));
                        startActivity(browserIntent);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dataArrayList.size();
        }
        public void imageViewPop(final int position){

            dialog  = new Dialog(context);
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

            txtTitle.setText(dataArrayList.get(position).getLesson_title());
            txtDescription.setText(dataArrayList.get(position).getLesson_description());
            ActivityLog = dataArrayList.get(position).getLesson_title();
            notiTitle = dataArrayList.get(position).getLessonId();
            PagenameLog = "Success Story Today";
            getLogEvent(context);

            if (dataArrayList.get(position).getLesson_video_url().equals("")){
                Glide.with(context)

                        .load(dataArrayList.get(position).getLesson_image_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
                youTubePlayerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                mp4URL = dataArrayList.get(position).getLesson_video_url();
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
                            getLogEvent(context);
                        } else if (state.toString().equals("PAUSED")){
                            PagenameLog = "Video paused";
                            getLogEvent(context);
                        } else if (state.toString().equals("STOPPED")){
                            PagenameLog = "Video stopped";
                            getLogEvent(context);
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

        public void getHamstechToday(final Context context,int postValue,int listPosition){

            RequestQueue queue = Volley.newRequestQueue(context);
            hocLoadingDialog.showLoadingDialog();
            JSONObject params = new JSONObject();
            JSONObject metaData = new JSONObject();
            try {
                params.put("appname","Hamstech");
                params.put("page","hamstechtoday");
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
                        listSelectedPosition = listPosition;
                        getHamstechTodayList(SuccessStoryActivity.this);

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

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getHamstechTodayList(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        hocLoadingDialog.showLoadingDialog();
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","successstories");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("phone",UserDataConstants.userMobile);
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.get_success_stories, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    dataArrayList.clear();
                    if (jo.isNull("list")){
                        Toast.makeText(SuccessStoryActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();

                    } else {
                        JSONArray jsonArray = jo.getJSONArray("list");

                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            LessonsDataModel dataModel = new LessonsDataModel();

                            dataModel.setLessonId(object.getString("postid"));
                            dataModel.setLesson_name(object.getString("category"));
                            dataModel.setLesson_title(object.getString("title"));
                            dataModel.setLesson_description(object.getString("description"));
                            dataModel.setLesson_image_url(object.getString("image"));
                            dataModel.setLesson_video_url(object.getString("videourl"));
                            dataModel.setLikedislike(object.getInt("likedislike"));
                            dataModel.setExternalLink(object.getString("externallink"));

                            dataArrayList.add(dataModel);
                        }
                        hamstechTodayAdapter = new HamstechTodayAdapter(SuccessStoryActivity.this,dataArrayList);
                        listItems.setLayoutManager(new LinearLayoutManager(SuccessStoryActivity.this, RecyclerView.VERTICAL, false));
                        listItems.setAdapter(hamstechTodayAdapter);
                        listItems.scrollToPosition(listSelectedPosition);
                    }
                    hocLoadingDialog.hideDialog();
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
                    Toast.makeText(SuccessStoryActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(6000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
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
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",email);
            data.put("category","");
            data.put("course","");
            data.put("lesson",notiTitle);
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
