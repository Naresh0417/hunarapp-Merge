package com.hamstechonline.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.AppEventsLogger;
import com.hamstechonline.R;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.LessonsPageActivity;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;
import com.hamstechonline.activities.MyCoursesLessonsPage;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CallWithFacultyResponse;
import com.hamstechonline.datamodel.LessonsDataModel;
import com.hamstechonline.datamodel.mycources.Lesson;
import com.hamstechonline.datamodel.mycources.MyCoursesResponse;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicsFragment extends Fragment {

    RecyclerView itemLessons;
    LessonsFragmentAdapter lessonsFragmentAdapter;
    TextView txtCallRequest,txtChat;
    LinearLayout floatBtns;
    String searchName,language,CategoryName,CourseName,CategoryLog;
    UserDataBase userDataBase;
    String langPref = "Language";
    LogEventsActivity logEventsActivity;
    String CourseLog, LessonLog, ActivityLog, PagenameLog;
    SharedPreferences prefs;
    AppEventsLogger logger;
    Bundle params;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    HocLoadingDialog hocLoadingDialog;
    DownloadManager downloadManager;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lessons_fragment, container, false);

        prefs = getActivity().getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");

        itemLessons = v.findViewById(R.id.itemLessons);
        txtCallRequest = v.findViewById(R.id.txtCallRequest);
        txtChat = v.findViewById(R.id.txtChat);
        floatBtns = v.findViewById(R.id.floatBtns);

        userDataBase = new UserDataBase(getActivity());
        logger = AppEventsLogger.newLogger(getContext());
        apiService = ApiClient.getClient().create(ApiInterface.class);
        hocLoadingDialog = new HocLoadingDialog(getActivity());

        params = new Bundle();
        sharedPrefsUtils = new SharedPrefsUtils(getActivity(), getString(R.string.app_name));
        logEventsActivity = new LogEventsActivity();
        if (getActivity().getIntent().getStringExtra("searchItem") != null){
            searchName = getActivity().getIntent().getStringExtra("CategoryId");
            language = getActivity().getIntent().getStringExtra("language");
            CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
            CourseName = getActivity().getIntent().getStringExtra("CategoryName");
        } else {
            searchName = getActivity().getIntent().getStringExtra("CategoryId");
            language = getActivity().getIntent().getStringExtra("language");
            CategoryName = getActivity().getIntent().getStringExtra("CategoryName");
            CourseName = getActivity().getIntent().getStringExtra("CourseName");
            //CategoryName
        }
        floatBtns.setVisibility(View.VISIBLE);
        getTopics();

        txtCallRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Video Call with Faculty";
                LessonLog = "";
                getLogEvent(getActivity());
                getCallWithFaculty();
            }
        });
        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Chat with Student Guide";
                LessonLog = "";
                getLogEvent(getActivity());
                PackageManager packageManager = getActivity().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                            URLEncoder.encode(getResources().getString(R.string.chat_student_guide), "UTF-8");
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

        return v;
    }

    public static TopicsFragment newInstance(String text) {

        TopicsFragment f = new TopicsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void getTopics() {
        hocLoadingDialog.showLoadingDialog();
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",searchName,language,langPref,userDataBase.getUserMobileNumber(1),"");
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {
                hocLoadingDialog.hideDialog();
                lessonsFragmentAdapter = new LessonsFragmentAdapter(getActivity(),response.body().getLessons());
                itemLessons.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                //itemLessons.addItemDecoration(new GridSpacingItemDecoration(1,0,false));
                itemLessons.setAdapter(lessonsFragmentAdapter);

                CategoryLog = response.body().getCourseDetails().getCategoryname();
            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public void getCallWithFaculty() {
        hocLoadingDialog.showLoadingDialog();
        CallWithFacultyResponse callWithFacultyResponse = new CallWithFacultyResponse("Hamstech", getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),searchName);
        Call<CallWithFacultyResponse> call = apiService.getCallWithFacultyResponse(callWithFacultyResponse);
        call.enqueue(new Callback<CallWithFacultyResponse>() {
            @Override
            public void onResponse(Call<CallWithFacultyResponse> call, Response<CallWithFacultyResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    OnlineSuccessfulPopUp();
                }
            }

            @Override
            public void onFailure(Call<CallWithFacultyResponse> call, Throwable t) {
                hocLoadingDialog.hideDialog();
            }
        });
    }

    public void OnlineSuccessfulPopUp(){
        final Dialog dialog = new Dialog(getActivity());
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

        Glide.with(getActivity())
                .load(R.drawable.ic_sucess_payment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_payment)
                .into(progressBar);
        paymentComment.setText(getActivity().getResources().getString(R.string.call_request_accepted));

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
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
                    Intent intent = new Intent(getActivity(), HomePageActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }


    public class LessonsFragmentAdapter extends RecyclerView.Adapter<LessonsFragmentAdapter.ViewHolder> {

        Context context;
        List<Lesson> coursesList = new ArrayList<>();

        public LessonsFragmentAdapter(Context context, List<Lesson> coursesList) {
            this.context = context;
            this.coursesList = coursesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.topics_fragment_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            try {
                holder.txtTitle.setText(coursesList.get(position).getLessonTitle());
                holder.imgLock.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(coursesList.get(position).getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgLock);

                holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CourseLog = CourseName;
                        LessonLog = coursesList.get(position).getLessonTitle();
                        ActivityLog = "Click";
                        PagenameLog = "Course Page";
                        getLogEvent(context);
                        if (coursesList.get(position).getType().equalsIgnoreCase("handout")) {
                            if (!checkPermissions()) {
                                Toast.makeText(getActivity(),"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                                return;
                            } else {
                                startDownload(coursesList.get(position).getStudyMaterialUrl(), "_"+coursesList.get(position).getLessonTitle());
                            }
                        } else if (coursesList.get(position).getType().equalsIgnoreCase("live")) {
                            new AppsFlyerEventsHelper(context).EventEnroll();
                            Intent intent = new Intent(context, MyCoursesLessonsPage.class);
                            intent.putExtra("videoURL", coursesList.get(position).getVideoUrl());
                            intent.putExtra("CategoryName", coursesList.get(position).getCourseTitle());
                            intent.putExtra("CategoryLog", CategoryLog);
                            intent.putExtra("CourseName", coursesList.get(position).getCourseTitle());
                            intent.putExtra("LessonName", coursesList.get(position).getLessonTitle());
                            intent.putExtra("description", coursesList.get(position).getLessonDescription());
                            intent.putExtra("pdfURL", coursesList.get(position).getStudyMaterialUrl());
                            intent.putExtra("CourseId", coursesList.get(position).getLessonId());
                            intent.putExtra("LessonId", coursesList.get(position).getLessonId());
                            intent.putExtra("LessonImage", coursesList.get(position).getLessonImageUrl());
                            intent.putExtra("LessonText", coursesList.get(position).getLessonImageUrl());
                            //intent.putParcelableArrayListExtra("LessonData", (ArrayList<Lesson>) coursesList);
                            intent.putExtra("LessonData", (ArrayList<? extends Serializable>) coursesList);
                            intent.putExtra("intNext", position);
                            context.startActivity(intent);
                        } else if (coursesList.get(position).getType().equalsIgnoreCase("video")) {
                            new AppsFlyerEventsHelper(context).EventEnroll();
                            Intent intent = new Intent(context, MyCoursesLessonsPage.class);
                            intent.putExtra("videoURL", coursesList.get(position).getVideoUrl());
                            intent.putExtra("CategoryName", coursesList.get(position).getCourseTitle());
                            intent.putExtra("CategoryLog", CategoryLog);
                            intent.putExtra("CourseName", coursesList.get(position).getCourseTitle());
                            intent.putExtra("LessonName", coursesList.get(position).getLessonTitle());
                            intent.putExtra("description", coursesList.get(position).getLessonDescription());
                            intent.putExtra("pdfURL", coursesList.get(position).getStudyMaterialUrl());
                            intent.putExtra("CourseId", searchName);
                            intent.putExtra("LessonId", coursesList.get(position).getLessonId());
                            intent.putExtra("LessonImage", coursesList.get(position).getLessonImageUrl());
                            intent.putExtra("LessonText", coursesList.get(position).getLessonImageUrl());
                            //intent.putParcelableArrayListExtra("LessonData", (ArrayList<Lesson>) coursesList);
                            intent.putExtra("LessonData", (ArrayList<? extends Serializable>) coursesList);
                            intent.putExtra("intNext", position);
                            context.startActivity(intent);
                        }
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
            LinearLayout mainLayout;

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

        private  boolean checkPermissions() {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p:permissions) {
                result = ContextCompat.checkSelfPermission(getActivity(),p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
                return false;
            }
            return true;
        }

        public void startDownload(String url, String file_name){
            Toast.makeText(getActivity(),"Please Wait until the file is downloaded",Toast.LENGTH_LONG).show();
            downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            //Toast.makeText(getActivity(), ""+url.substring(url.length()-4), Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setTitle("" + CourseName+file_name + url.substring(url.length()-4));
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

    }
    public void getLogEvent(Context context) {
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey", context.getResources().getString(R.string.lblApiKey));
            data.put("appname", "Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname", UserDataConstants.userName);
            data.put("email", UserDataConstants.userMail);
            data.put("category", CategoryLog);
            data.put("course", CourseName);
            data.put("lesson", LessonLog);
            data.put("activity", ActivityLog);
            data.put("pagename", "Course Page");
            logEventsActivity.LogEventsActivity(context, data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
