package com.hamstechonline.adapters;

import android.annotation.SuppressLint;
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
import com.hamstechonline.activities.MyCoursesPageActivity;
import com.hamstechonline.datamodel.SimilarCourse;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.utils.LogEventsActivity;
import com.hamstechonline.utils.UserDataConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SimilarCoursesListAdapter extends RecyclerView.Adapter<SimilarCoursesListAdapter.ViewHolder> {

    Context context;
    List<SimilarCourse> datamodels;
    LogEventsActivity logEventsActivity;
    String CategoryName = "",CourseLog = "",LessonLog="",ActivityLog,PagenameLog;

    public SimilarCoursesListAdapter(Context context, List<SimilarCourse> datamodels, String CourseLog){
    //public SimilarCoursesListAdapter(Context context){
        this.context=context;
        this.datamodels = datamodels;
        this.CourseLog = CourseLog;
        logEventsActivity = new LogEventsActivity();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.similar_courses_list_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Glide.with(context)
                    .load(datamodels.get(position).getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgCategory);
            holder.txtTopTitle.setText(datamodels.get(position).getCourseTitle());
            holder.txtBottomTitle.setText(datamodels.get(position).getCourseDescription());

            holder.listLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityLog = "clicked";
                    PagenameLog = "Similar courses";
                    getLogEvent(context);
                    Intent intent = new Intent(context, CoursePageActivity.class);
                    intent.putExtra("CategoryId",datamodels.get(position).getCourseId());
                    intent.putExtra("CategoryName",datamodels.get(position).getCategoryname());
                    intent.putExtra("CourseName",datamodels.get(position).getCourseTitle());
                    intent.putExtra("description",datamodels.get(position).getCourseDescription());
                    intent.putExtra("language",datamodels.get(position).getLanguage());
                    intent.putExtra("VideoUrl",datamodels.get(position).getVideoUrl());
                    intent.putExtra("statusNSDC",datamodels.get(position).getNsdcStatus());
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
        JSONObject data = new JSONObject();
        try {
            data.put("apikey",context.getResources().getString(R.string.lblApiKey));
            data.put("appname","Dashboard");
            data.put("mobile", UserDataConstants.userMobile);
            data.put("fullname", UserDataConstants.userName);
            data.put("email", UserDataConstants.userMail);
            data.put("category","MyCourses page");
            data.put("course",CourseLog);
            data.put("lesson",LessonLog);
            data.put("activity",ActivityLog);
            data.put("pagename",PagenameLog);
            logEventsActivity.LogEventsActivity(context,data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
