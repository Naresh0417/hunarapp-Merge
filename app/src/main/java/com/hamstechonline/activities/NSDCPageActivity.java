package com.hamstechonline.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.hamstechonline.R;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.fragments.NavigationFragment;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NSDCPageActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    BottomNavigationView navigation;
    NavigationFragment navigationFragment;
    NavigationView navSideMenu;
    int mMenuId;
    LogEventsActivity logEventsActivity;
    UserDataBase userDataBase;
    HocLoadingDialog hocLoadingDialog;
    TextView txtDescription,txtAbout,txtViewCertificate;
    ImageView imgBanner;
    ArrayList<CategoryDatamodel> coursesList;
    CoursesListAdapter coursesListAdapter;
    RecyclerView listCourses;
    String ActivityLog,PagenameLog;
    String langPref = "Language",notiTitle="",language;
    SharedPreferences prefs;
    AppEventsLogger logger;
    Bundle params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.nsdcpage_activity);

        drawer = findViewById(R.id.drawer_layout);
        navigation = findViewById(R.id.navigation);
        navSideMenu = findViewById(R.id.navSideMenu);
        txtDescription = findViewById(R.id.txtDescription);
        imgBanner = findViewById(R.id.imgBanner);
        txtAbout = findViewById(R.id.txtAbout);
        listCourses = findViewById(R.id.listCourses);
        txtViewCertificate = findViewById(R.id.txtViewCertificate);

        userDataBase = new UserDataBase(this);
        logEventsActivity = new LogEventsActivity();
        hocLoadingDialog = new HocLoadingDialog(this);
        coursesList = new ArrayList<>();
        logger = AppEventsLogger.newLogger(this);
        params = new Bundle();
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.navigation_enrol).setChecked(true);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        navigationFragment = NavigationFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.navSideMenu, navigationFragment, "")
                .commit();


        txtViewCertificate.setVisibility(View.GONE);

        if (getIntent().getStringExtra("notificationId")!= null){
            notiTitle = UserDataConstants.notificationTitle;
            PagenameLog = "NSDC page";
            ActivityLog = "Notification Clicked";
            getLogEvent(this);
        } else {
            notiTitle = "";
            ActivityLog = "NSDC page";
            PagenameLog = "Clicked";
            getLogEvent(this);
        }

        if (langPref.equals("en")){
            language = "english";
            getNSDCdetails(this);
        } else {
            language = "hindi";
            getNSDCdetails(this);
        }

        txtViewCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(NSDCPageActivity.this);
                dialog.getWindow();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.setContentView(R.layout.dialogue_certificate);
                dialog.setCancelable(false);

                ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

                dialog.show();

                imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                Intent intentCourses = new Intent(NSDCPageActivity.this, HomePageActivity.class);
                startActivity(intentCourses);
                return true;
            case R.id.navigation_chat:
                ActivityLog = "";
                PagenameLog = "chat with whatsapp";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "chat with whatsapp");
                logger.logEvent(AppEventsConstants.EVENT_NAME_CONTACT,params);
                getLogEvent(NSDCPageActivity.this);
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
                Intent enrol = new Intent(NSDCPageActivity.this, SuccessStoryActivity.class);
                startActivity(enrol);
                return true;
            case R.id.navigation_today:
                PagenameLog = "Hunar Club";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                Intent hamstech = new Intent(NSDCPageActivity.this, BuzzActivity.class);
                startActivity(hamstech);
                return true;
            case R.id.navigation_aboutus:
                PagenameLog = "Contact Page";
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, PagenameLog);
                logger.logEvent(AppEventsConstants.EVENT_PARAM_SEARCH_STRING,params);
                new AppsFlyerEventsHelper(this).EventContactus();
                Intent about = new Intent(NSDCPageActivity.this, ContactActivity.class);
                startActivity(about);
                return true;
        }

        return false;
    }
    public void sideMenu(View view){
        drawer.openDrawer(Gravity.LEFT);
    }

    public void getNSDCdetails(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","category");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("lang",langPref);
            params.put("language",language);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.get_nsdc_categories_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            try {
                                txtDescription.setText(jsonObject.getJSONArray("Page").getJSONObject(0).getString("categoryname"));
                                Glide.with(context)
                                        .load(jsonObject.getJSONArray("Page").getJSONObject(0).getString("image_url"))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imgBanner);
                                txtAbout.setText(Html.fromHtml(jsonObject.getJSONArray("Page").getJSONObject(0).getString("category_description")));
                                if (jsonObject.isNull("courseslist")){

                                } else {
                                    JSONArray jsonArrayCourses = jsonObject.getJSONArray("courseslist");
                                    coursesList.clear();
                                    for (int i = 0; i<jsonArrayCourses.length(); i++) {
                                        JSONObject object = jsonArrayCourses.getJSONObject(i);

                                        CategoryDatamodel datamodel = new CategoryDatamodel();
                                        datamodel.setCategoryId(object.getString("courseId"));
                                        datamodel.setCategoryname(object.getString("course_title"));
                                        datamodel.setCat_image_url(object.getString("image_url"));
                                        datamodel.setCategory_description(object.getString("course_description"));
                                        datamodel.setCategory_language(language);
                                        datamodel.setCatVideoUrl(object.getString("video_url"));
                                        datamodel.setStatus(object.getString("status"));
                                        datamodel.setStatusNSDC(object.getString("nsdc_status"));
                                        if (object.getString("nsdc_status").equals("1")){
                                            coursesList.add(datamodel);
                                        }

                                    }

                                    coursesListAdapter = new CoursesListAdapter(NSDCPageActivity.this,coursesList);
                                    listCourses.setLayoutManager(new GridLayoutManager(NSDCPageActivity.this, 2));
                                    listCourses.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
                                    listCourses.setAdapter(coursesListAdapter);
                                    txtViewCertificate.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                            hocLoadingDialog.hideDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NSDCPageActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(NSDCPageActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(stringRequest);
    }

    public class CoursesListAdapter extends RecyclerView.Adapter<CoursesListAdapter.ViewHolder> {

        Context context;
        ArrayList<CategoryDatamodel> datamodels = new ArrayList<>();

        public CoursesListAdapter(Context context,ArrayList<CategoryDatamodel> datamodels){
            this.context=context;
            this.datamodels = datamodels;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.courses_list_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                Glide.with(context)
                        .load(datamodels.get(position).getCat_image_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgCategory);

                holder.txtTopTitle.setText(datamodels.get(position).getCategoryname());
                holder.txtBottomTitle.setText(datamodels.get(position).getCategory_description());
                holder.listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLogEvent(NSDCPageActivity.this);
                        Intent intent = new Intent(context, CoursePageActivity.class);
                        intent.putExtra("CategoryId",datamodels.get(position).getCategoryId());
                        intent.putExtra("CategoryName",datamodels.get(position).getCategoryname());
                        intent.putExtra("CourseName",datamodels.get(position).getCategoryname());
                        intent.putExtra("description",datamodels.get(position).getCategory_description());
                        intent.putExtra("language",datamodels.get(position).getCategory_language());
                        intent.putExtra("VideoUrl",datamodels.get(position).getCatVideoUrl());
                        intent.putExtra("statusNSDC",datamodels.get(position).getStatusNSDC());
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
            ImageView imgCategory;
            TextView txtTopTitle,txtBottomTitle;
            RelativeLayout listLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.imgCategory);
                txtTopTitle = view.findViewById(R.id.txtTopTitle);
                txtBottomTitle = view.findViewById(R.id.txtBottomTitle);
                listLayout = view.findViewById(R.id.listLayout);
            }
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
            data.put("category","");
            data.put("course","");
            data.put("lesson",notiTitle);
            data.put("activity", ActivityLog);
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
