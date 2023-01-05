package com.hamstechonline.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.hamstechonline.R;
import com.hamstechonline.adapters.OurAwardsAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.NonScrollExpandableListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    List groupNames;
    NonScrollExpandableListView listExpandable;
    private int lastExpandedPosition = -1;
    ExpandableListAdapter expandableListAdapter;
    int mMenuId;
    UserDataBase userDataBase;
    String aboutusImage,aboutContent,achieversImage1,achieversImage2,achieversImage3,imageOurMentor;
    NestedScrollView mainLayout;
    ArrayList<String> achieversImage = new ArrayList<>();
    ArrayList<String> awardsImage = new ArrayList<>();
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog;
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language",mp4URL="",lessonsLog = "",mobile = "",fullname = "",email = "",footerMenuStatus;
    SharedPreferences prefs,footerStatus;
    YouTubePlayer player;
    YouTubePlayerView youTubePlayerView;
    AppEventsLogger logger;
    Bundle params;
    ImageButton stickyWhatsApp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aboutus_activity);

        drawer = findViewById(R.id.drawer_layout);
        navSideMenu = findViewById(R.id.navSideMenu);
        listExpandable = findViewById(R.id.listExpandable);
        mainLayout = findViewById(R.id.mainLayout);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        groupNames = Arrays.asList(getResources().getStringArray(R.array.aboutGroupList));

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
        langPref = prefs.getString("Language", "");

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
        if (getIntent().getStringExtra("notificationId")!= null){
            PagenameLog = "About us page";
            lessonsLog = UserDataConstants.notificationTitle;
            ActivityLog = "Notification Clicked";
            getLogEvent(this);
        }

        getData(this);
        expandableListAdapter = new ExpandableListAdapter(AboutUsActivity.this);
        listExpandable.setAdapter(expandableListAdapter);

        listExpandable.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listExpandable.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        setListViewHeight(listExpandable, 3);
        listExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                setListViewHeight(parent, groupPosition);
                return false;
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

        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (footerMenuStatus.equalsIgnoreCase("paid")) {
            //footerNavigationPaid = FooterNavigationPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationPaid(), "About Us").commit();
        } else {
            //footerNavigationUnPaid = FooterNavigationUnPaid.newInstance();
            ft.replace(R.id.footer_menu, new FooterNavigationUnPaid(), "About Us")
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
    }

    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart() {
        drawer.closeDrawers();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10) {
            height = 200;
        }
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    public void getData(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        mainLayout.setVisibility(View.GONE);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","aboutus");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();
        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getAboutData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    awardsImage.clear();
                    achieversImage.clear();
                    if (jo.getString("status").equals("ok")){

                        JSONObject jsonObject = jo.getJSONObject("content");
                        aboutContent = jsonObject.getString("post_content");
                        aboutusImage = jsonObject.getString("image");
                        JSONObject objectOurMentor = jo.getJSONObject("our_mentor");
                        mp4URL = objectOurMentor.getString("video");
                        imageOurMentor = objectOurMentor.getString("image");
                        JSONObject objectOurAwards = jo.getJSONObject("our_awards");
                        awardsImage.add(objectOurAwards.getString("image1"));
                        awardsImage.add(objectOurAwards.getString("image2"));
                        awardsImage.add(objectOurAwards.getString("image3"));
                        awardsImage.add(objectOurAwards.getString("image4"));
                        JSONObject objectOurAchievers = jo.getJSONObject("our_achievers");
                        achieversImage1 = objectOurAchievers.getString("image1");
                        achieversImage2 = objectOurAchievers.getString("image2");
                        achieversImage3 = objectOurAchievers.getString("image3");

                    } else {
                        Toast.makeText(AboutUsActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                    listExpandable.expandGroup(1);
                    hocLoadingDialog.hideDialog();
                    mainLayout.setVisibility(View.VISIBLE);
                } catch(JSONException e) {
                    hocLoadingDialog.hideDialog();
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
                    Toast.makeText(AboutUsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context context;

        public ExpandableListAdapter(Context context){
            this.context = context;

        }

        @Override
        public int getGroupCount() {
            return groupNames.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
                TextView listTitle = convertView.findViewById(R.id.listTitle);
                listTitle.setText(groupNames.get(groupPosition).toString());
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (groupPosition == 0){
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.aboutus_content, null);

                ImageView imgAboutusImage = convertView.findViewById(R.id.imgAboutusImage);
                TextView txtAboutText = convertView.findViewById(R.id.txtAboutText);
                txtAboutText.setText(aboutContent);

                Glide.with(context)
                        .load(aboutusImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgAboutusImage);

            } else if (groupPosition == 1) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.ourmentor_activity, null);
                ImageView imgOurMentor = convertView.findViewById(R.id.imgOurMentor);
                Button btnEnrolNow = convertView.findViewById(R.id.btnEnrolNow);
                ImageView imgPlay = convertView.findViewById(R.id.imgPlay);
                Glide.with(context)
                        .asBitmap()
                        .load(imageOurMentor)
                        .into(imgOurMentor);
                btnEnrolNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityLog = "About us page";
                        PagenameLog = "Enroll Now";
                        getLogEvent(AboutUsActivity.this);
                        Intent intent = new Intent(AboutUsActivity.this, EnrolNowActivity.class);
                        startActivity(intent);
                    }
                });
                imgPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoPopUp();
                    }
                });
            } else if (groupPosition == 2) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.our_awards_layout, null);
                RecyclerView listItems = convertView.findViewById(R.id.listItems);

                OurAwardsAdapter ourAwardsAdapter = new OurAwardsAdapter(AboutUsActivity.this,awardsImage);
                listItems.setLayoutManager(new GridLayoutManager(AboutUsActivity.this, 1));
                listItems.addItemDecoration(new GridSpacingItemDecoration(1,10,false));
                listItems.setAdapter(ourAwardsAdapter);

            }else if (groupPosition == 3) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.our_achievers_content, null);

                ImageView imgAchievers1 = convertView.findViewById(R.id.imgAchievers1);
                ImageView imgAchievers2 = convertView.findViewById(R.id.imgAchievers2);
                ImageView imgAchievers3 = convertView.findViewById(R.id.imgAchievers3);

                Glide.with(context)
                        .asBitmap()
                        .load(achieversImage1)
                        .into(imgAchievers1);

                Glide.with(context)
                        .asBitmap()
                        .load(achieversImage2)
                        .into(imgAchievers2);

                Glide.with(context)
                        .asBitmap()
                        .load(achieversImage3)
                        .into(imgAchievers3);
            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public void videoPopUp(){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.mentorvideo_popup);
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                player.loadVideo(mp4URL,0);
            }
            @Override
            public void onStateChange(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "About us page";
                if (state.toString().equals("PLAYING")){
                    PagenameLog = "Video start";
                    getLogEvent(AboutUsActivity.this);
                } else if (state.toString().equals("PAUSED")){
                    PagenameLog = "Video paused";
                    getLogEvent(AboutUsActivity.this);
                } else if (state.toString().equals("STOPPED")){
                    PagenameLog = "Video stopped";
                    getLogEvent(AboutUsActivity.this);
                }
            }

        });

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
            data.put("email",email);
            data.put("category","");
            data.put("course","");
            data.put("lesson",lessonsLog);
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
        Intent intentCourses = new Intent(AboutUsActivity.this, HomePageActivity.class);
        startActivity(intentCourses);
        finish();
    }

}
