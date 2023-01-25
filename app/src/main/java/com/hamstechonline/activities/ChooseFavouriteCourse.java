package com.hamstechonline.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.adapters.MyCoursePagerAdapter;
import com.hamstechonline.adapters.StoriesSliderCardPagerAdapter;
import com.hamstechonline.database.UserDataBase;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.HocResponse;
import com.hamstechonline.datamodel.favourite.FavouriteCourse;
import com.hamstechonline.datamodel.favourite.FavouriteResponse;
import com.hamstechonline.datamodel.homepage.HomepageResponse;
import com.hamstechonline.fragments.FooterNavigationPaid;
import com.hamstechonline.fragments.FooterNavigationUnPaid;
import com.hamstechonline.restapi.ApiClient;
import com.hamstechonline.restapi.ApiInterface;
import com.hamstechonline.utils.ApiConstants;
import com.hamstechonline.utils.GridSpacingItemDecoration;
import com.hamstechonline.utils.HocLoadingDialog;
import com.hamstechonline.utils.UserDataConstants;
import com.moengage.core.DataCenter;
import com.moengage.core.LogLevel;
import com.moengage.core.MoEngage;
import com.moengage.core.config.LogConfig;
import com.moengage.core.config.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class ChooseFavouriteCourse extends AppCompatActivity {

    RecyclerView listGrid;
    ChooseTopicsAdapter chooseTopicsAdapter;
    ArrayList<CategoryDatamodel> coursesList = new ArrayList<>();
    LinearLayout linearList;
    List<String> positions = new ArrayList<>();
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language",footerMenuStatus,typeCat = "";
    SharedPreferences prefs,footerStatus,catType;
    ApiInterface apiService;
    UserDataBase userDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.choose_favourite_courses);

        listGrid = findViewById(R.id.listGrid);
        linearList = findViewById(R.id.linearList);

        hocLoadingDialog = new HocLoadingDialog(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        langPref = prefs.getString("Language", "");
        footerStatus = getSharedPreferences("footerStatus", Activity.MODE_PRIVATE);
        catType = getSharedPreferences("Category", Activity.MODE_PRIVATE);
        footerMenuStatus = footerStatus.getString("footerStatus", "unpaid");

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        userDataBase = new UserDataBase(this);
        getResponse();
        getCategories();

    }
    public void Homepage(View view) {
        Intent intent = new Intent(ChooseFavouriteCourse.this, HomePageActivity.class);
        startActivity(intent);
        /*if (positions.size() != 0) {
            setRecommendedTopics(ChooseFavouriteCourse.this);
        } else Toast.makeText(this, "Please select at lease one course", Toast.LENGTH_SHORT).show();*/
    }

    public void getResponse() {
        hocLoadingDialog.showLoadingDialog();
        HomepageResponse homepageResponse = new HomepageResponse("Hamstech","category",
                getResources().getString(R.string.lblApiKey),langPref,userDataBase.getUserMobileNumber(1),typeCat);
        Call<HomepageResponse> call = apiService.getHomepageResponse(homepageResponse);
        call.enqueue(new Callback<HomepageResponse>() {
            @Override
            public void onResponse(Call<HomepageResponse> call, retrofit2.Response<HomepageResponse> response) {
                hocLoadingDialog.hideDialog();
                if (response.body().getEnglish() != null) {

                    /*subListAdapter = new SubListAdapter(HomePageActivity.this,catListHindi);
                    listCategory.setLayoutManager(new GridLayoutManager(HomePageActivity.this, 2));
                    listCategory.addItemDecoration(new GridSpacingItemDecoration(2,30,false));
                    listCategory.setAdapter(subListAdapter);*/

                    if (response.body().getMyCourses().size() > 0) {
                        footerMenuStatus = "paid";
                        SharedPreferences.Editor editor = footerStatus.edit();
                        editor.putString("footerStatus", "paid");
                        editor.commit();

                    } else {
                        SharedPreferences.Editor editor = footerStatus.edit();
                        editor.putString("footerStatus", "unpaid");
                        editor.commit();

                    }
                }

            }

            @Override
            public void onFailure(Call<HomepageResponse> call, Throwable t) {

            }
        });
    }


    public void getCategories() {
        FavouriteResponse hocResponse = new FavouriteResponse("Hamstech", getResources().getString(R.string.lblApiKey),userDataBase.getUserMobileNumber(1), langPref);
        Call<FavouriteResponse> call = apiService.getFavouriteResponse(hocResponse);
        call.enqueue(new Callback<FavouriteResponse>() {
            @Override
            public void onResponse(Call<FavouriteResponse> call, retrofit2.Response<FavouriteResponse> response) {
                if (response.body().getCourses() != null) {
                    chooseTopicsAdapter = new ChooseTopicsAdapter(ChooseFavouriteCourse.this,response.body().getCourses());
                    listGrid.setLayoutManager(new LinearLayoutManager(ChooseFavouriteCourse.this, RecyclerView.VERTICAL, false));
                    listGrid.setAdapter(chooseTopicsAdapter);
                }
            }

            @Override
            public void onFailure(Call<FavouriteResponse> call, Throwable t) {

            }
        });
    }
    public class ChooseTopicsAdapter extends RecyclerView.Adapter<ChooseTopicsAdapter.ViewHolder> {

        Context context;
        List<FavouriteCourse> coursesList = new ArrayList<>();

        public ChooseTopicsAdapter(Context context,List<FavouriteCourse> coursesList){
            this.context=context;
            this.coursesList = coursesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.choose_favourite_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            try {
                //holder.cardMainLayout.setCardBackgroundColor(context.getResources.getColor(R.color.transparent));
                holder.txtDescription.setText(coursesList.get(position).getDescription());
                holder.txtTitle.setText(coursesList.get(position).getTitle());
                Glide.with(context)
                        .load(coursesList.get(position).getImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imgCategory);
                holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = catType.edit();
                        editor.putString("Category", coursesList.get(position).getType());
                        editor.commit();
                        Intent intent = new Intent(ChooseFavouriteCourse.this,HomePageActivity.class);
                        intent.putExtra("Category",coursesList.get(position).getType());
                        startActivity(intent);
                        //ChooseFavouriteCourse.this.finish();
                        //saveLocale(coursesList.get(position).getType());
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return coursesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            CheckBox itemCheck;
            TextView txtDescription, txtTitle;
            RelativeLayout cardMainLayout;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.imgCategory);
                itemCheck = view.findViewById(R.id.itemCheck);
                txtDescription = view.findViewById(R.id.txtDescription);
                txtTitle = view.findViewById(R.id.txtTitle);
                cardMainLayout = view.findViewById(R.id.cardMainLayout);
            }
        }
    }

    public void choosePopup(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.choose_popup);
        dialog.setCancelable(true);

        dialog.show();
    }

    public void saveLocale(String course){

        if (typeCat.equalsIgnoreCase("")) {
            SharedPreferences.Editor editor = catType.edit();
            editor.putString("Category", course);
            editor.commit();
        }

        Intent intent = new Intent(ChooseFavouriteCourse.this,HomePageActivity.class);
        startActivity(intent);
        ChooseFavouriteCourse.this.finish();
    }

    public void setRecommendedTopics(Context context){

        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        hocLoadingDialog.showLoadingDialog();
        try {
            params.put("appname","Hamstech");
            params.put("page","course");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("topicId",positions.toString().substring(1, (positions.toString().length())-1));
            params.put("mobile", UserDataConstants.userMobile);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody = metaData.toString();

        StringRequest sr = new StringRequest(Request.Method.POST, ApiConstants.setRecommendedTopics, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    if (jo.getString("status").equals("ok")){
                        hocLoadingDialog.hideDialog();
                        Intent intent = new Intent(ChooseFavouriteCourse.this, HomePageActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChooseFavouriteCourse.this, "Invalid Request", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
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
                    Toast.makeText(ChooseFavouriteCourse.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        };

        queue.add(sr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finishAffinity();
    }
}
