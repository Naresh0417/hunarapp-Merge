package com.hamstechonline.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OurAwardsAdapter extends RecyclerView.Adapter<OurAwardsAdapter.ViewHolder> {

    Context context;
    int courseId;
    ArrayList<String> awardsImage;
    List names;

    public OurAwardsAdapter(Context context,ArrayList<String> awardsImage){
        this.context=context;
        this.courseId = courseId;
        this.awardsImage = awardsImage;
        names = Arrays.asList(context.getResources().getStringArray(R.array.ourAwardsItemList));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.our_awards_content, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.txtAwards.setText(names.get(position).toString());

            Glide.with(context)
                    .load(awardsImage.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgAward1);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return awardsImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAward1;
        TextView txtAwards;

        public ViewHolder(@NonNull View view) {
            super(view);
            imgAward1 = view.findViewById(R.id.imgAward1);
            txtAwards = view.findViewById(R.id.txtAwards);
        }
    }
}
