package com.hamstechonline.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hamstechonline.R;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.LiveFashionWebview;
import com.hamstechonline.activities.MyCoursesLessonsPage;
import com.hamstechonline.activities.MyCoursesPageActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.PayinstallmentRequest;
import com.hamstechonline.datamodel.PaymentSuccessResponse;
import com.hamstechonline.datamodel.mycources.Lesson;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MyCoursesLessonsListAdapter extends RecyclerView.Adapter<MyCoursesLessonsListAdapter.ViewHolder> implements PaymentResultWithDataListener {

    Context context;
    private List<Lesson> lessonsArray;
    LogEventsActivity logEventsActivity;
    int listSize;
    String courseId,order_id,mobile = "",m_strEmail,tracking_id = "", CourseLog,ActivityLog,PagenameLog,LessonLog;
    int matchedPosition,lessonPositionId;
    boolean isExpand = false;
    ApiInterface apiService;
    UserDataBase userDataBase;
    List<Lesson> originalListArray;

    //public MyCoursesLessonsListAdapter(Context context, List<MyCourse> datamodels){
    public MyCoursesLessonsListAdapter(Context context, List<Lesson> lessonsArray,int listSize,String courseId, String CourseLog,
                                       int matchedPosition,boolean isExpand,String order_id,String m_strEmail,List<Lesson> originalListArray){
        this.context=context;
        this.lessonsArray = lessonsArray;
        this.originalListArray = originalListArray;
        logEventsActivity = new LogEventsActivity();
        this.listSize = listSize;
        this.courseId = courseId;
        this.CourseLog = CourseLog;
        this.order_id = order_id;
        this.m_strEmail = m_strEmail;
        this.matchedPosition = matchedPosition;
        this.isExpand = isExpand;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        userDataBase = new UserDataBase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.lessons_list_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {

            if (isExpand){
                if (matchedPosition <= position) {
                    holder.txtLessons.setText("Lessons "+lessonsArray.get(position).getOrderno());
                } else {
                    holder.txtLessons.setText("Lessons "+lessonsArray.get(position).getOrderno());
                }
            } else {
                holder.txtLessons.setText("Lessons "+lessonsArray.get(position).getOrderno());
            }

            if (lessonsArray.get(position).getLockValue().equalsIgnoreCase("0")) {
                holder.lessonLock.setVisibility(View.VISIBLE);
                holder.lessonCheckbox.setVisibility(View.GONE);
            } else if (lessonsArray.get(position).getLockValue().equalsIgnoreCase("1")) {
                holder.lessonLock.setVisibility(View.GONE);
                holder.lessonCheckbox.setVisibility(View.VISIBLE);
            }

            holder.txtLessonTitle.setText(lessonsArray.get(position).getLessonTitle());
            if (lessonsArray.get(position).getWatchedPercentage().equalsIgnoreCase("100")) {
                holder.lessonCheckbox.setBackground(context.getResources().getDrawable(R.drawable.ic_ticks_mutedblue));
            }

            holder.listLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (lessonsArray.get(position).getLockValue().equalsIgnoreCase("0")) {
                        //CourseLog = MyCourses page;
                        ActivityLog = "Pay your Instalment";
                        PagenameLog = "Lesson page";
                        LessonLog = lessonsArray.get(position).getLessonTitle();
                        getLogEvent(context);
                        lockPopup();
                    } else if (lessonsArray.get(position).getType().equalsIgnoreCase("nsdc_exam") ||
                            lessonsArray.get(position).getType().equalsIgnoreCase("live")){
                        String url = lessonsArray.get(position).getVideoUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                        context.startActivity(intent);
                    } else {
                        for (int i =0; i< originalListArray.size(); i++) {
                            if (originalListArray.get(i).getLessonId().equalsIgnoreCase(lessonsArray.get(position).getLessonId())) {
                                lessonPositionId = i;
                                ActivityLog = "Lesson click";
                                PagenameLog = "Lesson page";
                                LessonLog = originalListArray.get(lessonPositionId).getLessonTitle();
                                getLogEvent(context);
                                Intent intent = new Intent(context, MyCoursesLessonsPage.class);
                                intent.putExtra("videoURL", originalListArray.get(lessonPositionId).getVideoUrl());
                                intent.putExtra("CategoryName", originalListArray.get(lessonPositionId).getCourseTitle());
                                intent.putExtra("CategoryLog", "");
                                intent.putExtra("CourseName", originalListArray.get(lessonPositionId).getCourseTitle());
                                intent.putExtra("LessonName", originalListArray.get(lessonPositionId).getLessonTitle());
                                intent.putExtra("description", originalListArray.get(lessonPositionId).getLessonDescription());
                                intent.putExtra("pdfURL", originalListArray.get(lessonPositionId).getStudyMaterialUrl());
                                intent.putExtra("CourseId", courseId);
                                intent.putExtra("LessonId", originalListArray.get(lessonPositionId).getLessonId());
                                intent.putExtra("LessonImage", originalListArray.get(lessonPositionId).getLessonImageUrl());
                                intent.putExtra("LessonText", originalListArray.get(lessonPositionId).getLessonImageUrl());
                                //intent.putParcelableArrayListExtra("LessonData", (ArrayList<Lesson>) coursesList);
                                intent.putExtra("LessonData", (ArrayList<? extends Serializable>) originalListArray);
                                intent.putExtra("intNext", lessonPositionId);
                                context.startActivity(intent);
                            }
                        }
                    }

                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        //return lessonsArray.size();
        return listSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLessons,txtLessonTitle;
        RelativeLayout listLayout;
        CheckBox lessonCheckbox;
        ImageView lessonLock;

        public ViewHolder(@NonNull View view) {
            super(view);
            txtLessons = view.findViewById(R.id.txtLessons);
            listLayout = view.findViewById(R.id.listLayout);
            txtLessonTitle = view.findViewById(R.id.txtLessonTitle);
            lessonCheckbox = view.findViewById(R.id.lessonCheckbox);
            lessonLock = view.findViewById(R.id.lessonLock);
        }
    }

    public void lockPopup() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.lock_popup);
        dialog.setCancelable(true);

        TextView btnNext = dialog.findViewById(R.id.btnNext);
        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);

        try {
            mobile = userDataBase.getUserMobileNumber(1);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayInstallmentAPi(order_id);
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void PayInstallmentAPi(String order_id) {
        PayinstallmentRequest payinstallmentRequest = new PayinstallmentRequest(order_id,courseId);
        Call<PayinstallmentRequest> call = apiService.getPayinstallment(payinstallmentRequest);
        call.enqueue(new Callback<PayinstallmentRequest>() {
            @Override
            public void onResponse(Call<PayinstallmentRequest> call, retrofit2.Response<PayinstallmentRequest> response) {
                if (response.body().getRazorpayOrderId() != null) {
                    CourseLog = originalListArray.get(lessonPositionId).getCourseTitle();
                    ActivityLog = "Pay your Instalment";
                    PagenameLog = "MyCourses page";
                    LessonLog = "";
                    getLogEvent(context);
                    startPayment(response.body().getRazorpayOrderId(),response.body().getAmount());

                }
            }

            @Override
            public void onFailure(Call<PayinstallmentRequest> call, Throwable t) {

            }
        });
    }

    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname", UserDataConstants.userName);
            data.put("email", UserDataConstants.userMail);
            data.put("category","MyCourses page");
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startPayment(String razorpay_order_id,int InstallmentAmount) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Checkout co = new Checkout();

        try {
            /*try {
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", 12000); // amount in the smallest currency unit
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", "order_rcptid_11");

                Order order = razorpay.Orders.create(orderRequest);
            } catch (RazorpayException e) {
                // Handle Exception
                System.out.println(e.getMessage());
            }*/
            //finalAmount = 1;
            String vAmount = String.valueOf(InstallmentAmount);
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
            // options.put("invoice_number", "inv_KQyyZZXNQRMZxj");
            //options.put("receipt", "inv_KQyyZZXNQRMZxj");
            options.put("order_id", razorpay_order_id);
            //options.put("wallet", "0");

            //options.put("receipt", "txn_123456");
            //Order order = razorpayClient.Orders.create(options)

            JSONObject preFill = new JSONObject();
            if (UserDataConstants.userMail != null) {
                if (UserDataConstants.userMail.equalsIgnoreCase("")) {
                    preFill.put("email", "no-reply@hunarcourses.com");
                } else preFill.put("email", UserDataConstants.userMail);
            } else {
                preFill.put("email", "no-reply@hunarcourses.com");
            }

            preFill.put("email", m_strEmail);
            preFill.put("contact", mobile);
            //preFill.put("method", "upi");
            //preFill.put("vpa", "");

            options.put("prefill", preFill);

            //OnlineSuccessfulPopUp();
            co.open((MyCoursesPageActivity)context, options);
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
            //rezorOrderID = paymentData.getOrderId();
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
                mobile, order_id, tracking_id);
        Call<PaymentSuccessResponse> call = apiService.getPaymentSuccess(paymentSuccessResponse);
        call.enqueue(new Callback<PaymentSuccessResponse>() {
            @Override
            public void onResponse(Call<PaymentSuccessResponse> call, retrofit2.Response<PaymentSuccessResponse> response) {
                if (response.body().getMesssage().equalsIgnoreCase("Payment updated successfully"))
                    //OnlineSuccessfulPopUp();
                    OnlineSuccessfulPopUp();
                else
                    Toast.makeText(context, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PaymentSuccessResponse> call, Throwable t) {
                Toast.makeText(context, "Failed to update payment details", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void OnlineSuccessfulPopUp() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.successfull_layout);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);
        LinearLayout onlinePaymentLayout = dialog.findViewById(R.id.onlinePaymentLayout);
        LinearLayout cod_layout = dialog.findViewById(R.id.cod_layout);
        LinearLayout txt_ClickHere = dialog.findViewById(R.id.txt_ClickHere);

        onlinePaymentLayout.setVisibility(View.VISIBLE);
        cod_layout.setVisibility(View.GONE);
        txt_ClickHere.setVisibility(View.GONE);

        Glide.with(context)
                .asGif()
                .load(R.drawable.successfull_gif)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
//                    Intent intent = new Intent(EnrolNowActivity.this, HomePageActivity.class);
//                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });
    }
}
