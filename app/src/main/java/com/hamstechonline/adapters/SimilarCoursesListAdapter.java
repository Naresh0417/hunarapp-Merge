package com.hamstechonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hamstechonline.R;
import com.hamstechonline.datamodel.homepage.MyCourse;
import com.hamstechonline.utils.LogEventsActivity;

import java.util.List;

public class SimilarCoursesListAdapter extends RecyclerView.Adapter<SimilarCoursesListAdapter.ViewHolder> {

    Context context;
    List<MyCourse> datamodels;
    LogEventsActivity logEventsActivity;

    //public MyCoursesLessonsListAdapter(Context context, List<MyCourse> datamodels){
    public SimilarCoursesListAdapter(Context context){
        this.context=context;
        this.datamodels = datamodels;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 7;
        //return datamodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imsSimilar;
        TextView txtSimilarTitle;
        public ViewHolder(@NonNull View view) {
            super(view);
            imsSimilar = view.findViewById(R.id.imsSimilar);
            txtSimilarTitle = view.findViewById(R.id.txtSimilarTitle);
        }
    }
}
