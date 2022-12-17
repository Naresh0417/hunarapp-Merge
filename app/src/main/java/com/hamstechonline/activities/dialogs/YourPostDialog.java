package com.hamstechonline.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.hamstechonline.activities.CoursePageActivity;
import com.hamstechonline.adapters.UserReplyCommentDataAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.BuzzDataModel;
import com.hamstechonline.datamodel.CommentsData;
import com.hamstechonline.datamodel.CommentsDataModel;
import com.hamstechonline.datamodel.HocTodayData;
import com.hamstechonline.datamodel.HocTodayResponse;
import com.hamstechonline.datamodel.SaveCommentReply;
import com.hamstechonline.datamodel.SaveCommentRequest;
import com.hamstechonline.datamodel.UserReplyCommentData;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourPostDialog {

    Context context;
    Dialog dialog;
    ImageView imageView;
    int position;
    ImageView imgHamstech, imgZoom,imgPlayButton;
    TextView txtTitle, txtDescription,likesCount,txtPost, txtUserName, imgLikeUnlike;
    LinearLayout btnShare;
    RecyclerView listComments;
    RelativeLayout player_youtube;
    EditText userInputComment;
    LinearLayout inputCommentFields;
    TextView imgProfile;
    UserDataBase userDataBase;
    String langPref, term_id = "";
    List<HocTodayData> dataBuzz = new ArrayList<>();
    SharedPreferences prefs;
    LogEventsActivity logEventsActivity;
    AppEventsLogger logger;
    String PagenameLog,activityLog = "",mp4URL = "",lessonEvent,ActivityLog,mobile,fullname,email;
    Bundle params;
    ApiInterface apiService;
    BuzzCommentsAdapter buzzCommentsAdapter;
    SharedPrefsUtils sharedPrefsUtils;
    Bundle bundle;
    BuzzDataModel buzzDataModel;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    List<HocTodayData> dataMainList;
    LikesInterface likesInterface;
    CommentReportDialoge reportDialoge;

    public YourPostDialog(Context context, int position, List<HocTodayData> dataMainList, String term_id) {
        this.context = context;
        this.position = position;
        this.bundle = bundle;
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

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                getHocTodayList();
            }
            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                ActivityLog = "Your Post";
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
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lessonEvent = dataBuzz.get(position).getTitle();
                ActivityLog = "Share";
                PagenameLog = "My Post";
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

    public void getHocTodayList() {
        HocTodayResponse hocTodayResponse = new HocTodayResponse("Hamstech",context.getResources().getString(R.string.lblApiKey),
                UserDataConstants.userMobile,langPref,"userposts",String.valueOf(term_id));
        Call<HocTodayResponse> call = apiService.getHocTodayResponse(hocTodayResponse);
        call.enqueue(new Callback<HocTodayResponse>() {
            @Override
            public void onResponse(Call<HocTodayResponse> call, Response<HocTodayResponse> response) {
                if (response.body().getHocTodayData() != null) {
                    dataBuzz.clear();
                    dataBuzz = response.body().getHocTodayData();
                    txtTitle.setText(dataBuzz.get(position).getTitle());
                    lessonEvent = dataBuzz.get(position).getTitle();
                    txtDescription.setText(dataBuzz.get(position).getDescription());
                    if (dataBuzz.get(position).getVideourl().equals("")) {
                        imgHamstech.setVisibility(View.VISIBLE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.GONE);
                        Glide.with(context)
                                .load(dataBuzz.get(position).getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgHamstech);
                    } else {
                        imgHamstech.setVisibility(View.VISIBLE);
                        imgPlayButton.setVisibility(View.GONE);
                        player_youtube.setVisibility(View.VISIBLE);
                        mp4URL = dataBuzz.get(position).getVideourl();
                        player.loadVideo(mp4URL, 0);
                        //player.loadVideo(dataBuzz.get(position).getVideourl(),0);
                    }
                    if (dataBuzz.get(position).getLikedislike() == 1) {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    } else {
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                    }
                    if (dataBuzz.get(position).getComment() == 1) {
                        inputCommentFields.setVisibility(View.VISIBLE);
                        txtUserName.setText(UserDataConstants.userName);
                        imgProfile.setText(""+userDataBase.getUserName(1).charAt(0));
                    } else {
                        inputCommentFields.setVisibility(View.VISIBLE);
                        txtUserName.setText(UserDataConstants.userName);
                        imgProfile.setText(""+userDataBase.getUserName(1).charAt(0));
                    }
                    likesCount.setText(dataBuzz.get(position).getLikes() + " " +context.getResources().getString(R.string.like)+
                            " "+context.getResources().getString(R.string.comment));
                    getCommentsData(dataBuzz.get(position).getPostid());
                }
            }

            @Override
            public void onFailure(Call<HocTodayResponse> call, Throwable t) {

            }
        });
    }

    public void getCommentsData(int postId){
        CommentsDataModel commentsDataModel = new CommentsDataModel("Hamstech",context.getResources().getString(R.string.lblApiKey),postId,
                "buzz",UserDataConstants.userMobile);
        Call<CommentsDataModel> call = apiService.getCommentsData(commentsDataModel);
        call.enqueue(new Callback<CommentsDataModel>() {
            @Override
            public void onResponse(Call<CommentsDataModel> call, Response<CommentsDataModel> response) {
                if (response.body().getCommentsData() != null) {
                    buzzCommentsAdapter = new BuzzCommentsAdapter(context, response.body().getCommentsData());
                    listComments.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                    listComments.setAdapter(buzzCommentsAdapter);
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
                postId,"buzz",comment,UserDataConstants.userMobile);
        Call<SaveCommentRequest> call = apiService.saveComment(saveCommentRequest);
        call.enqueue(new Callback<SaveCommentRequest>() {
            @Override
            public void onResponse(Call<SaveCommentRequest> call, Response<SaveCommentRequest> response) {
                ActivityLog = "Comment Posted";
                PagenameLog = "My Post";
                getLogEvent(context);
                userInputComment.setText("");
                userInputComment.setHint("Type your comment here...");
                inputCommentFields.setVisibility(View.VISIBLE);
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
                        PagenameLog = "My Post";
                        getLogEvent(context);
                        dataBuzz.get(position).setLikedislike(0);
                        imgLikeUnlike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unlike, 0, 0, 0);
                        likesCount.setText((Integer.parseInt(dataBuzz.get(position).getLikes())) + " Likes " +
                                +dataBuzz.get(position).getComments()+" "+dataBuzz.get(position).getComments() + " Comments");*/
                    } else {
                        lessonEvent = dataBuzz.get(position).getTitle();
                        ActivityLog = "Like";
                        PagenameLog = "My Post";
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
        UserReplyCommentDataAdapter userReplyCommentDataAdapter;

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
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                holder.txtUserName.setText(dataBuzz.get(position).getName());
                holder.txtComment.setText(dataBuzz.get(position).getComment());
                holder.imgProfile.setText(dataBuzz.get(position).getProfilepic());
                holder.txtCommentReply.setVisibility(View.GONE);

                Glide.with(context)
                        .load(R.drawable.ic_vertical_dots)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.imgReport);

                holder.txtCommentReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.inputCommentFields.setVisibility(View.VISIBLE);
                    }
                });

                holder.txtPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.inputCommentFields.setVisibility(View.GONE);
                        SaveCommentReply saveCommentReply = new SaveCommentReply("Hamstech",context.getResources().getString(R.string.lblApiKey),
                                "discussions",holder.userInputComment.getText().toString().trim(),dataBuzz.get(position).getComment_id(),mobile);

                        Call<SaveCommentReply> saveCommentReplyCall = apiService.saveCommentReply(saveCommentReply);

                        saveCommentReplyCall.enqueue(new Callback<SaveCommentReply>() {
                            @Override
                            public void onResponse(Call<SaveCommentReply> call, Response<SaveCommentReply> response) {
                                Toast.makeText(context, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();

                                UserReplyCommentData userReplyCommentData = new UserReplyCommentData("Hamstech",context.getResources().getString(R.string.lblApiKey),
                                        "discussions",dataBuzz.get(position).getComment_id(),mobile);
                                Call<UserReplyCommentData> userReplyCommentDataCall = apiService.getUserCommentReplyData(userReplyCommentData);
                                userReplyCommentDataCall.enqueue(new Callback<UserReplyCommentData>() {
                                    @Override
                                    public void onResponse(Call<UserReplyCommentData> call, Response<UserReplyCommentData> response) {
                                        if (response.body().getReplies() != null) {
                                            userReplyCommentDataAdapter = new UserReplyCommentDataAdapter(context, response.body().getReplies());
                                            listComments.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                            listComments.setAdapter(userReplyCommentDataAdapter);
                                        } else listComments.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<UserReplyCommentData> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<SaveCommentReply> call, Throwable t) {

                            }
                        });
                    }
                });

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
            TextView txtUserName, txtComment, imgProfile,txtCommentReply,txtPost;
            RelativeLayout inputCommentFields;
            ImageView imgReport;
            EditText userInputComment;
            RecyclerView listComments;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgProfile = view.findViewById(R.id.imgProfile);
                txtUserName = view.findViewById(R.id.txtUserName);
                txtComment = view.findViewById(R.id.txtComment);
                txtCommentReply = view.findViewById(R.id.txtCommentReply);
                inputCommentFields = view.findViewById(R.id.inputCommentFields);
                txtPost = view.findViewById(R.id.txtPost);
                userInputComment = view.findViewById(R.id.userInputComment);
                listComments = view.findViewById(R.id.listComments);
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
