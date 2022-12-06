package com.hamstechonline.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hamstechonline.R;
import com.hamstechonline.datamodel.LessonsDataModel;

import java.util.ArrayList;

public class CoursesFaqsAdapter extends RecyclerView.Adapter<CoursesFaqsAdapter.ViewHolder> {

    Context context;
    ArrayList<LessonsDataModel> faqsArrayList = new ArrayList<>();
    ArrayList<LessonsDataModel> ansArrayList = new ArrayList<>();

    public CoursesFaqsAdapter(Context context, ArrayList<LessonsDataModel> faqsArrayList, ArrayList<LessonsDataModel> ansArrayList){
        this.context=context;
        this.faqsArrayList = faqsArrayList;
        this.ansArrayList = ansArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.faqs_item_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.txtTitle.setText(faqsArrayList.get(position).getLesson_title());
            holder.txtDescription.setText(ansArrayList.get(position).getLesson_title());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ansArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDescription;
        public ViewHolder(@NonNull View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
        }
    }
}
