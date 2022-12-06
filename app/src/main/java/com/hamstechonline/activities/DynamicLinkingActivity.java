package com.hamstechonline.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.hamstechonline.R;
import com.hamstechonline.adapters.FacultySliderAdapter;
import com.hamstechonline.adapters.MyCourseOverviewAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CourseDetailsResponse;
import com.hamstechonline.datamodel.CourseType;
import com.hamstechonline.datamodel.mycources.MyCoursesResponse;
import com.hamstechonline.fragments.MiniCourcesTab;
import com.hamstechonline.fragments.TopicsFragment;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class DynamicLinkingActivity extends AppCompatActivity {

    String courseId, language,lessonId = "";
    String langPref = "Language",notiTitle="",splitLesson = "",deepLink;
    SharedPreferences prefs;
    private Locale myLocale;
    ApiInterface apiService;
    HocLoadingDialog hocLoadingDialog;
    UserDataBase userDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.loading_progress);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        hocLoadingDialog = new HocLoadingDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (langPref.equals("en"))
            language = "english";
         else language = "hindi";
        changeLang(langPref);
        /*MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);*/
        deepLink = getIntent().getStringExtra("deepLink");
        Log.e("data","125   ");



        if (deepLink != null) {
            Log.e("data","125   "+deepLink.toString());
            String split = deepLink.toString().split("utm_content")[1];
            splitLesson = deepLink.toString().split("utm_campaign")[1];
            lessonId = "";
            Log.e("data","125   "+splitLesson.split("\\%")[1].substring(2).toString());
            if (splitLesson.split("\\%")[1].substring(2).equals("lesson")){
                UserDataConstants.lessonId = deepLink.toString().split("utm_term")[1].split("\\%")[1].substring(2);
                UserDataConstants.courseId = split.toString().split("\\%")[1].substring(2);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("home")) {
                lessonId = "Home";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("aboutus")) {
                lessonId = "aboutus";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("enrol")) {
                lessonId = "enrol";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("successstories")) {
                lessonId = "successstories";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("profile")) {
                lessonId = "profile";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("courses_page")) {
                lessonId = "courses_page";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("hunarclub")) {
                lessonId = "hunarclub";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("contactus")) {
                lessonId = "contactus";
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("my-courses")) {
                lessonId = "my-courses";
                UserDataConstants.courseId = ""+split.toString().split("\\%")[1].substring(2, split.toString().split("\\%")[1].length() - 0);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("courses")) {
                lessonId = "my-courses";
                UserDataConstants.courseId = ""+split.toString().split("\\%")[1].substring(2, split.toString().split("\\%")[1].length() - 0);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("regular-courses")) {
                lessonId = "regular-courses";
                UserDataConstants.courseId = ""+split.toString().split("\\%")[1].substring(2, split.toString().split("\\%")[1].length() - 0);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("paid_course")) {
                lessonId = "paid_course";
                UserDataConstants.courseId = ""+split.toString().split("\\%")[1].substring(2, split.toString().split("\\%")[1].length() - 0);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("whatsappchat")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.chatURL)));
                startActivity(intent);
                finish();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("courseclarificationcall")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+916281109108"));
                startActivity(intent);
                finish();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("mini_lessons_course_page")) {
                lessonId = "mini_lesson";
                UserDataConstants.courseId = split.toString().split("\\%")[1].substring(2);
                new DbCheck().execute();
            } else if (splitLesson.split("\\%")[1].substring(2).equals("mini_lesson")) {
                lessonId = "mini_lesson";
                UserDataConstants.courseId = split.toString().split("\\%")[1].substring(2);
                new DbCheck().execute();
            } else {
                split = split.toString().split("\\%")[1];
                String result = "";
                result = split.substring(2);
                courseId = result;

                new DbCheck().execute();
            }

        }

        if (getIntent().getStringExtra("notificationId")!=null){
            UserDataConstants.courseId = getIntent().getStringExtra("notificationId");
            if (getIntent().getStringExtra("status").equalsIgnoreCase("mini_lessons_enroll_page")) {
                new DbCheck().execute();
            } else {
                courseId = UserDataConstants.notificationID;
                notiTitle = getIntent().getStringExtra("notiTitle");
                new DbCheck().execute();
            }

        } else {


        }
    }

    private class DbCheck extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(result!=null) {
                if(result.equals("0")) {
                    startActivity(new Intent(DynamicLinkingActivity.this,RegistrationActivity.class));
                } else if (lessonId.equalsIgnoreCase("Home")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("home")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("aboutus")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("aboutus")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("enrol")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("enrol")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, EnrolNowActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("successstories")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("successstories")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, SuccessStoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("hunarclub")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("hunarclub")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, BuzzActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("contactus")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("contactus")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, ContactActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("profile")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("profile")) {
                        Intent intent = new Intent(DynamicLinkingActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (lessonId.equalsIgnoreCase("my-courses")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("my-courses")) {
                        userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                        courseId = UserDataConstants.courseId;
                        if (langPref.equals("english"))
                            language = "en";
                        else language = "hi";
                        getCourseDetails();
                    }
                }else if (lessonId.equalsIgnoreCase("paid_course")){
                    if (splitLesson.split("\\%")[1].substring(2).equals("paid_course")) {
                        userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                        courseId = UserDataConstants.courseId;
                        if (langPref.equals("english"))
                            language = "en";
                        else language = "hi";
                        getCourseDetails();
                    }
                } else if (getIntent().getStringExtra("status") != null) {
                    if (getIntent().getStringExtra("status").equalsIgnoreCase("mini_lessons_enroll_page")) {
                        userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                        getMyCourseDetails();
                        //getMiniLessonDetails();
                    }
                } else if (lessonId.equalsIgnoreCase("mini_lessons_course_page")) {
                    userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                    getCourseContentType();
                } else if (lessonId.equalsIgnoreCase("regular-courses")) {
                    userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                    getRegularCourseDetails();
                } else if (lessonId.equalsIgnoreCase("mini_lesson")) {
                    userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                    getCourseContentType();
                } else {
                    userDataBase = new UserDataBase(DynamicLinkingActivity.this);
                    if (UserDataConstants.lessonId.equalsIgnoreCase("")) {
                        getCourseType();
                    } else {
                        getLessons(DynamicLinkingActivity.this);
                    }

                    //getLessons(DynamicLinkingActivity.this);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            UserDataBase dh = new UserDataBase(DynamicLinkingActivity.this);
            int c = dh.getCount();
            if(c == 0) {
                return "0";
            } else {
                return "1";
            }

        }
    }

    public void getCourseType() {
        CourseType courseType = new CourseType("Hamstech",getResources().getString(R.string.lblApiKey),
                Integer.parseInt(UserDataConstants.courseId));
        Call<CourseType> call = apiService.getCourseType(courseType);
        call.enqueue(new Callback<CourseType>() {
            @Override
            public void onResponse(Call<CourseType> call, retrofit2.Response<CourseType> response) {
                if (response.body().getMy_course().equalsIgnoreCase("yes")) {
                    getMyCourseDetails();
                } else if (response.body().getMy_course().equalsIgnoreCase("no")) {
                    getLessons(DynamicLinkingActivity.this);
                } else {
                    startActivity(new Intent(DynamicLinkingActivity.this,HomePageActivity.class));
                }
            }

            @Override
            public void onFailure(Call<CourseType> call, Throwable t) {

            }
        });
    }

    public void getCourseContentType() {
        CourseType courseType = new CourseType("Hamstech",getResources().getString(R.string.lblApiKey),
                Integer.parseInt(UserDataConstants.courseId));
        Call<CourseType> call = apiService.getCourseType(courseType);
        call.enqueue(new Callback<CourseType>() {
            @Override
            public void onResponse(Call<CourseType> call, retrofit2.Response<CourseType> response) {
                if (response.body().getMiniCourse().equalsIgnoreCase("yes")) {
                    getMyCourseDetails();
                } else if (response.body().getMiniCourse().equalsIgnoreCase("no")) {
                    getLessons(DynamicLinkingActivity.this);
                } else {
                    startActivity(new Intent(DynamicLinkingActivity.this,HomePageActivity.class));
                }
            }

            @Override
            public void onFailure(Call<CourseType> call, Throwable t) {

            }
        });
    }

    public void getMyCourseDetails() {
        hocLoadingDialog.showLoadingDialog();
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",UserDataConstants.courseId,language,langPref,userDataBase.getUserMobileNumber(1),"");
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {
                hocLoadingDialog.hideDialog();
                if (lessonId.equalsIgnoreCase("mini_lesson")) {
                    Intent intent = new Intent(DynamicLinkingActivity.this, MiniLessonsCourseActivity.class);
                    intent.putExtra("CategoryId",response.body().getCourseDetails().getCourseId());
                    intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CourseName",response.body().getCourseDetails().getCourseTitle());
                    intent.putExtra("description",response.body().getCourseDetails().getCourseDescription());
                    intent.putExtra("language",response.body().getCourseDetails().getCourseLanguage());
                    intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                    intent.putExtra("amount",response.body().getCourseDetails().getAmount());
                    intent.putExtra("notificationId",getIntent().getStringExtra("notificationId"));
                    startActivity(intent);
                    finish();
                } else if (lessonId.equalsIgnoreCase("my-courses")){
                    Intent intent = new Intent(DynamicLinkingActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                    /*Intent intent = new Intent(DynamicLinkingActivity.this, CoursePageActivity.class);
                    intent.putExtra("CategoryId",datamodels.get(postn).getCategoryId());
                    intent.putExtra("CategoryName",datamodels.get(postn).getCategoryname());
                    intent.putExtra("CourseName",datamodels.get(postn).getCategory_Title());
                    intent.putExtra("description",datamodels.get(postn).getCategory_description());
                    intent.putExtra("language",datamodels.get(postn).getCategory_language());
                    intent.putExtra("VideoUrl",datamodels.get(postn).getCatVideoUrl());
                    intent.putExtra("statusNSDC",datamodels.get(postn).getStatusNSDC());
                    startActivity(intent);*/
                }else {
                    getMiniLessonDetails();
                    /*Intent intent = new Intent(DynamicLinkingActivity.this, MiniCourseDetailsActivity.class);
                    intent.putExtra("CategoryId",response.body().getCourseDetails().getCourseId());
                    intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CategoryLog",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CourseName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("description",response.body().getCourseDetails().getCourseTitle());
                    intent.putExtra("language",language);
                    intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                    intent.putExtra("statusNSDC","0");
                    startActivity(intent);
                    finish();*/
                }

            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public void MyCourseDetails() {
        hocLoadingDialog.showLoadingDialog();
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",UserDataConstants.courseId,language,langPref,userDataBase.getUserMobileNumber(1),"");
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {
                hocLoadingDialog.hideDialog();
                if (lessonId.equalsIgnoreCase("mini_lesson")) {
                    Intent intent = new Intent(DynamicLinkingActivity.this, MiniLessonsCourseActivity.class);
                    intent.putExtra("CategoryId",response.body().getCourseDetails().getCourseId());
                    intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CourseName",response.body().getCourseDetails().getCourseTitle());
                    intent.putExtra("description",response.body().getCourseDetails().getCourseDescription());
                    intent.putExtra("language",response.body().getCourseDetails().getCourseLanguage());
                    intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                    intent.putExtra("amount",response.body().getCourseDetails().getAmount());
                    intent.putExtra("notificationId",getIntent().getStringExtra("notificationId"));
                    startActivity(intent);
                    finish();
                } else {
                    getMiniLessonDetails();
                    /*Intent intent = new Intent(DynamicLinkingActivity.this, MiniCourseDetailsActivity.class);
                    intent.putExtra("CategoryId",response.body().getCourseDetails().getCourseId());
                    intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CategoryLog",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("CourseName",response.body().getCourseDetails().getCategoryname());
                    intent.putExtra("description",response.body().getCourseDetails().getCourseTitle());
                    intent.putExtra("language",language);
                    intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                    intent.putExtra("statusNSDC","0");
                    startActivity(intent);
                    finish();*/
                }

            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public void getRegularCourseDetails() {
        hocLoadingDialog.showLoadingDialog();
        CourseDetailsResponse courseDetailsResponse = new CourseDetailsResponse("Hamstech","course",
                getResources().getString(R.string.lblApiKey),UserDataConstants.courseId,language,langPref,
                userDataBase.getUserMobileNumber(1),"yes");
        Call<CourseDetailsResponse> call = apiService.getCourseDetails(courseDetailsResponse);
        call.enqueue(new Callback<CourseDetailsResponse>() {
            @Override
            public void onResponse(Call<CourseDetailsResponse> call, retrofit2.Response<CourseDetailsResponse> response) {
                hocLoadingDialog.hideDialog();

                Intent intent = new Intent(DynamicLinkingActivity.this, CoursePageActivity.class);
                intent.putExtra("CategoryId",response.body().getCourseDetails().getCategoryId());
                intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryName());
                intent.putExtra("CourseName",response.body().getCourseDetails().getCourseTitle());
                intent.putExtra("description",response.body().getCourseDetails().getCourseDescription());
                if (language.equalsIgnoreCase("en")) {
                    intent.putExtra("language","english");
                } else if (language.equalsIgnoreCase("hi")){
                    intent.putExtra("language","hi");
                }

                intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                intent.putExtra("statusNSDC",response.body().getCourseDetails().getStatus());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<CourseDetailsResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
                Toast.makeText(DynamicLinkingActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getMiniLessonDetails() {
        hocLoadingDialog.showLoadingDialog();
        CourseDetailsResponse courseDetailsResponse = new CourseDetailsResponse("Hamstech","course",
                getResources().getString(R.string.lblApiKey),UserDataConstants.courseId,language,langPref,
                userDataBase.getUserMobileNumber(1),"yes");
        Call<CourseDetailsResponse> call = apiService.getCourseDetails(courseDetailsResponse);
        call.enqueue(new Callback<CourseDetailsResponse>() {
            @Override
            public void onResponse(Call<CourseDetailsResponse> call, retrofit2.Response<CourseDetailsResponse> response) {
                hocLoadingDialog.hideDialog();
                Intent intent = new Intent(DynamicLinkingActivity.this, MiniLessonsEnrolNowActivity.class);
                intent.putExtra("getCourseId",Integer.parseInt(UserDataConstants.courseId));
                intent.putExtra("status",getIntent().getStringExtra("status"));
                intent.putExtra("notificationTtitle","");
                intent.putExtra("CategoryName",response.body().getCourseDetails().getCategoryName());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<CourseDetailsResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
                Toast.makeText(DynamicLinkingActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCourseDetails() {
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",courseId,language,langPref,userDataBase.getUserMobileNumber(1),"");
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {
                Intent intent = new Intent(DynamicLinkingActivity.this, MyCoursesPageActivity.class);
                intent.putExtra("CategoryId",response.body().getCourseDetails().getCourseId());
                intent.putExtra("CategoryName",response.body().getCourseDetails().getCourseTitle());
                intent.putExtra("CategoryLog",response.body().getCourseDetails().getCourseTitle());
                intent.putExtra("CourseName",response.body().getCourseDetails().getCourseTitle());
                intent.putExtra("description",response.body().getCourseDetails().getCourseDescription());
                intent.putExtra("language",response.body().getCourseDetails().getCourseLanguage());
                intent.putExtra("VideoUrl",response.body().getCourseDetails().getVideoUrl());
                intent.putExtra("order_id",response.body().getCourseDetails().getOrder_id());
                intent.putExtra("email",response.body().getCourseDetails().getEmail());
                intent.putExtra("statusNSDC","0");
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {

            }
        });
    }

    public void getLessons(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("courseId",UserDataConstants.courseId);
            params.put("language",language);
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getListLessons, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);

                    if (jo.getString("status").equals("ok")) {
                        JSONObject object = new JSONObject(jo.getString("detail"));
                        Intent intent = new Intent(DynamicLinkingActivity.this, CoursePageActivity.class);
                        intent.putExtra("CategoryId",object.getString("courseId"));
                        intent.putExtra("CategoryName",object.getString("categoryname"));
                        intent.putExtra("CourseName",object.getString("course_title"));
                        intent.putExtra("description",object.getString("course_description"));
                        intent.putExtra("language",language);
                        intent.putExtra("VideoUrl",object.getString("video_url"));
                        intent.putExtra("statusNSDC",object.getString("nsdc_status"));
                        intent.putExtra("notiTitle",notiTitle);
                        intent.putExtra("notificationId",courseId);
                        startActivity(intent);
                        finish();
                    }

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
                    Toast.makeText(DynamicLinkingActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
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

}
