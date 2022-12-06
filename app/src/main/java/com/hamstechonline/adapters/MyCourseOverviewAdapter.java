package com.hamstechonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.datamodel.CourseOverview;

import java.util.List;

public class MyCourseOverviewAdapter extends RecyclerView.Adapter<MyCourseOverviewAdapter.ViewHolder> {

    Context context;
    List<CourseOverview> coursesList;

    public MyCourseOverviewAdapter(Context context, List<CourseOverview> coursesList) {
        this.context = context;
        this.coursesList = coursesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.course_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.txtTitle.setText(coursesList.get(position).getContent());
            Glide.with(context)
                    .load(coursesList.get(position).getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imgCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtTitle;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgCategory = view.findViewById(R.id.img_item);
            txtTitle = view.findViewById(R.id.tv_name);
        }
    }
}
