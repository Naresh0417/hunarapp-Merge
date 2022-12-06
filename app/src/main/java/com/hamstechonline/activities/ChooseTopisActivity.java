package com.hamstechonline.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;
import android.widget.Toast;

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
import com.hamstechonline.datamodel.CategoryDatamodel;
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

public class ChooseTopisActivity extends AppCompatActivity {

    RecyclerView listGrid;
    ChooseTopicsAdapter chooseTopicsAdapter;
    ArrayList<CategoryDatamodel> coursesList = new ArrayList<>();
    LinearLayout linearList;
    List<String> positions = new ArrayList<>();
    HocLoadingDialog hocLoadingDialog;
    String langPref = "Language";
    SharedPreferences prefs;
    ImageButton stickyWhatsApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.choose_topics);

        listGrid = findViewById(R.id.listGrid);
        linearList = findViewById(R.id.linearList);
        stickyWhatsApp = findViewById(R.id.stickyWhatsApp);

        hocLoadingDialog = new HocLoadingDialog(this);

        prefs = getSharedPreferences("Hindi", Activity.MODE_PRIVATE);
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        langPref = prefs.getString("Language", "");

        MoEngage moEngage = new MoEngage.Builder(getApplication(), "UUN7GSHBBH1UT5GCHI2EQ1KY")
                .setDataCenter(DataCenter.DATA_CENTER_3)
                .configureNotificationMetaData(new NotificationConfig(R.drawable.generic_notification, R.drawable.generic_notification, R.color.dark_grey_blue, "sound", true, true, true))
                .configureLogs(new LogConfig(LogLevel.VERBOSE, true))
                .build();
        MoEngage.initialise(moEngage);

        getCategories(this);

        stickyWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "919010100240" +"&text=" +
                            URLEncoder.encode(getResources().getString(R.string.whatsAppmsg), "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    public void Homepage(View view) {
        if (positions.size() != 0) {
            setRecommendedTopics(ChooseTopisActivity.this);
        } else Toast.makeText(this, "Please select at lease one course", Toast.LENGTH_SHORT).show();
    }

    public void getCategories(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject params = new JSONObject();
        JSONObject metaData = new JSONObject();
        try {
            params.put("appname","Hamstech");
            params.put("page","topics");
            params.put("apikey",getResources().getString(R.string.lblApiKey));
            params.put("lang",langPref);
            metaData.put("metadata", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hocLoadingDialog.showLoadingDialog();
        final String mRequestBody = metaData.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConstants.getTopics,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            coursesList.clear();
                            if (jsonObject.getString("status").equals("ok")) {
                                if (jsonObject.isNull("topicslist")){
                                    Toast.makeText(ChooseTopisActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray jsonArray = jsonObject.getJSONArray("topicslist");

                                    for (int i = 0; i<jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        CategoryDatamodel datamodel = new CategoryDatamodel();
                                        datamodel.setCategoryId(object.getString("topicId"));
                                        datamodel.setCategoryname(object.getString("topic_name"));
                                        datamodel.setCat_image_url(object.getString("topic_image"));
                                        datamodel.setCategory_description(object.getString("topic_description"));
                                        datamodel.setStatus(object.getString("status"));

                                        coursesList.add(datamodel);
                                    }
                                    chooseTopicsAdapter = new ChooseTopicsAdapter(ChooseTopisActivity.this,coursesList);
                                    listGrid.setLayoutManager(new GridLayoutManager(ChooseTopisActivity.this, 3));
                                    listGrid.addItemDecoration(new GridSpacingItemDecoration(3,30,false));
                                    listGrid.setAdapter(chooseTopicsAdapter);
                                }
                            }
                            hocLoadingDialog.hideDialog();
                            linearList.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChooseTopisActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
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
                    Toast.makeText(ChooseTopisActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

        };

        queue.add(stringRequest);

    }

    public class ChooseTopicsAdapter extends RecyclerView.Adapter<ChooseTopicsAdapter.ViewHolder> {

        Context context;
        ArrayList<CategoryDatamodel> coursesList = new ArrayList<>();

        public ChooseTopicsAdapter(Context context,ArrayList<CategoryDatamodel> coursesList){
            this.context=context;
            this.coursesList = coursesList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.topics_adapter, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            try {
                final boolean[] itemChecked = {false};
                holder.txtDescription.setText(coursesList.get(position).getCategoryname());
                Glide.with(context)

                        .load(coursesList.get(position).getCat_image_url())
                        //.placeholder(R.drawable.duser1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(holder.imgCategory);
                holder.imgCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemChecked[0] == false) {
                            if (positions.size() == 5) {
                                choosePopup(context);
                            } else {
                                holder.itemCheck.setChecked(true);
                                holder.itemCheck.setVisibility(View.VISIBLE);
                                positions.add(coursesList.get(position).getCategoryId());
                                itemChecked[0] = true;
                            }

                        } else {
                            holder.itemCheck.setVisibility(View.GONE);
                            holder.itemCheck.setChecked(false);
                            itemChecked[0] = false;
                            for (int i = 0; i < positions.size(); i++){
                                if (positions.get(i).equals(coursesList.get(position).getCategoryId())){
                                    positions.remove(positions.get(i));
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
            return coursesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgCategory;
            CheckBox itemCheck;
            TextView txtDescription;

            public ViewHolder(@NonNull View view) {
                super(view);
                imgCategory = view.findViewById(R.id.imgCategory);
                itemCheck = view.findViewById(R.id.itemCheck);
                txtDescription = view.findViewById(R.id.txtDescription);
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
                        Intent intent = new Intent(ChooseTopisActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ChooseTopisActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ChooseTopisActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
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
