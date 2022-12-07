package com.hamstechonline.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hamstechonline.R;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.datamodel.GetChatNumber;
import com.hamstechonline.datamodel.MyCourseGetChatNumber;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DynamicWhatsAppChat {

    Context context;
    ApiInterface apiService;
    LogEventsActivity logEventsActivity;
    String CourseLog,LessonLog,ActivityLog,PagenameLog;

    public DynamicWhatsAppChat(Context context,String PagenameLog,String CourseLog, String LessonLog) {
        this.context = context;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        logEventsActivity = new LogEventsActivity();
        this.PagenameLog = PagenameLog;
        this.CourseLog = CourseLog;
        this.LessonLog = LessonLog;
    }

    public void getChatNumber(String phone){
        GetChatNumber getChatNumber = new GetChatNumber("Hamstech",context.getResources().getString(R.string.lblApiKey),phone);
        Call<GetChatNumber> call = apiService.getChatNumber(getChatNumber);

        call.enqueue(new Callback<GetChatNumber>() {
            @Override
            public void onResponse(Call<GetChatNumber> call, Response<GetChatNumber> response) {
                if (!PagenameLog.equalsIgnoreCase(""))getLogEvent(context);
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone="+ response.body().getWaba() +"&text=" +
                                URLEncoder.encode(context.getResources().getString(R.string.whatsAppmsg), "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChatNumber> call, Throwable t) {

            }
        });
    }

    public void getEnrollChatNumber(String phone){
        GetChatNumber getChatNumber = new GetChatNumber("Hamstech",context.getResources().getString(R.string.lblApiKey),phone);
        Call<GetChatNumber> call = apiService.getEnrolChatNumber(getChatNumber);

        call.enqueue(new Callback<GetChatNumber>() {
            @Override
            public void onResponse(Call<GetChatNumber> call, Response<GetChatNumber> response) {
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone="+ response.body().getWaba() +"&text=" +
                                URLEncoder.encode(context.getResources().getString(R.string.whatsAppmsg), "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChatNumber> call, Throwable t) {

            }
        });
    }

    public void getMyCourseChatNumber(String phone,String course_id){
        MyCourseGetChatNumber myCourseGetChatNumber = new MyCourseGetChatNumber("Hamstech",context.getResources().getString(R.string.lblApiKey),
                phone,course_id);
        Call<MyCourseGetChatNumber> call = apiService.getMyCourseChatNumber(myCourseGetChatNumber);

        call.enqueue(new Callback<MyCourseGetChatNumber>() {
            @Override
            public void onResponse(Call<MyCourseGetChatNumber> call, Response<MyCourseGetChatNumber> response) {
                if (response.body().getStatus().equalsIgnoreCase("ok")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    try {
                        String url = "https://api.whatsapp.com/send?phone="+ response.body().getWaba() +"&text=" +
                                URLEncoder.encode(context.getResources().getString(R.string.whatsAppmsg), "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyCourseGetChatNumber> call, Throwable t) {

            }
        });
    }

    public void getLogEvent(Context context){
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile",UserDataConstants.userMobile);
            data.put("fullname",UserDataConstants.userName);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity","Sticky whatsapp");
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
