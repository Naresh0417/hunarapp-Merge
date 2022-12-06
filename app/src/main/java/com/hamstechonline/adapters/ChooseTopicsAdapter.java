package com.hamstechonline.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hamstechonline.R;
import com.hamstechonline.datamodel.CategoryDatamodel;

import java.util.ArrayList;
import java.util.List;

public class ChooseTopicsAdapter extends RecyclerView.Adapter<ChooseTopicsAdapter.ViewHolder> {

    Context context;
    List<Integer> positions = new ArrayList<>();
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
        View v = inflater.inflate(R.layout.choose_topics_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            final boolean[] itemChecked = {false};
            holder.txtDescription.setText(coursesList.get(position).getCategory_description());
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
                            Toast.makeText(context, context.getResources().getString(R.string.lblAboveFive), Toast.LENGTH_SHORT).show();
                        } else {
                            holder.itemCheck.setChecked(true);
                            holder.itemCheck.setVisibility(View.VISIBLE);
                            positions.add(position);
                            itemChecked[0] = true;
                        }

                    } else {
                        holder.itemCheck.setVisibility(View.GONE);
                        holder.itemCheck.setChecked(false);
                        itemChecked[0] = false;
                        for (int i = 0; i < positions.size(); i++){
                            if (positions.get(i) == position){
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
