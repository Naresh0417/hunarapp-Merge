package com.hamstechonline.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hamstechonline.R;
import com.hamstechonline.activities.AboutUsActivity;
import com.hamstechonline.activities.BuzzActivity;
import com.hamstechonline.activities.ContactActivity;
import com.hamstechonline.activities.DynamicLinkingActivity;
import com.hamstechonline.activities.EditProfileActivity;
import com.hamstechonline.activities.EnrolNowActivity;
import com.hamstechonline.activities.HamstechTodayActivity;
import com.hamstechonline.activities.HomePageActivity;
import com.hamstechonline.activities.LessonsPageActivity;
import com.hamstechonline.activities.LessonsPageNotifications;
import com.hamstechonline.activities.LiveFashionWebview;
import com.hamstechonline.activities.MiniLessonsEnrolNowActivity;
import com.hamstechonline.activities.NSDCPageActivity;
import com.hamstechonline.activities.NotificationsActivity;
import com.hamstechonline.activities.SplashScreenActivity;
import com.hamstechonline.activities.SuccessStoryActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.internal.push.PushManager;
import com.moengage.firebase.MoEFireBaseHelper;
import com.moengage.pushbase.MoEPushHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Intent intent;
    Bitmap image;
    String imageLarge,activityLog,PagenameLog,notiTitle,mobile,fullname;
    NotificationCompat.Builder notificationBuilder;
    NotificationCompat.Builder mBuilder;
    LogEventsActivity logEventsActivity;
    UserDataBase userDataBase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        logEventsActivity = new LogEventsActivity();
        activityLog = "Notification";mobile = "";fullname = "";
        userDataBase = new UserDataBase(this);
        if (remoteMessage.getData().size() > 0) {

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }
        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
        } catch (CursorIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }

        sendNotification(remoteMessage);
    }

    @Override
    public void onNewToken(String token) {

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(RemoteMessage messageBody) {
        String courseId = ""+messageBody.getData().get("notificationID");
        notiTitle = messageBody.getData().get("title");



        if (messageBody.getData().get("status").equals("notification")){
            intent = new Intent(this, NotificationsActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notiTitle",notiTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("hoc")){
            intent = new Intent(this, BuzzActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notifiTitle",messageBody.getData().get("title"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (messageBody.getData().get("status").equals("home_page")){//click on notification
            intent = new Intent(this, HomePageActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            UserDataConstants.notificationID = messageBody.getData().get("notificationID");
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("enroll_page")){
            intent = new Intent(this, EnrolNowActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("contactus_page")){
            intent = new Intent(this, ContactActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("aboutus_page")){
            intent = new Intent(this, AboutUsActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("courses_page")
            || messageBody.getData().get("status").equals("mini_lessons_course_page")
            ||messageBody.getData().get("status").equals("my-courses")){
            UserDataConstants.notificationID = ""+messageBody.getData().get("notificationID");
            UserDataConstants.courseId = ""+messageBody.getData().get("notificationID");
            intent = new Intent(this, DynamicLinkingActivity.class);
            notiTitle = messageBody.getData().get("title");
            intent.putExtra("notificationId",""+messageBody.getData().get("notificationID"));
            intent.putExtra("status",""+messageBody.getData().get("status"));
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("mini_lessons_enroll_page")){
            UserDataConstants.notificationID = ""+messageBody.getData().get("notificationID");
            UserDataConstants.courseId = ""+messageBody.getData().get("notificationID");
            intent = new Intent(this, DynamicLinkingActivity.class);
            notiTitle = messageBody.getData().get("title");
            intent.putExtra("notificationId",""+messageBody.getData().get("notificationID"));
            intent.putExtra("status",""+messageBody.getData().get("status"));
            intent.putExtra("notificationTtitle",""+messageBody.getData().get("title"));
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("lessons-page")){
            UserDataConstants.notificationID = ""+messageBody.getData().get("notificationID");
            intent = new Intent(this, LessonsPageNotifications.class);
            notiTitle = messageBody.getData().get("title");
            intent.putExtra("lessonId",Integer.parseInt(""+messageBody.getData().get("notificationID").toString()));
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("nsdc-page-title")){
            intent = new Intent(this, NSDCPageActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("success_stories")){
            intent = new Intent(this, SuccessStoryActivity.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("chat_page")){
            Intent myIntent = new Intent(Intent.ACTION_VIEW);
            String url = "";
            try {
                url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                        URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            myIntent.setPackage("com.whatsapp");
            myIntent.setData(Uri.parse(url));
            startActivity(myIntent);
            notiTitle = messageBody.getData().get("title");
            PagenameLog = "chat with whatsapp";
            getLogEvent(MyFirebaseMessagingService.this);
            new AppsFlyerEventsHelper(this).EventNotification(messageBody.getData().get("title"));
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("chat_with_whats_app")){
            notiTitle = messageBody.getData().get("title");
            PagenameLog = "Chat with Whatsapp";
            new AppsFlyerEventsHelper(this).EventNotification(messageBody.getData().get("title"));
            getLogEvent(MyFirebaseMessagingService.this);
            intent = new Intent(Intent.ACTION_VIEW);
            String url = "";
            try {
                url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                        URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            startActivity(intent);
            notiTitle = messageBody.getData().get("title");
            PagenameLog = "chat with whatsapp";
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("course-clarification-call")){
            notiTitle = messageBody.getData().get("title");
            PagenameLog = "Chat with Whatsapp";
            new AppsFlyerEventsHelper(this).EventNotification(messageBody.getData().get("title"));
            getLogEvent(MyFirebaseMessagingService.this);
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+916281109108"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("profile_page")){
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            PagenameLog = "Profile Page";
            getLogEvent(MyFirebaseMessagingService.this);
            intent = new Intent(this, EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("ratings")){
            notiTitle = messageBody.getData().get("title");
            intent = new Intent(this, HomePageActivity.class);
            UserDataConstants.notificationTitle = messageBody.getData().get("title");
            UserDataConstants.notificationID = messageBody.getData().get("notificationID");
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("isRatings","forRating");
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else if (messageBody.getData().get("status").equals("live-digital-fashion-show")){
            intent = new Intent(this, LiveFashionWebview.class);
            notiTitle = messageBody.getData().get("title");
            UserDataConstants.notificationTitle = notiTitle;
            new AppsFlyerEventsHelper(this).EventNotification(UserDataConstants.notificationTitle);
            intent.putExtra("notificationId",messageBody.getData().get("notificationID"));
            intent.putExtra("notiTitle",notiTitle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent = new Intent(this, SplashScreenActivity.class);
            notiTitle = messageBody.getData().get("title");
            PagenameLog = "Push Notification status issue";
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        String channelId = "Hamstech";
        imageLarge = messageBody.getData().get("image");
        try {
            URL url = new URL(messageBody.getData().get("image"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hamstech";
            String description = "Hunar Online";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, 0);
        if (imageLarge == null || imageLarge.equals("false")){
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.generic_notification)
                    .setContentTitle(messageBody.getData().get("title"))
                    .setContentText(messageBody.getData().get("body"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody.getData().get("body")))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.generic_notification)
                    .setContentTitle(messageBody.getData().get("title"))
                    .setContentText(messageBody.getData().get("body"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image).setSummaryText(messageBody.getData().get("body")))
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify((int) System.currentTimeMillis() /* ID of notification */, notificationBuilder.build());
    }
    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname", fullname);
            data.put("email","");
            data.put("category","");
            data.put("course","");
            data.put("lesson",notiTitle);
            data.put("activity",activityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
