package com.hamstechonline.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.AppEventsLogger;
import com.hamstechonline.R;
import com.hamstechonline.activities.EditProfileActivity;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.mycources.DownloadCertificate;
import com.hamstechonline.datamodel.mycources.MyCoursesResponse;
import com.hamstechonline.datamodel.mycources.UploadResponse;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.DownloadPdfTask;
import com.hamstechonline.utils.DownloadTask;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Assignments_Downloads_fragment extends Fragment {

    String searchName,language,CategoryName,CourseName;
    UserDataBase userDataBase;
    String langPref = "Language", urlAssignment, urlCertificate;
    String CourseLog,LessonLog = "",ActivityLog,PagenameLog,mobile = "",
            fullname = "",email = "",CategoryLog;
    AppEventsLogger logger;
    SharedPrefsUtils sharedPrefsUtils;
    ApiInterface apiService;
    TextView uploadFileName,btnSubmit,txtChat,linerCertificate;
    RelativeLayout downAssignment,btnFileUpload,dwnCertificate;
    private static final int FILE_SELECT_CODE = 0;
    Bitmap mBitmap, resized;
    String type_image, picturePath = "", imgString;
    private String imagePathData = "";
    LogEventsActivity logEventsActivity;
    private final int PERM_READ_WRITE_STORAGE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.assignments_fragment, container, false);

        downAssignment = v.findViewById(R.id.downAssignment);
        btnFileUpload = v.findViewById(R.id.btnFileUpload);
        dwnCertificate = v.findViewById(R.id.dwnCertificate);
        uploadFileName = v.findViewById(R.id.uploadFileName);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        txtChat = v.findViewById(R.id.txtChat);
        linerCertificate = v.findViewById(R.id.linerCertificate);

        userDataBase = new UserDataBase(getActivity());
        logger = AppEventsLogger.newLogger(getContext());
        apiService = ApiClient.getClient().create(ApiInterface.class);

        sharedPrefsUtils = new SharedPrefsUtils(getActivity(), getString(R.string.app_name));

        searchName = getActivity().getIntent().getStringExtra("CategoryId");
        language = getActivity().getIntent().getStringExtra("language");
        CourseName = getActivity().getIntent().getStringExtra("CourseName");
        logEventsActivity = new LogEventsActivity();
        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = "";
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
        LessonLog = "";

        getTopics();

        downAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), ""+urlAssignment, Toast.LENGTH_SHORT).show();
                if (!checkPermissions()) {
                    Toast.makeText(getActivity(),"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    ActivityLog = "Download Assignment";
                    getLogEvent(getActivity());
                    new DownloadTask(getActivity(), urlAssignment,CourseName);

                }
            }
        });

        btnFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    Toast.makeText(getActivity(),"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    //uploadSuccessPopUp();
                    showFileChooser();
                }
            }
        });

        dwnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    Toast.makeText(getActivity(),"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    ActivityLog = "Download Certificate";
                    PagenameLog = "Course Page";
                    LessonLog = "";
                    getLogEvent(getActivity());
                    downloadCertificate();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picturePath.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select file", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityLog = "Upload Assignment";
                    getLogEvent(getActivity());
                    uploadFile();
                }
            }
        });

        txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLog = "Chat with Student Guide";
                getLogEvent(getActivity());
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case PERM_READ_WRITE_STORAGE:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted, do nothing
                } else {
                    Toast.makeText(getActivity(), "Permission is required to download PDF file", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public static Assignments_Downloads_fragment newInstance(String text) {

        Assignments_Downloads_fragment f = new Assignments_Downloads_fragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void getTopics() {
        MyCoursesResponse myCoursesResponse = new MyCoursesResponse("Hamstech",getResources().getString(R.string.lblApiKey),
                "course",searchName,language,langPref,userDataBase.getUserMobileNumber(1),"");
        Call<MyCoursesResponse> call = apiService.getMyCoursesResponse(myCoursesResponse);
        call.enqueue(new Callback<MyCoursesResponse>() {
            @Override
            public void onResponse(Call<MyCoursesResponse> call, retrofit2.Response<MyCoursesResponse> response) {
                urlAssignment = response.body().getCourseAssignment();
                CategoryLog = response.body().getCourseDetails().getCategoryname();
                if (response.body().getDownloadCertificate().equalsIgnoreCase("no")) {
                    dwnCertificate.setEnabled(false);
                    linerCertificate.setVisibility(View.VISIBLE);
                } else {
                    dwnCertificate.setEnabled(true);
                    linerCertificate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MyCoursesResponse> call, Throwable t) {

            }
        });
    }

    public void downloadCertificate() {
        DownloadCertificate downloadCertificate = new DownloadCertificate("Hamstech", getActivity().getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),searchName);
        Call<DownloadCertificate> call = apiService.getDownloadCertificate(downloadCertificate);
        call.enqueue(new Callback<DownloadCertificate>() {
            @Override
            public void onResponse(Call<DownloadCertificate> call, Response<DownloadCertificate> response) {
                urlCertificate = response.body().getUrl();
                new DownloadPdfTask(getActivity(), urlCertificate,CourseName);
            }

            @Override
            public void onFailure(Call<DownloadCertificate> call, Throwable t) {

            }
        });
    }

    private  boolean checkPermissions() {
        int result;

        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERM_READ_WRITE_STORAGE);
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
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
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
                        mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

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
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    type_image = getActivity().getContentResolver().getType(selectedImage);

                    if (type_image.equals(null)) type_image = "images/jpeg";

                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    try{
                        File file = new File(picturePath);
                        long length = file.length();
                        length = length/1024;
                        uploadFileName.setText(file.getPath());
                    } catch(Exception e) {
                        System.out.println("File not found : " + e.getMessage() + e);
                    }

                    if (picturePath.isEmpty() || picturePath == null) {
                        Toast.makeText(getActivity(), "Unable to capture the image..Please try again", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Image Loading Failed", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getActivity(),
                        "User cancelled file upload", Toast.LENGTH_LONG)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getActivity(),
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
        UploadResponse uploadResponse = new UploadResponse("Hamstech", getActivity().getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),searchName,imagePathData);
        Call<UploadResponse> call = apiService.getUploadResponse(uploadResponse);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if (response.body().getStatus().equalsIgnoreCase("200")) {
                    //Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Assignment_Submission_Liner), Toast.LENGTH_SHORT).show();
                    imagePathData = "";picturePath = "";
                    uploadFileName.setText("Select Your files");
                    uploadSuccessPopUp();
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {

            }
        });
    }

    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.upload_successfull);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);

        Glide.with(getActivity())
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
            data.put("apikey",getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",email);
            data.put("category",CategoryLog);
            data.put("course",CourseName);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename","Course Page");
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
