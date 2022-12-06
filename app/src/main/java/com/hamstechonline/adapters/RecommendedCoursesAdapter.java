package com.hamstechonline.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.activities.CoursePageActivity;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecommendedCoursesAdapter extends RecyclerView.Adapter<RecommendedCoursesAdapter.ViewHolder> {

    Context context;
    ArrayList<CategoryDatamodel> datamodels;
    LogEventsActivity logEventsActivity;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog;

    public RecommendedCoursesAdapter(Context context,ArrayList<CategoryDatamodel> datamodels){
        this.context=context;
        this.datamodels = datamodels;
        logEventsActivity = new LogEventsActivity();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.recommended_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            Glide.with(context)

                    .load(datamodels.get(position).getCat_image_url())
                    //.placeholder(R.drawable.duser1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.imgCategory);

            holder.txtTopTitle.setText(datamodels.get(position).getCategory_Title());
            holder.txtBottomTitle.setText(datamodels.get(position).getCategory_description());
            holder.listLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (datamodels.get(position).getStatusNSDC().equals("1")){
                        CourseLog = "NSDC "+datamodels.get(position).getCategory_Title()+" "+datamodels.get(position).getCategory_language();
                        CategoryName = "";LessonLog = "";ActivityLog = "Click";PagenameLog = "Dashboard";
                        getLogEvent(context);
                    } else {
                        CourseLog = datamodels.get(position).getCategory_Title()+" "+datamodels.get(position).getCategory_language();
                        CategoryName = "";LessonLog = "";ActivityLog = "Click";PagenameLog = "Dashboard";
                        getLogEvent(context);
                    }
                    new AppsFlyerEventsHelper(context).EventCategory(datamodels.get(position).getCategoryname());
                    Intent intent = new Intent(context, CoursePageActivity.class);
                    intent.putExtra("CategoryId",datamodels.get(position).getCategoryId());
                    intent.putExtra("CategoryName",datamodels.get(position).getCategoryname());
                    intent.putExtra("CourseName",datamodels.get(position).getCategory_Title());
                    intent.putExtra("description",datamodels.get(position).getCategory_description());
                    intent.putExtra("language",datamodels.get(position).getCategory_language());
                    intent.putExtra("VideoUrl",datamodels.get(position).getCatVideoUrl());
                    intent.putExtra("statusNSDC",datamodels.get(position).getStatusNSDC());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datamodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtTopTitle,txtBottomTitle;
        RelativeLayout listLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgCategory = view.findViewById(R.id.imgCategory);
            txtTopTitle = view.findViewById(R.id.txtTopTitle);
            txtBottomTitle = view.findViewById(R.id.txtBottomTitle);
            listLayout = view.findViewById(R.id.listLayout);
        }
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
            data.put("email", UserDataConstants.userMail);
            data.put("category",CategoryName);
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            boolean logevent = logEventsActivity.LogEventsActivity(context,data);
            metaData.put("metadata", params);
            metaData.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
