package com.hamstechonline.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.hamstechonline.R;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.LessonsPageActivity;
import com.hamstechonline.activities.LessonsPageNotifications;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MiniLessonsFragment extends Fragment {

    RecyclerView itemLessons;
    LessonsFragmentAdapter lessonsFragmentAdapter;
    ArrayList<LessonsDataModel> coursesList = new ArrayList<>();
    ArrayList<LessonsDataModel> unlockedLessonsList = new ArrayList<>();
    String searchName,language,CategoryName,CourseName;
    UserDataBase userDataBase;
    String langPref = "Language";
    SharedPreferences prefs;
    AppEventsLogger logger;
    Bundle params;
    int lessonPosition;
    SharedPrefsUtils sharedPrefsUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lessons_fragment, container, false);

        prefs = getActivity().getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        itemLessons = v.findViewById(R.id.itemLessons);
        userDataBase = new UserDataBase(getActivity());
        logger = AppEventsLogger.newLogger(getContext());
        params = new Bundle();
        sharedPrefsUtils = new SharedPrefsUtils(getActivity(), getString(R.string.app_name));
        if (getActivity().getIntent().getStringExtra("searchItem") != null){
            searchName = getActivity().getIntent().getStringExtra("CategoryId");
            language = getActivity().getIntent().getStringExtra("language");
            CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
        } else {
            searchName = getActivity().getIntent().getStringExtra("CategoryId");
            language = getActivity().getIntent().getStringExtra("language");
            CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
            CourseName = getActivity().getIntent().getStringExtra("CourseName");
            //CategoryName
        }
        getLessons(getActivity());

        return v;
    }

    public static MiniLessonsFragment newInstance(String text) {

        MiniLessonsFragment f = new MiniLessonsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
    public void getLessons(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("courseId",searchName);
            params.put("language",language);
            params.put("lang",langPref);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = params.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.getCourseDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    coursesList.clear();
                    unlockedLessonsList.clear();
                    if (jo.isNull("lessons")) {

                    } else {
                        JSONArray jsonArray = jo.getJSONArray("lessons");
                        for (int i = 0; i<jsonArray.length(); i++){

                            JSONObject obj = jsonArray.getJSONObject(i);
                            LessonsDataModel datamodel = new LessonsDataModel();
                            datamodel.setLessonId(obj.getString("lessonId"));
                            datamodel.setLesson_title(obj.getString("lesson_title"));
                            datamodel.setCourse_title(obj.getString("course_title"));
                            datamodel.setVideo_duration(obj.getString("video_duration"));
                            datamodel.setLesson_description(obj.getString("lesson_description"));
                            datamodel.setLesson_image_url(obj.getString("lesson_image_url"));
                            datamodel.setStudy_material_url(obj.getString("study_material_url"));
                            datamodel.setLesson_video_url(obj.getString("video_url"));
                            datamodel.setLessonImage(obj.getString("lesson_page_url"));
                            datamodel.setTextImage(obj.getString("lesson_page_text"));
                            datamodel.setLock_status(Integer.parseInt(obj.getString("lock_value")));
                            datamodel.setCategory_name(CategoryName);
                            datamodel.setCourseId(searchName);

                            if (obj.getString("lock_value").equalsIgnoreCase("1")){
                                unlockedLessonsList.add(datamodel);
                            }

                            coursesList.add(datamodel);
                        }
                        lessonsFragmentAdapter = new LessonsFragmentAdapter(getActivity(),coursesList,
                                Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")),
                                unlockedLessonsList);

                        itemLessons.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        itemLessons.addItemDecoration(new GridSpacingItemDecoration(1,35,false));
                        itemLessons.setAdapter(lessonsFragmentAdapter);
                        if (!UserDataConstants.lessonId.equals("")){
                            for (int i = 0; i< coursesList.size(); i++){
                                if (coursesList.get(i).getLessonId().equals(UserDataConstants.lessonId)){
                                    lessonPosition = i;
                                    CategoryName = coursesList.get(lessonPosition).getCategory_name();
                                    Intent intent = new Intent(getActivity(), LessonsPageActivity.class);
                                    intent.putExtra("videoURL", coursesList.get(lessonPosition).lesson_video_url);
                                    intent.putExtra("CategoryName", coursesList.get(lessonPosition).getCategory_name());
                                    intent.putExtra("CourseName", coursesList.get(lessonPosition).getCourse_title());
                                    intent.putExtra("LessonName", coursesList.get(lessonPosition).getLesson_title());
                                    intent.putExtra("description", coursesList.get(lessonPosition).getLesson_description());
                                    intent.putExtra("pdfURL", coursesList.get(lessonPosition).study_material_url);
                                    intent.putExtra("CourseId", coursesList.get(lessonPosition).getCourseId());
                                    intent.putExtra("LessonId", coursesList.get(lessonPosition).getLessonId());
                                    intent.putExtra("LessonImage", coursesList.get(lessonPosition).getLessonImage());
                                    intent.putExtra("LessonText", coursesList.get(lessonPosition).getTextImage());
                                    intent.putExtra("LessonData", (ArrayList<LessonsDataModel>) unlockedLessonsList);
                                    intent.putExtra("intNext", lessonPosition);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        }
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
                    return null;
                }
            }

        };
        sr.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public class LessonsFragmentAdapter extends RecyclerView.Adapter<LessonsFragmentAdapter.ViewHolder> {

        Context context;
        ArrayList<LessonsDataModel> coursesList;
        ArrayList<LessonsDataModel> unlockedLessonsList;
        LogEventsActivity logEventsActivity;
        String CategoryName, CourseLog, LessonLog, ActivityLog, PagenameLog;
        int CatId;

        public LessonsFragmentAdapter(Context context, ArrayList<LessonsDataModel> coursesList, int CatId, ArrayList<LessonsDataModel> unlockedLessonsList) {
            this.context = context;
            this.coursesList = coursesList;
            this.unlockedLessonsList = unlockedLessonsList;
            this.CatId = CatId;
            logEventsActivity = new LogEventsActivity();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.lessons_fragment_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                holder.txtTitle.setText(coursesList.get(position).getLesson_title());
                holder.txtDescription.setText(coursesList.get(position).getLesson_description());
                holder.txtDuration.setText(coursesList.get(position).getVideo_duration());
                holder.imgLock.setVisibility(View.GONE);
                Glide.with(context)
                        .load(coursesList.get(position).getLesson_image_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);

                if (coursesList.get(position).getLock_status() == 1) {
                    holder.imgLock.setVisibility(View.VISIBLE);
                }

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AppsFlyerEventsHelper(context).EventLessons(coursesList.get(position).getCategory_name(),
                                coursesList.get(position).getCourse_title(),coursesList.get(position).getLesson_title());
                        CategoryName = coursesList.get(position).getCategory_name();
                        CourseLog = coursesList.get(position).getCourse_title();
                        LessonLog = coursesList.get(position).getLesson_title();
                        ActivityLog = "Click";PagenameLog = "Enroll Page";
                        getLogEvent(context);
                        new AppsFlyerEventsHelper(context).EventEnroll();
                        Intent intent = new Intent(context, MiniLessonsEnrolNowActivity.class);
                        sharedPrefsUtils.setSharedPrefBoolean(ApiConstants.isFromCourse, false);
                        intent.putExtra("getCourseId",Integer.parseInt(getActivity().getIntent().getStringExtra("CategoryId")));
                        startActivity(intent);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory, imgLock;
            TextView txtTitle, txtDescription, txtDuration;
            RelativeLayout mainLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.profile_image);
                txtTitle = view.findViewById(R.id.txtTitle);
                txtDescription = view.findViewById(R.id.txtDescription);
                txtDuration = view.findViewById(R.id.txtDuration);
                mainLayout = view.findViewById(R.id.mainLayout);
                imgLock = view.findViewById(R.id.imgLock);
            }
        }

        public void lockPopup(final Context context, final int listPosition) {
            final Dialog dialog = new Dialog(context);
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

                }
            });

            dialog.show();
        }

        public void getLogEvent(Context context) {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject params = new JSONObject();
            JSONObject metaData = new JSONObject();
            JSONObject data = new JSONObject();
            try {
                data.put("apikey", context.getResources().getString(R.string.lblApiKey));
                data.put("appname", "Dashboard");
                data.put("mobile", UserDataConstants.userMobile);
                data.put("fullname", UserDataConstants.userName);
                data.put("email", UserDataConstants.userMail);
                data.put("category", CategoryName);
                data.put("course", CourseLog);
                data.put("lesson", LessonLog);
                data.put("activity", ActivityLog);
                data.put("pagename", PagenameLog);
                logEventsActivity.LogEventsActivity(context, data);
                metaData.put("metadata", params);
                metaData.put("data", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
