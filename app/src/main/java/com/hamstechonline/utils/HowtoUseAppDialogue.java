package com.hamstechonline.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.hamstechonline.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

public class HowtoUseAppDialogue {

    Context context;
    Dialog dialog;
    YouTubePlayerView youTubePlayerView;
    TextView txtTitle, txtDescription;
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog;
    DisplayMetrics displayMetrics;
    YouTubePlayer player;

    public HowtoUseAppDialogue(Context context){
        this.context = context;
    }


    public void showLoadingDialog() {

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.video_popup);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtDescription = dialog.findViewById(R.id.txtDescription);
        logEventsActivity = new LogEventsActivity();
        youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //getLifecycle().addObserver(youTubePlayerView);

        txtTitle.setText(UserDataConstants.howtouseTitle);
        txtDescription.setText(UserDataConstants.howtouseDesc);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                String videoId = UserDataConstants.videoUrl;
                //String videoId = "S0Q4gqBUs7c";
                player.loadVideo(videoId, 0);
                player.play();
            }
            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "How to use app";
                if (state.toString().equals("PLAYING")){
                    PagenameLog = "Video start";
                    getLogEvent(context);
                } else if (state.toString().equals("PAUSED")){
                    PagenameLog = "Video paused";
                    getLogEvent(context);
                } else if (state.toString().equals("STOPPED")){
                    PagenameLog = "Video stopped";
                    getLogEvent(context);
                }
            }

        });

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
    }
    public void hideDialog(){
        dialog.dismiss();
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
            data.put("course","");
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
