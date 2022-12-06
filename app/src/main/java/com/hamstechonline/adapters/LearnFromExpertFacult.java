package com.hamstechonline.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.datamodel.LessonsDataModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LearnFromExpertFacult extends RecyclerView.Adapter<LearnFromExpertFacult.ViewHolder> {

    Context context;
    ArrayList<LessonsDataModel> facultyArrayList = new ArrayList<>();

    public LearnFromExpertFacult(Context context,ArrayList<LessonsDataModel> facultyArrayList) {
        this.context = context;
        this.facultyArrayList = facultyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.learn_from_expert_facult, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            holder.txtTitle.setText(facultyArrayList.get(position).getFaculty_name());
            holder.txtDescription.setText(facultyArrayList.get(position).getFaculty_description());

            Glide.with(context)
                    .load(facultyArrayList.get(position).getFaculty_image())
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imgCategory);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return facultyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgCategory;
        TextView txtTitle,txtDescription;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgCategory = view.findViewById(R.id.profile_image);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
        }
    }
}
