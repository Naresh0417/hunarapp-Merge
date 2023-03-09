package com.hamstechonline.activities.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.hamstechonline.activities.MyCoursesPageActivity;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.BuzzDataModel;
import com.hamstechonline.datamodel.CommentsData;
import com.hamstechonline.datamodel.CommentsDataModel;
import com.hamstechonline.datamodel.Discussions;
import com.hamstechonline.datamodel.DiscussionsModel;
import com.hamstechonline.datamodel.HocTodayData;
import com.hamstechonline.datamodel.HocTodayResponse;
import com.hamstechonline.datamodel.SaveCommentRequest;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.LikesInterface;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.SharedPrefsUtils;
import com.hamstechonline.utils.UserDataConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionDetailsDialog {

    Context context;
    Dialog dialog;
    ImageView imageView;
    int position;
    ImageView imgHamstech, imgZoom,imgPlayButton;
    TextView txtTitle, txtDescription,txtExternalLink,likesCount,txtPost, txtUserName,
            imgLikeUnlike,txtUserNameChar,txtUserNamePost,txtAskDoubt;
    LinearLayout btnShare;
    RecyclerView listComments;
    RelativeLayout player_youtube;
    EditText userInputComment;
    LinearLayout inputCommentFields;
    TextView imgProfile;
    UserDataBase userDataBase;
    String langPref, term_id = "";
    List<Discussions> dataBuzz = new ArrayList<>();
    SharedPreferences prefs;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    String PagenameLog,activityLog = "",mp4URL = "",lessonEvent,ActivityLog,mobile,fullname,email,course_id;
    Bundle params;
    ApiInterface apiService;
    BuzzCommentsAdapter buzzCommentsAdapter;
    SharedPrefsUtils sharedPrefsUtils;
    Bundle bundle;
    BuzzDataModel buzzDataModel;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    List<Discussions> dataMainList;
    LikesInterface likesInterface;
    CommentReportDialoge reportDialoge;
    CircleImageView profile_image;

    public DiscussionDetailsDialog(Context context, int position, List<Discussions> dataMainList, String term_id,
                                   String course_id) {
        this.context = context;
        this.position = position;
        this.bundle = bundle;
        this.course_id = course_id;
        this.dataMainList = dataMainList;
        this.term_id = term_id;
        this.likesInterface = (LikesInterface) this.context;
    }

    public void showLoadingDialog() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(true);

        dialog.setContentView(R.layout.buzz_details_activity);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        imgHamstech = dialog.findViewById(R.id.imgHamstech);
        txtTitle = dialog.findViewById(R.id.txtTitle);
        txtDescription = dialog.findViewById(R.id.txtDescription);
        txtExternalLink = dialog.findViewById(R.id.txtExternalLink);
        imgLikeUnlike = dialog.findViewById(R.id.imgLikeUnlike);
        imgZoom = dialog.findViewById(R.id.imgZoom);
        imgPlayButton = dialog.findViewById(R.id.imgPlayButton);
        btnShare = dialog.findViewById(R.id.btnShare);
        likesCount = dialog.findViewById(R.id.likesCount);
        listComments = dialog.findViewById(R.id.listComments);
        userInputComment = dialog.findViewById(R.id.userInputComment);
        txtPost = dialog.findViewById(R.id.txtPost);
        inputCommentFields = dialog.findViewById(R.id.inputCommentFields);
        imgProfile = dialog.findViewById(R.id.imgProfile);
        txtUserName = dialog.findViewById(R.id.txtUserName);
        youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);
        player_youtube = dialog.findViewById(R.id.player_youtube);
        profile_image = dialog.findViewById(R.id.profile_image);
        txtUserNameChar = dialog.findViewById(R.id.txtUserNameChar);
        txtUserNamePost = dialog.findViewById(R.id.txtUserNamePost);
        txtAskDoubt = dialog.findViewById(R.id.txtAskDoubt);

        userDataBase = new UserDataBase(context);
        logEventsActivity = new LogEventsActivity();
        logger = AppEventsLogger.newLogger(context);
        params = new Bundle();

        prefs = context.getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        langPref = prefs.getString("Language", "en");
        sharedPrefsUtils = new SharedPrefsUtils(context, context.getString(R.string.app_name));

        try {
            mobile = userDataBase.getUserMobileNumber(1);
            fullname = userDataBase.getUserName(1);
            email = "";
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);

        getHocTodayList();

        txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInputComment.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Please enter valid input", Toast.LENGTH_SHORT).show();
                } else {
                    savePost(userInputComment.getText().toString().trim(), dataMainList.get(position).getPostid());
                }
            }
        });
        imgLikeUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLikeData(context,dataMainList.get(position).getPostid());
            }
        });
        if (inputCommentFields.getVisibility() == View.VISIBLE) {
            txtUserName.setText(UserDataConstants.userName);
            imgProfile.setText(""+userDataBase.getUserName(1).charAt(0));
        }

        txtExternalLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dataMainList.get(position).getExternallink().equalsIgnoreCase("")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(dataBuzz.get(position).getExternallink()));
                    context.startActivity(i);
                }
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonEvent = dataBuzz.get(position).getTitle();
                ActivityLog = "Share";
                PagenameLog = "Discussions Posts";
                getLogEvent(context);
                if (dataMainList.get(position).getVideourl().equals("")) {
                    //getLogEvent(context);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Text");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, dataMainList.get(position).getImage());
                    context.startActivity(Intent.createChooser(shareIntent, dataMainList.get(position).getTitle()));
                } else {
                    PagenameLog = "Url Share";
                    String url = "https://www.youtube.com/watch?v="+dataMainList.get(position).getVideourl();
                    //getLogEvent(context);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                    context.startActivity(Intent.createChooser(shareIntent, dataMainList.get(position).getTitle()));
                }
            }
        });

        dialog.show();
    }

    public void PlayerInitialize() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                if (player != null) player.loadVideo(mp4URL, 0);
            }
            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "Hunar Post";
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
    }

    public void getHocTodayList() {
        DiscussionsModel discussionsModel = new DiscussionsModel("Hamstech",context.getResources().getString(R.string.lblApiKey),course_id, langPref,mobile);
        Call<DiscussionsModel> call = apiService.getDiscussionsData(discussionsModel);
        call.enqueue(new Callback<DiscussionsModel>() {
            @Override
            public void onResponse(Call<DiscussionsModel> call, retrofit2.Response<DiscussionsModel> response) {
                if (response.body().getList() != null) {
                    dataBuzz.clear();
                    dataBuzz = response.body().getList();
                    txtTitle.setText(dataBuzz.get(position).getTitle());
                    lessonEvent = dataBuzz.get(position).getTitle();
                    txtDescription.setText(dataBuzz.get(position).getDescription());
                    if (!dataBuzz.get(position).getExternallink().isEmpty()) {
                        txtExternalLink.setText(dataBuzz.get(position).getExternallink());
                        txtExternalLink.setVisibility(View.VISIBLE);
                    }

                    if (!dataBuzz.get(position).getProfilePic().isEmpty()) {
                        Glide.with(context)
                                .load(dataBuzz.get(position).getProfilePic())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(profile_image);
                        profile_image.setVisibility(View.VISIBLE);
                        txtUserNamePost.setVisibility(View.VISIBLE);
                        txtUserNamePost.setText(dataBuzz.get(position).getName());
                    } else if (!dataBuzz.get(position).getName().isEmpty()) {
                        txtUserNamePost.setText(dataBuzz.get(position).getName());
                        profile_image.setVisibility(View.VISIBLE);
                        txtUserNamePost.setVisibility(View.VISIBLE);
                    } else if (!dataBuzz.get(position).getNameFirstCharacter().isEmpty()) {
                        txtUserNameChar.setText(dataBuzz.get(position).getNameFirstCharacter());
                        profile_image.setVisibility(View.GONE);
                        txtUserNameChar.setVisibility(View.VISIBLE);
                    }


                    if (!dataBuzz.get(position).getVideourl().equals("")) {
                        imgHamstech.setVisibility(View.GONE);
                        txtAskDoubt.setVisibility(View.GONE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.VISIBLE);
                        mp4URL = dataBuzz.get(position).getVideourl();
                        /*imgHamstech.setVisibility(View.VISIBLE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(dataBuzz.get(position).getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgHamstech);*/
                    } else if (!dataBuzz.get(position).getImage().isEmpty()) {
                        imgHamstech.setVisibility(View.VISIBLE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.GONE);
                        txtAskDoubt.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(dataBuzz.get(position).getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgHamstech);
                    } else if (!dataBuzz.get(position).getTitle().isEmpty()){
                        imgHamstech.setVisibility(View.GONE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.GONE);
                        txtAskDoubt.setVisibility(View.VISIBLE);
                        txtAskDoubt.setText(dataBuzz.get(position).getTitle());
                    } else {
                        imgHamstech.setVisibility(View.VISIBLE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.VISIBLE);
                        mp4URL = dataBuzz.get(position).getVideourl();
                        //if (player != null) player.loadVideo(mp4URL, 0);
                        //player.loadVideo(dataBuzz.get(position).getVideourl(),0);
                    }
                    if (dataBuzz.get(position).getLikedislike() == 1) {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                    }
                    if (dataBuzz.get(position).getComment() == 1) {
                        inputCommentFields.setVisibility(View.GONE);
                    } else {
                        inputCommentFields.setVisibility(View.VISIBLE);
                        txtUserName.setText(UserDataConstants.userName);
                        imgProfile.setText(""+userDataBase.getUserName(1).charAt(0));
                    }
                    likesCount.setText(dataBuzz.get(position).getLikes() +" "+context.getResources().getString(R.string.like)+" "+
                            dataBuzz.get(position).getComments() + " "+context.getResources().getString(R.string.comment));
                    getCommentsData(dataBuzz.get(position).getPostid());
                }
            }

            @Override
            public void onFailure(Call<DiscussionsModel> call, Throwable t) {

            }
        });
    }

    public void getCommentsData(int postId){
        CommentsDataModel commentsDataModel = new CommentsDataModel("Hamstech",context.getResources().getString(R.string.lblApiKey),postId,
                "discussions",UserDataConstants.userMobile);
        Call<CommentsDataModel> call = apiService.getCommentsData(commentsDataModel);
        call.enqueue(new Callback<CommentsDataModel>() {
            @Override
            public void onResponse(Call<CommentsDataModel> call, Response<CommentsDataModel> response) {
                if (response.body().getCommentsData() != null) {

                    buzzCommentsAdapter = new BuzzCommentsAdapter(context, response.body().getCommentsData());
                    listComments.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    listComments.setAdapter(buzzCommentsAdapter);
                    if (dataMainList.get(position).getVideourl().equals("")) {
                    } else {
                        PlayerInitialize();
                    }

                } else listComments.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<CommentsDataModel> call, Throwable t) {
                listComments.setVisibility(View.GONE);
            }
        });
    }

    public void savePost(String comment, int postId) {
        SaveCommentRequest saveCommentRequest = new SaveCommentRequest("Hamstech",context.getResources().getString(R.string.lblApiKey),
                postId,"discussions",comment,UserDataConstants.userMobile);
        Call<SaveCommentRequest> call = apiService.saveComment(saveCommentRequest);
        call.enqueue(new Callback<SaveCommentRequest>() {
            @Override
            public void onResponse(Call<SaveCommentRequest> call, Response<SaveCommentRequest> response) {
                ActivityLog = "Comment Posted";
                PagenameLog = "Hunar Posts";
                getLogEvent(context);
                userInputComment.setText("");
                userInputComment.setHint("Type your comment here...");
                inputCommentFields.setVisibility(View.GONE);
                uploadSuccessPopUp();
            }

            @Override
            public void onFailure(Call<SaveCommentRequest> call, Throwable t) {

            }
        });

    }

    public void uploadSuccessPopUp(){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.post_upload_successfull);
        dialog.setCancelable(false);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        ImageView progressBar = dialog.findViewById(R.id.progressBar);

        Glide.with(context)
                .load(R.drawable.ic_sucess_payment)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_sucess_payment)
                .into(progressBar);

        dialog.show();

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHocTodayList();
                dialog.dismiss();
            }
        });
    }


    public void getLikeData(final Context context,int postValue){

        RequestQueue queue = Volley.newRequestQueue(context);
        //hocLoadingDialog.showLoadingDialog();
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","buzz");
            params.put("apikey",context.getResources().getString(R.string.lblApiKey));
            params.put("postid",postValue);
            params.put("phone", mobile);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.save_like_dislike, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    //dataArrayList.clear();
                    //hocLoadingDialog.hideDialog();
                    //getBuzzList(BuzzActivity.this);
                    if (dataBuzz.get(position).getLikedislike() == 1) {
                        /*lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "UnLike";
                        PagenameLog = "Hunar Posts";
                        getLogEvent(context);
                        dataBuzz.get(position).setLikedislike(0);
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                        likesCount.setText((Integer.parseInt(dataBuzz.get(position).getLikes())) + " Likes " +
                                +dataBuzz.get(position).getComments()+" "+dataBuzz.get(position).getComments() + " Comments");*/
                    } else {
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Like";
                        PagenameLog = "Hunar Posts";
                        getLogEvent(context);
                        dataBuzz.get(position).setLikedislike(1);
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                        likesCount.setText((Integer.parseInt(dataBuzz.get(position).getLikes())) + " Likes " +
                                +dataBuzz.get(position).getComments()+" "+dataBuzz.get(position).getComments() + " Comments");

                    }
                    likesInterface.setIsLiked(1,position);
                    getHocTodayList();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
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
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                    return mRequestBody.getBytes();
                }
            }

        };
        queue.add(sr);
    }

    public class BuzzCommentsAdapter extends RecyclerView.Adapter<BuzzCommentsAdapter.ViewHolder> {

        Context context;
        List<CommentsData> dataBuzz;

        public BuzzCommentsAdapter(Context context,List<CommentsData> dataBuzz){
            this.context = context;
            this.dataBuzz = dataBuzz;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.buzz_comments_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            try {
                holder.txtUserName.setText(dataBuzz.get(position).getName());
                holder.txtComment.setText(dataBuzz.get(position).getComment());
                holder.imgProfile.setText(dataBuzz.get(position).getProfilepic());


                Glide.with(context)
                        .load(R.drawable.ic_vertical_dots)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.imgReport);
                holder.imgReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reportDialoge = new CommentReportDialoge(context,Integer.parseInt(dataBuzz.get(position).getComment_id()),mobile);
                        reportDialoge.showLoadingDialog();
                    }
                });

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dataBuzz.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtUserName, txtComment, imgProfile;
            ImageView imgReport;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgProfile = view.findViewById(R.id.imgProfile);
                txtUserName = view.findViewById(R.id.txtUserName);
                txtComment = view.findViewById(R.id.txtComment);
                imgReport = view.findViewById(R.id.imgReport);
            }

        }

    }
    public void getLogEvent(Context context){
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", mobile);
            data.put("fullname",fullname);
            data.put("email",UserDataConstants.userMail);
            data.put("category","");
            data.put("course","");
            data.put("lesson",lessonEvent);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
