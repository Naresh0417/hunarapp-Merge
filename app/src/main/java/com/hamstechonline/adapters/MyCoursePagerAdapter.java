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
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.MiniCourseDetailsActivity;
import com.hamstechonline.activities.MyCoursesPageActivity;
import com.hamstechonline.activities.SuccessStoryActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.datamodel.homepage.SuccessStory;
import com.hamstechonline.datamodel.mycources.DownloadCertificate;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
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

public class MyCoursePagerAdapter extends PagerAdapter implements PaymentResultWithDataListener {

    Context context;
    ArrayList<MyCourse> datamodels;
    ImageView imgCategory;
    TextView txtBottomTitle,videoTxtPercentage,daysLeft,nextInstalmentDate,txtCertificate,btnPay,txtCourseStatus;
    CardView cardInstallment;
    RelativeLayout listLayout;
    LogEventsActivity logEventsActivity;
    String CourseLog,CategoryLog,searchName,urlCertificate,CourseName,ActivityLog = "Clicked",
            tracking_id = "",mobile = "",m_strEmail,PagenameLog = "MyCourse page";
    ProgressBar progressBar;
    UserDataBase userDataBase;
    ApiInterface apiService;
    LinearLayout dwnCertificate;
    private final int PERM_READ_WRITE_STORAGE = 101;
    float finalAmount;
    long orderID;

