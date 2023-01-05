package com.hamstechonline.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hamstechonline.activities.AboutUsActivity;
import com.hamstechonline.activities.BuzzActivity;
import com.hamstechonline.activities.CentersActivity;
import com.hamstechonline.activities.ChooseLanguage;
import com.hamstechonline.activities.ChooseTopisActivity;
import com.hamstechonline.activities.EditProfileActivity;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.FaqsActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.LiveFashionWebview;
import com.hamstechonline.activities.NotificationsActivity;
import com.hamstechonline.activities.SuccessStoryActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.DynamicMenuData;
import com.hamstechonline.datamodel.GetDynamicData;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.HowtoUseAppDialogue;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class NavigationFragment extends Fragment {

    RecyclerView listItems;
    NavigationItemAdapter navigationItemAdapter;
    Button btnEnrolNow;
    CircleImageView profile_image;
    LinearLayout linProfile;
    TextView txtUserMobile,txtUserName,txtHowToUse;
    UserDataBase userDataBase;
    String gcm_id,mobile = "",fullname = "",email = "",langPref = "en";
    int version = 0;
    List<DynamicMenuData> names = new ArrayList<>();
    List<String> namesList;
    ArrayList<DynamicMenuData> menuDataList = new ArrayList<>();
    LogEventsActivity logEventsActivity;
    String activityLog;
    HowtoUseAppDialogue howtoUseAppDialogue;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    SharedPreferences prefs;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_layout,
                container, false);

        listItems = view.findViewById(R.id.listItems);
        profile_image = view.findViewById(R.id.profile_image);
        linProfile = view.findViewById(R.id.linProfile);
        txtUserMobile = view.findViewById(R.id.txtUserMobile);
        txtUserName = view.findViewById(R.id.txtUserName);
        btnEnrolNow = view.findViewById(R.id.btnEnrolNow);
        txtHowToUse = view.findViewById(R.id.txtHowToUse);

        userDataBase = new UserDataBase(getActivity());
        logEventsActivity = new LogEventsActivity();
        howtoUseAppDialogue = new HowtoUseAppDialogue(getActivity());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        namesList = Arrays.asList(getActivity().getResources().getStringArray(R.array.menu_list));
        /*menuDataList.clear();
        for (int i = 0; i<namesList.size(); i++) {
            DynamicMenuData dynamicMenuData = new DynamicMenuData();
            dynamicMenuData.setTitle(namesList.get(i));
            dynamicMenuData.
        }
        getMenuData(getActivity());*/

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        prefs = getActivity().getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        getMenuData(getActivity());
        sharedPrefsUtils = new SharedPrefsUtils(getActivity(), getString(R.string.app_name));

        if (userDataBase.getCount() != 0) {
            getProfile(getActivity());
            mobile = UserDataConstants.userMobile;
            fullname = UserDataConstants.userName;
            email = UserDataConstants.userMail;
        }

        btnEnrolNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityLog = "Enroll Now";
                getLogEvent(getActivity());
                new AppsFlyerEventsHelper(getActivity()).EventEnroll();
                sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, false);
                Intent intent = new Intent(getActivity(), EnrolNowActivity.class);
                startActivity(intent);
            }
        });
        txtHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howtoUseAppDialogue.showLoadingDialog();
                activityLog = "How to use app";
                getLogEvent(getActivity());
            }
        });

        return view;
    }

    public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.ViewHolder> {

        Context context;
        List<DynamicMenuData> names;

        public NavigationItemAdapter(Context context,List<DynamicMenuData> names) {
            this.context = context;
            this.names = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.navigation_list_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            try {
                holder.txtNavigation.setText(names.get(position).getTitle());
                Glide.with(context)
                        .load(names.get(position).getIcon())
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.imgNavigation);

                holder.txtNavigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (names.get(position).getTitle().equalsIgnoreCase("Profile")||
                                names.get(position).getTitle().equalsIgnoreCase("प्रोफइल")) {
                            activityLog = "Profile Activity";
                            getLogEvent(getActivity());
                            Intent intentProfile = new Intent(context, EditProfileActivity.class);
                            context.startActivity(intentProfile);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Notifications")
                                || names.get(position).getTitle().equalsIgnoreCase("प्रोफइल")) {
                            activityLog = "Notification Page";
                            getLogEvent(getActivity());
                            Intent intentNotification = new Intent(context, NotificationsActivity.class);
                            context.startActivity(intentNotification);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Courses")
                                || names.get(position).getTitle().equalsIgnoreCase("कोर्सेस")) {
                            activityLog = "Course Page";
                            getLogEvent(getActivity());
                            Intent intentCourses = new Intent(context, HomePageActivity.class);
                            context.startActivity(intentCourses);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Success Stories")||
                                names.get(position).getTitle().equalsIgnoreCase("सक्सेस स्टोरीज़")) {
                            activityLog = "Success Stories";
                            getLogEvent(getActivity());
                            Intent nsdcCourses = new Intent(context, SuccessStoryActivity.class);
                            context.startActivity(nsdcCourses);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Recommendations")||
                                names.get(position).getTitle().equalsIgnoreCase("प्रोफइल")) {
                            activityLog = "Topics Page";
                            getLogEvent(getActivity());
                            Intent recommendedCourses = new Intent(context, ChooseTopisActivity.class);
                            context.startActivity(recommendedCourses);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Hunar Club")||
                                names.get(position).getTitle().equalsIgnoreCase("हुनर क्लब")) {
                            activityLog = "Buzz page";
                            getLogEvent(getActivity());
                            Intent intentHToday = new Intent(context, BuzzActivity.class);
                            context.startActivity(intentHToday);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("About Us")||
                                names.get(position).getTitle().equalsIgnoreCase("हमारे बारे में जाने")) {
                            activityLog = "About Us";
                            getLogEvent(getActivity());
                            new AppsFlyerEventsHelper(context).EventAboutUs();
                            Intent intentAbout = new Intent(context, AboutUsActivity.class);
                            context.startActivity(intentAbout);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Our Centres")||
                                names.get(position).getTitle().equalsIgnoreCase("हमारे सेंटर्स")) {
                            activityLog = "Our Centers";
                            getLogEvent(getActivity());
                            Intent intentCenters = new Intent(context, CentersActivity.class);
                            context.startActivity(intentCenters);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("Choose Language")||
                                names.get(position).getTitle().equalsIgnoreCase("भाषा चुनें")) {
                            activityLog = "Language Selection";
                            getLogEvent(getActivity());
                            Intent intentLanguage = new Intent(context, ChooseLanguage.class);
                            context.startActivity(intentLanguage);
                        } else if (names.get(position).getTitle().equalsIgnoreCase("FAQ's")||
                                names.get(position).getTitle().equalsIgnoreCase("अक्सर पूछे जाने वाले प्रश्न")) {
                            activityLog = "FAQs";
                            getLogEvent(getActivity());
                            Intent intentFaqs = new Intent(context, FaqsActivity.class);
                            context.startActivity(intentFaqs);
                        } else {
                            if (names.get(position).getPage_type().equalsIgnoreCase("2")) {
                                activityLog = names.get(position).getTitle();
                                getLogEvent(getActivity());
                                Intent intent = new Intent(context, LiveFashionWebview.class);
                                intent.putExtra("URL",names.get(position).getUrl());
                                context.startActivity(intent);
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgNavigation;
            TextView txtNavigation;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgNavigation = view.findViewById(R.id.imgNavigation);
                txtNavigation = view.findViewById(R.id.txtNavigation);
            }
        }
    }

    public void getMenuData(Context context) {
        GetDynamicData getDynamicData = new GetDynamicData("Hamstech",context.getResources().getString(R.string.lblApiKey),langPref);
        Call<GetDynamicData> call = apiService.getDynamicData(getDynamicData);
        call.enqueue(new Callback<GetDynamicData>() {
            @Override
            public void onResponse(Call<GetDynamicData> call, retrofit2.Response<GetDynamicData> response) {
                if (response.body().getDynamicMenuData() != null) {
                    navigationItemAdapter = new NavigationItemAdapter(getActivity(),response.body().getDynamicMenuData());
                    listItems.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    listItems.setAdapter(navigationItemAdapter);
                }
            }

            @Override
            public void onFailure(Call<GetDynamicData> call, Throwable t) {

            }
        });

    }

    public void getProfile(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("phone",userDataBase.getUserMobileNumber(1));
            params.put("page","profile");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    JSONObject object = jo.getJSONObject("status");
                    JSONObject objData = jo.getJSONObject("data");
                    if (object.getInt("status")==200){
                        txtUserMobile.setText(objData.getString("phone"));
                        txtUserName.setText(objData.getString("prospectname"));
                        if (objData.getString("gcm_id").equals("")){
                            getInsertaApp(getActivity());
                        }
                        UserDataConstants.userMail = objData.getString("email");
                        UserDataConstants.userName = objData.getString("prospectname");
                        UserDataConstants.userMobile = objData.getString("phone");
                        UserDataConstants.userAddress = objData.getString("address");
                        UserDataConstants.userCity = objData.getString("city");
                        UserDataConstants.userPincode = objData.getString("pincode");
                        UserDataConstants.userState = objData.getString("state");
                        UserDataConstants.userCountryName = objData.getString("country");
                        UserDataConstants.prospectId = objData.getString("prospectid");
                        UserDataConstants.lang = objData.getString("lang");
                        sharedPrefsUtils.setUserProfilePic(ApiConstants.userProfilePic, objData.getString("profilepic"));
                        Glide.with(getActivity())

                                .load(objData.getString("profilepic"))
                                //.placeholder(R.drawable.duser1)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profile_image);
                    } else {
                        Toast.makeText(getActivity(), ""+jo.getString("messsage"), Toast.LENGTH_SHORT).show();
                    }

                } catch(JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ex){
                    ex.printStackTrace();
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
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public void getInsertaApp(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();

        try {
            params.put("phone",userDataBase.getUserMobileNumber(1));
            params.put("gcmid",gcm_id);
            params.put("version",version);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getGcmid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

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
                    Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
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
            data.put("lesson","");
            data.put("activity",activityLog);
            data.put("pagename","Hamberg menu");
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
