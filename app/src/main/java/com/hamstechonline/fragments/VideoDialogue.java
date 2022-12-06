package com.hamstechonline.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hamstechonline.R;
import com.hamstechonline.activities.CoursePageActivity;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoDialogue {

    Context context;
    Dialog dialog;
    YouTubePlayerView youTubePlayerView;
    LogEventsActivity logEventsActivity;
    String ActivityLog,PagenameLog;
    String videoURL;

    public VideoDialogue(Context context, String videoURL){
        this.context = context;
        this.videoURL = videoURL;
    }


    public void showLoadingDialog() {

        dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.video_dialogue);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        logEventsActivity = new LogEventsActivity();
        youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);
        //getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if (!videoURL.isEmpty()) {
                    ActivityLog = "Lessons Page";
                    //String videoId = "S0Q4gqBUs7c";
                    youTubePlayer.loadVideo(videoURL, 0);
                }

            }
            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                if (state.toString().equals("PLAYING")){
                    PagenameLog = "Video start";
                    getLogEvent(context);
                } else if (state.toString().equals("PAUSED")){
                    PagenameLog = "Video paused";
                    getLogEvent(context);
                } else if (state.toString().equals("STOPPED")){
                    PagenameLog = "Video stopped";
                    getLogEvent(context);
                } else if (state.toString().equals("ENDED")){
                    PagenameLog = "Video Ended";
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
