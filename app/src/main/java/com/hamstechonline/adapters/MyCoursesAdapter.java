package com.hamstechonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.activities.CoursePageActivity;
import com.hamstechonline.activities.MiniCourseDetailsActivity;
import com.hamstechonline.datamodel.CategoryDatamodel;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.utils.AppsFlyerEventsHelper;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.ViewHolder> {

    Context context;
    List<MyCourse> datamodels;
    LogEventsActivity logEventsActivity;
    String CategoryName,CourseLog,LessonLog,ActivityLog,PagenameLog;

    public MyCoursesAdapter(Context context, List<MyCourse> datamodels){
        this.context=context;
        this.datamodels = datamodels;
        logEventsActivity = new LogEventsActivity();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.mycourses_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            Glide.with(context)

                    .load(datamodels.get(position).getImageUrl())
                    //.placeholder(R.drawable.duser1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.imgCategory);

            holder.txtBottomTitle.setText(datamodels.get(position).getCourseTitle());
            holder.listLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MiniCourseDetailsActivity.class);
                    intent.putExtra("CategoryId",datamodels.get(position).getCourseId());
                    intent.putExtra("CategoryName",datamodels.get(position).getCourseTitle());
                    intent.putExtra("CourseName",datamodels.get(position).getCourseTitle());
                    intent.putExtra("description",datamodels.get(position).getCourseTitle());
                    intent.putExtra("language",datamodels.get(position).getLanguage());
                    intent.putExtra("VideoUrl",datamodels.get(position).getVideoUrl());
                    intent.putExtra("statusNSDC","0");
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
        TextView txtBottomTitle;
        RelativeLayout listLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgCategory = view.findViewById(R.id.imgCategory);
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