    public MyCoursePagerAdapter(Context context, ArrayList<MyCourse> datamodels) {
        this.context = context;
        this.datamodels = datamodels;
        logEventsActivity = new LogEventsActivity();
        userDataBase = new UserDataBase(context);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        try {
            mobile = userDataBase.getUserMobileNumber(1);
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
                .inflate(R.layout.mycourses_adapter, container, false);
        container.addView(view);
        bind(view,position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void bind(View view, int position) {
        imgCategory = view.findViewById(R.id.imgCategory);
        txtBottomTitle = view.findViewById(R.id.txtBottomTitle);
        txtCourseStatus = view.findViewById(R.id.txtCourseStatus);
        listLayout = view.findViewById(R.id.listLayout);
        progressBar = view.findViewById(R.id.progressBar);
        videoTxtPercentage = view.findViewById(R.id.videoTxtPercentage);
        daysLeft = view.findViewById(R.id.daysLeft);
        nextInstalmentDate = view.findViewById(R.id.nextInstalmentDate);
        dwnCertificate = view.findViewById(R.id.dwnCertificate);
        cardInstallment = view.findViewById(R.id.cardInstallment);
        txtCertificate = view.findViewById(R.id.txtCertificate);
        btnPay = view.findViewById(R.id.btnPay);

        CategoryLog = datamodels.get(position).getCategoryname();
        Glide.with(context)
                .load(datamodels.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(imgCategory);

        txtBottomTitle.setText(datamodels.get(position).getCourseTitle());
        progressBar.setProgress(datamodels.get(position).getWatchedPercentage());
        if (datamodels.get(position).getWatchedPercentage() > 46) {
            videoTxtPercentage.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            videoTxtPercentage.setTextColor(context.getResources().getColor(R.color.dark_pink));
        }
        if (datamodels.get(position).getWatchedPercentage() == 100) {
            if (datamodels.get(position).getDownloadCertificate().equalsIgnoreCase("no")) {
                dwnCertificate.setEnabled(false);
                txtCertificate.setText(R.string.complete_your_assignment);
            } else {
                txtCertificate.setText(R.string.download_certificate);
                dwnCertificate.setEnabled(true);
            }
            txtCourseStatus.setText(R.string.course_completed);
        } else {
            txtCertificate.setText(R.string.complete_your_course_to_download);
            dwnCertificate.setEnabled(false);
            txtCourseStatus.setText(R.string.course_in_progress);
        }
        videoTxtPercentage.setText(String.valueOf(datamodels.get(position).getWatchedPercentage())+" %");
        daysLeft.setText(datamodels.get(position).getDaysLeft()+" "+context.getResources().getString(R.string.days_left));
        nextInstalmentDate.setText(datamodels.get(position).getNextInstallmentDate());

        if (datamodels.get(position).getDownloadCertificate().equalsIgnoreCase("no")) {
            dwnCertificate.setEnabled(false);
            //txtCertificate.setText("Complete your assignments to download your certificate");
        } else {
            //txtCertificate.setText(R.string.download_certificate);
            dwnCertificate.setEnabled(true);
        }

        if (datamodels.get(position).getShowInstallment().equalsIgnoreCase("yes")) {
            cardInstallment.setVisibility(View.VISIBLE);
        } else cardInstallment.setVisibility(View.GONE);

        /*if (datamodels.get(position).getWatchedPercentage() == 100) {

            txtCertificate.setEnabled(true);
        } else {
            txtCertificate.setEnabled(false);
        }*/

        dwnCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermissions()) {
                    Toast.makeText(context,"Please provide permissions to move ahead.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    ActivityLog = "Download Certificate";
                    CourseLog = datamodels.get(position).getCourseTitle();
                    searchName = datamodels.get(position).getCourseId();
                    getLogEvent(context);
                    PagenameLog = "Home page";
                    downloadCertificate();
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAmount = Float.parseFloat(datamodels.get(position).getInstallmentAmount());
                m_strEmail = datamodels.get(position).getEmail();
                orderID = Long.parseLong(datamodels.get(position).getOrderId());
                CourseLog = datamodels.get(position).getCategoryname();
                ActivityLog = "MyCourse Pay";
                PagenameLog = "Home page";
                getLogEvent(context);
                startPayment();
            }
        });

        listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryLog = datamodels.get(position).getCategoryname();
                CourseLog = datamodels.get(position).getCourseTitle();
                Intent intent = new Intent(context, MyCoursesPageActivity.class);
                intent.putExtra("CategoryId",datamodels.get(position).getCourseId());
                intent.putExtra("CategoryName",datamodels.get(position).getCourseTitle());
                intent.putExtra("CategoryLog",datamodels.get(position).getCategoryname());
                intent.putExtra("CourseName",datamodels.get(position).getCourseTitle());
                intent.putExtra("description",datamodels.get(position).getCourseTitle());
                intent.putExtra("language",datamodels.get(position).getLanguage());
                //intent.putExtra("VideoUrl",datamodels.get(position).getVideoUrl());
                intent.putExtra("order_id",datamodels.get(position).getOrderId());
                intent.putExtra("email",datamodels.get(position).getEmail());
                intent.putExtra("statusNSDC","0");
                getLogEvent(context);
                new AppsFlyerEventsHelper(context).EventMyCourse();
                context.startActivity(intent);
            }
        });
    }

    public void downloadCertificate() {
        DownloadCertificate downloadCertificate = new DownloadCertificate("Hamstech", context.getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1),searchName);
        Call<DownloadCertificate> call = apiService.getDownloadCertificate(downloadCertificate);
        call.enqueue(new Callback<DownloadCertificate>() {
            @Override
            public void onResponse(Call<DownloadCertificate> call, Response<DownloadCertificate> response) {
                urlCertificate = response.body().getUrl();
                new DownloadPdfTask(context, urlCertificate,CategoryLog);
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
            result = ContextCompat.checkSelfPermission(context,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERM_READ_WRITE_STORAGE);
            return false;
        }

        return true;
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        //final Activity activity = this;

        final Checkout co = new Checkout();
        orderID = System.currentTimeMillis();

        try {
            String vAmount = String.format("%.0f", finalAmount);
            //if (paymentOption < 4) {
            vAmount = String.format("%.0f", finalAmount);
            //}
            vAmount = vAmount + "00";
            JSONObject options = new JSONObject();
            options.put("name", "Hunar");
            options.put("description", "Online Courses");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://res.cloudinary.com/hamstechonline-com/image/upload/f_auto,q_auto/v1615006126/hunar%20website%20images/Website_lkujhk.jpg");
            options.put("currency", "INR");
            options.put("amount", vAmount);

            JSONObject preFill = new JSONObject();
            //preFill.put("email", txtEmail.getText().toString().trim());
            preFill.put("email", m_strEmail);
            preFill.put("contact", mobile);

            options.put("prefill", preFill);

            //OnlineSuccessfulPopUp();
            co.open((HomePageActivity)context, options);
        } catch (Exception e) {
            Toast.makeText(context, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        try {
            tracking_id = razorpayPaymentID;
            PaymentSuccessAPi(tracking_id);
        } catch (Exception e) {
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            //Toast.makeText(this, "Payment error: "+"  " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    public void PaymentSuccessAPi(String tracking_id) {
        PaymentSuccessResponse paymentSuccessResponse = new PaymentSuccessResponse("Hamstech", context.getResources().getString(R.string.lblApiKey),
                userDataBase.getUserMobileNumber(1), String.valueOf(orderID), tracking_id);
        Call<PaymentSuccessResponse> call = apiService.getPaymentSuccess(paymentSuccessResponse);
        call.enqueue(new Callback<PaymentSuccessResponse>() {
            @Override
            public void onResponse(Call<PaymentSuccessResponse> call, retrofit2.Response<PaymentSuccessResponse> response) {
                if (response.body().getMesssage().equalsIgnoreCase("Payment updated successfully"))
                    //OnlineSuccessfulPopUp();
                    onlinePaymentSuccessful(context);
                else
                    Toast.makeText(context, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {

            }
        });
    }

    private void onlinePaymentSuccessful(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.payment_successful);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        TextView textAddress = dialog.findViewById(R.id.textAddress);
        TextView fillNow = dialog.findViewById(R.id.fillNow);

        fillNow.setVisibility(View.GONE);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePageActivity.class);
                dialog.dismiss();
                context.startActivity(intent);
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(context.getString(R.string.pay_message));
        int start = context.getString(R.string.pay_message).indexOf("message") + 7;
        spannableStringBuilder.setSpan(new ImageSpan(context, R.drawable.ic_message), start, start + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textMessage.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

        spannableStringBuilder = new SpannableStringBuilder(context.getString(R.string.pay_address));
        start = context.getString(R.string.pay_address).indexOf("home");
        spannableStringBuilder.setSpan(new ImageSpan(context, R.drawable.ic_home), start, start + 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textAddress.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

        dialog.show();
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
