package com.hamstechonline.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.MyCoursesPageActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.ExpertFaculty;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.datamodel.mycources.DownloadCertificate;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.DownloadPdfTask;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultySliderAdapter extends PagerAdapter {

    Context context;
    List<ExpertFaculty> datamodels;
    ImageView imgFaculty;
    TextView facultyName,facultyDesignation,facultyDescription;
    LogEventsActivity logEventsActivity;
    String CourseLog,CategoryLog,mobile = "";
    UserDataBase userDataBase;
    ApiInterface apiService;

    public FacultySliderAdapter(Context context, List<ExpertFaculty> datamodels) {
        this.context = context;
        this.datamodels = datamodels;
        logEventsActivity = new LogEventsActivity();
        userDataBase = new UserDataBase(context);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        try {
            mobile = userDataBase.getUserMobileNumber(1);
            //email = "";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return datamodels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.faculty_slider_adapter, container, false);
        container.addView(view);
        bind(view,position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(View view, final int position) {
        facultyName = view.findViewById(R.id.facultyName);
        facultyDesignation = view.findViewById(R.id.facultyDesignation);
        facultyDescription = view.findViewById(R.id.facultyDescription);
        imgFaculty = view.findViewById(R.id.imgFaculty);

        Glide.with(context)

                .load(datamodels.get(position).getFacultyImage())
                //.placeholder(R.drawable.duser1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(imgFaculty);

        facultyName.setText(datamodels.get(position).getFacultyName());
        facultyDesignation.setText(datamodels.get(position).getDesignation());
        facultyDescription.setText(datamodels.get(position).getFacultyDescription());


    }


    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category",CategoryLog);
            data.put("course",CourseLog);
            data.put("lesson","");
            data.put("activity","Clicked");
            data.put("pagename","My Courses Page");
            logEventsActivity.LogEventsActivity(context,data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
